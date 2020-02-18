package cn.edu.buaa.onlinejudge.controller;

import cn.edu.buaa.onlinejudge.utils.FileUtil;
import cn.edu.buaa.onlinejudge.utils.HttpResponseWrapperUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

@Api(tags = "文件相关接口")
@RestController
@RequestMapping(value = "BUAAOJ/files")
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @ApiOperation("上传单个文件接口")
    @PostMapping(value = "/singleUpload")
    public HttpResponseWrapperUtil singleUpload(@RequestParam("file") MultipartFile file){
        if( file.isEmpty() ){
            return new HttpResponseWrapperUtil(null, -1, "上传失败，请选择文件");
        }
        //获取文件名
        String fileName = file.getOriginalFilename();
        //设置文件存储路径（Windows环境）
        String filePath = FileUtil.getUploadPath();
        File dest = FileUtil.getFileDest(filePath,fileName);
        try {
            file.transferTo(dest);
            LOGGER.info("上传成功");
            return new HttpResponseWrapperUtil(null,200,"上传成功");
        } catch (IOException e) {
            LOGGER.info("上传失败");
            e.printStackTrace();
        }
        return new HttpResponseWrapperUtil(null, -1, "上传失败");
    }

    @ApiOperation("批量上传文件接口")
    @PostMapping("/batchUpload")
    public HttpResponseWrapperUtil batchUpload(HttpServletRequest request){
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
        if( files == null ){
            return new HttpResponseWrapperUtil(null, -1, "上传失败，请选择文件");
        }
        //设置文件存储路径（Windows环境）
        String filePath = FileUtil.getUploadPath();
        int i = 0;
        for (i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            if( file.isEmpty() ) break;
            String fileName = file.getOriginalFilename();
            File dest = FileUtil.getFileDest(filePath,fileName);
            try {
                file.transferTo(dest);
                LOGGER.info("第" + (i+1) + "个文件上传成功");
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        if( i != files.size()) {
            LOGGER.info("第" + (i+1) + "个文件上传失败");
            return new HttpResponseWrapperUtil(null, -1, "第" + (i+1) + "个文件上传失败");
        }
        return new HttpResponseWrapperUtil(null,200,"上传成功");
    }

    @ApiOperation("下载文件接口")
    @PostMapping("/downloadFile")
    public void downloadFile(@RequestParam("fileName") String fileName,
                             HttpServletResponse response) throws UnsupportedEncodingException {
        LOGGER.info("待下载的文件名为：" + fileName);
        if ( fileName != null ) {
            //设置文件路径
            String uploadPath = FileUtil.getUploadPath();
            File file = new File(uploadPath + fileName);
            if (file.exists()) {
                //设置强制下载不打开
                response.setContentType("application/force-download");
                //设置文件名
                response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer,0, i);
                        i = bis.read(buffer);
                    }
                    LOGGER.info("下载成功");
                    return ;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        LOGGER.info("下载失败");
    }

    @ApiOperation("获取项目根目录接口")
    @GetMapping("/getProjectRootDir")
    public HttpResponseWrapperUtil getProjectRootDir(){
        LOGGER.info("ProjectRootDir = " + FileUtil.getProjectRootDir());
        return new HttpResponseWrapperUtil(null,200, FileUtil.getProjectRootDir());
    }
}
