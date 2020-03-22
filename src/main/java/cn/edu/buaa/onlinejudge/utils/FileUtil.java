package cn.edu.buaa.onlinejudge.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.*;

public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    private static final String COURSEWARE_UPLOAD_DIR_IN_WINDOWS = "D:\\tmp\\courseware\\";
    private static final String COURSEWARE_UPLOAD_RELATIVE_DIR_IN_UNIX = "/upload/courseware/";

    private static final String CHECKPOINT_UPLOAD_DIR_IN_WINDOWS = "D:\\tmp\\checkpoint\\";
    private static final String CHECKPOINT_UPLOAD_RELATIVE_DIR_IN_UNIX = "/upload/checkpoint/";

    /**
     * 获取上传课件存放的绝对路径
     * @param subDirName - 子目录名
     * @return
     */
    public static String getCoursewareUploadPath(String subDirName){
        String systemName = System.getProperty("os.name").toLowerCase();
        String coursewareUploadPath;
        if(systemName.contains("windows")){
            //在Java中表示Windows中的目录分割可用Unix系统的'/'分隔符（'\\'也可以）
            coursewareUploadPath = COURSEWARE_UPLOAD_DIR_IN_WINDOWS +
                    subDirName + File.separator;
        } else {
            coursewareUploadPath = getProjectRootDir() +
                    COURSEWARE_UPLOAD_RELATIVE_DIR_IN_UNIX + subDirName + File.separator;
        }
        return coursewareUploadPath;
    }

    /**
     * 获取上传测试点存放的绝对路径
     * @param subDirName - 子目录名
     * @return
     */
    public static String getCheckpointUploadPath(String subDirName){
        String systemName = System.getProperty("os.name").toLowerCase();
        String checkpointUploadPath;
        if(systemName.contains("windows")){
            checkpointUploadPath = CHECKPOINT_UPLOAD_DIR_IN_WINDOWS +
                    subDirName + File.separator;
        } else {
            checkpointUploadPath = getProjectRootDir() +
                    CHECKPOINT_UPLOAD_RELATIVE_DIR_IN_UNIX + subDirName + File.separator;
        }
        return checkpointUploadPath;
    }

    /**
     * 获取项目在Linux或Windows（默认）系统运行时的临时文件夹路径
     * @return
     */
    public static String getTmpDirPath(){
        String systemName = System.getProperty("os.name").toLowerCase();
        if(systemName.contains("windows")){
            return "D:\\tmp\\";
        }else{
            return "/tmp/";
        }
    }

    /**
     * 获取项目根目录
     * 在IDEA中运行结果为："项目绝对地址"\target（如D:\Documents\IntelliJ IDEA\BUAAOJ）
     * 打包成jar包部署到Linux服务器结果为jar包运行所在目录（如/home/BUAAOJ）
     * @return
     */
    public static String getProjectRootDir(){
        File path;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        if( !path.exists() ){
            path = new File("");
        }
        return path.getAbsolutePath();
    }

    /**
     * 生成存储文件的路径，可避免文件重复
     * @param filePath - 待存储路径的文件夹
     * @param fileName - 文件名
     * @return 存储路径File
     */
    public static File getFileDest(String filePath, String fileName){
        //检测目录是否存在
        File parentFile = new File(filePath);
        if( !parentFile.exists() ){
            //新建文件夹
            parentFile.mkdirs();
        }
        File dest = new File(filePath + fileName);
        if( !dest.exists() ){
            return dest;
        }
        /**
         * 获取文件后缀名
         */
        //不能设置为null，因为String拼接时会将null值拼接为"null"
        String suffixName = "";
        int index = fileName.lastIndexOf(".");
        //index等于0，形如.gitignore文件，直接在文件名头部添加副本名
        if( index >= 0 ) {
            suffixName = fileName.substring(index);
        } else{
            //为了方便后续组成新的文件名，如果原文件名中没有小数点(.)，将index置为文件名长度值
            index = fileName.length();
        }
        //重复测试在文件名后（文件后缀名之前）添加"(i)"是否会产生文件名重复冲突
        for (int i = 1;; i++) {
            String newFileName = fileName.substring(0, index) + "(" + i + ")" + suffixName;
            dest = new File(filePath + newFileName);
            if( !dest.exists() ) break;
        }
        return dest;
    }

    /**
     * 拷贝文件
     * @param oldPath - 源资源文件绝对路径（如"/tmp/file.zip"）
     * @param newPath - 目的资源文件绝对路径(如"/home/file.zip")
     * @throws IOException
     */
    public static void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(newFile);;
        byte[] buffer=new byte[1024];
        while((in.read(buffer)) != -1){
            out.write(buffer);
        }
        in.close();
        out.close();
        oldFile.delete();
    }

    /**
     * 删除文件，可以是文件也可以是文件夹
     * @param absoluteFileName - 文件或文件夹的绝对路径
     * @return
     */
    public static boolean delete(String absoluteFileName){
        File file = new File(absoluteFileName);
        if( !file.exists() ){
            LOGGER.info("删除文件失败:" + absoluteFileName + "不存在！");
            return false;
        } else{
            if( file.isFile() ){
                return deleteFile(absoluteFileName);
            } else{
                return deleteDirectory(absoluteFileName);
            }
        }
    }

    /**
     * 删除单个文件
     * @param absoluteFileName - 文件的绝对路径
     * @return
     */
    public static boolean deleteFile(String absoluteFileName){
        File file = new File(absoluteFileName);
        //如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if( file.exists() && file.isFile() ){
            if( file.delete() ){
                LOGGER.info("删除单个文件" + absoluteFileName + "成功！");
                return true;
            } else{
                LOGGER.info("删除单个文件" + absoluteFileName + "失败！");
                return false;
            }
        } else{
            LOGGER.info("删除单个文件失败：" + absoluteFileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录（递归删除目录下所有文件及文件夹）
     * @param absoluteDir - 目录的绝对路径
     * @return
     */
    public static boolean deleteDirectory(String absoluteDir){
        //如果dir不以文件分隔符结尾，自动添加文件分隔符
        if( !absoluteDir.endsWith(File.separator) ){
            absoluteDir += File.separator;
        }
        File dirFile = new File(absoluteDir);
        if( (!dirFile.exists()) || (!dirFile.isDirectory()) ){
            LOGGER.info("删除目录失败：" + absoluteDir + "不存在！");
            return false;
        }
        boolean flag = true;
        //删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (File file : files) {
            //删除子文件
            if( file.isFile() ){
                flag = deleteFile(file.getAbsolutePath());
                if( !flag ) break;
            } else if( file.isDirectory() ){  //删除子目录
                flag = deleteDirectory(file.getAbsolutePath());
                if( !flag ) break;
            }
        }
        if( !flag ){
            LOGGER.info("删除目录失败！");
            return false;
        }
        //删除当前目录
        if( dirFile.delete() ){
            LOGGER.info("删除目录" + absoluteDir + "成功！");
            return true;
        } else{
            return false;
        }
    }
}
