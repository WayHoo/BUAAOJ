package cn.edu.buaa.onlinejudge.utils;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class FileUtil {
    private static final String UPLOAD_DIR_IN_WINDOWS = "D:\\resources\\";
    private static final String UPLOAD_RELATIVE_DIR_IN_LINUX = "/upload/courseware/";

    public static String getUploadPath(){
        String systemName = System.getProperty("os.name").toLowerCase();
        String uploadPath = UPLOAD_DIR_IN_WINDOWS;
        if(systemName.contains("linux")){
            uploadPath = getProjectRootDir() + UPLOAD_RELATIVE_DIR_IN_LINUX;
        }
        return uploadPath;
    }

    /**
     * 获取项目根目录
     * 在IDEA中运行结果为："项目绝对地址"\target（如D:\Documents\IntelliJ IDEA\BUAAOJ）
     * 打包成jar包部署到Linux服务器结果为jar包运行所在目录（如/home/BUAAOJ）
     * @return
     */
    public static String getProjectRootDir(){
        File path = null;
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
            String newFileName = fileName.substring(0,index) + "(" + i + ")" + suffixName;
            dest = new File(filePath + newFileName);
            if( !dest.exists() ) break;
        }
        return dest;
    }
}
