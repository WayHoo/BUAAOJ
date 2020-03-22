package cn.edu.buaa.onlinejudge.utils;

import cn.edu.buaa.onlinejudge.controller.FileController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    private static final int BUFFER_SIZE = 2 * 1024;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    /**
     * 将指定文件目录下的所有文件压缩成ZIP
     * @param srcDir - 待压缩文件夹路径
     * @param out - 压缩文件输出流。（注意：不能是待压缩文件夹路径构造的输出流，否则会将压缩后的压缩包再次压缩进压缩包）
     * @param KeepDirStructure - 是否保留原来的目录结构，true:保留目录结构，
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException - 压缩失败会抛出运行时异常
     */
    public static void toZip(String srcDir, OutputStream out,
                             boolean KeepDirStructure) throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            long end = System.currentTimeMillis();
            LOGGER.info("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtil", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将指定的File对象列表压缩成ZIP到指定输出流
     * @param srcFiles - 源File对象列表
     * @param out - 文件输出流对象
     * @throws RuntimeException
     */
    public static void toZip(List<File> srcFiles, OutputStream out) throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtil", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 递归压缩方法
     * @param sourceFile - 源文件
     * @param zos - zip输出流
     * @param name - 压缩后的名称
     * @param KeepDirStructure - 是否保留原来的目录结构，true:保留目录结构，
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception {

        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            //向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            //copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            //Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                //需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    //空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    //没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    //判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        //注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        //不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }

    public static void unZip(String sourceFileName, String targetDir) {
        unZip(new File(sourceFileName), targetDir);
    }

    /**
     * 将sourceFile解压到targetDir
     * @param sourceFile - 源压缩文件
     * @param targetDir - 目的文件夹绝对路径
     * @throws FileNotFoundException
     */
    public static boolean unZip(File sourceFile, String targetDir) {
        if( !sourceFile.exists() ){
            LOGGER.warn("cannot find the file: " + sourceFile.getPath());
            return false;
        }
        if( !targetDir.endsWith(File.separator) ){
            targetDir += File.separator;
        }
        ZipFile zipFile = null;
        try {
            //解决中文文件名乱码问题
            zipFile = new ZipFile(sourceFile, Charset.forName("GBK"));
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements() ){
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if( entry.isDirectory() ){
                    String dirPath = targetDir + entry.getName();
                    createDirIfNotExist(dirPath);
                } else{
                    File targetFile = new File(targetDir + entry.getName());
                    createFileIfNotExist(targetFile);
                    InputStream is = null;
                    FileOutputStream fos = null;
                    is = zipFile.getInputStream(entry);
                    fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[1024];
                    while( (len = is.read(buf)) != -1 ){
                        fos.write(buf, 0, len);
                    }
                    is.close();
                    fos.close();
                }
            }
        } catch (ZipException e) {
            LOGGER.warn("ZipException occurred.");
            return false;
        } catch (IOException e) {
            LOGGER.warn("IOException occurred.");
            return false;
        } finally {
            if( zipFile != null ){
                try {
                    zipFile.close();
                } catch (IOException e) {
                    LOGGER.warn("IOException occurred.");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 创建文件夹目录
     * @param path - 文件夹绝对路径，最终指向目录，例如/home/dir
     */
    public static void createDirIfNotExist(String path){
        File file = new File(path);
        if( !file.exists() ){
            file.mkdirs();
        }
    }

    /**
     * 创建文件
     * @param file - 文件
     */
    public static void createFileIfNotExist(File file) {
        createDirIfNotExist(file.getParent());
        try {
            file.createNewFile();
        } catch (IOException e) {
            LOGGER.warn("IOException occurred while creating new file.");
        }
    }
}
