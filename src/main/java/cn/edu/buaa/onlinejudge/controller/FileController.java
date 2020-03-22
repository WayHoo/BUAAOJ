package cn.edu.buaa.onlinejudge.controller;

import cn.edu.buaa.onlinejudge.model.Course;
import cn.edu.buaa.onlinejudge.model.Problem;
import cn.edu.buaa.onlinejudge.service.CourseService;
import cn.edu.buaa.onlinejudge.service.ProblemService;
import cn.edu.buaa.onlinejudge.utils.DateUtil;
import cn.edu.buaa.onlinejudge.utils.FileUtil;
import cn.edu.buaa.onlinejudge.utils.HttpResponseWrapperUtil;
import cn.edu.buaa.onlinejudge.utils.ZipUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
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

    private final CourseService courseService;

    private final ProblemService problemService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    public FileController(CourseService courseService, ProblemService problemService) {
        this.courseService = courseService;
        this.problemService = problemService;
    }

    @ApiOperation("上传题目测试用例接口")
    @PostMapping(value = "/uploadProblemCheckpoints/{contestId}/{problemId}")
    public HttpResponseWrapperUtil uploadProblemCheckpoints(@PathVariable("contestId") int contestId,
                                                            @PathVariable("problemId") long problemId,
                                                            @RequestParam("checkpoint") MultipartFile checkpoint){
        Problem problem = problemService.getProblemById(problemId);
        if( problem == null ){
            return new HttpResponseWrapperUtil(null, -1, "题目不存在");
        }
        if( problem.getContestId() != contestId ){
            return new HttpResponseWrapperUtil(null, -1, "上传权限不足");
        }
        if( checkpoint.isEmpty() ){
            return new HttpResponseWrapperUtil(null, -1, "上传失败，请选择文件");
        }
        //获取原始文件名
        String fileRealName = checkpoint.getOriginalFilename();
        //获取点号的位置
        int pointIndex = fileRealName.lastIndexOf(".");
        //获取文件后缀
        String fileSuffix = fileRealName.substring(pointIndex);
        //新文件名,时间戳形式yyyyMMddHHmmssSSS
        String fileNewName = DateUtil.getNowTimeForUpload();
        //新文件完整名（含后缀）
        String saveFileName = fileNewName.concat(fileSuffix);
        //获取系统临时文件夹路径
        String tmpFilePath = FileUtil.getTmpDirPath();
        File savedFile = new File(tmpFilePath, saveFileName);
        try {
            FileUtils.copyInputStreamToFile(checkpoint.getInputStream(), savedFile);
        } catch (IOException e) {
            e.printStackTrace();
            return new HttpResponseWrapperUtil(null, -1, "上传失败");
        }
        //将测试点压缩包解压到指定路径
        ZipUtil.unZip(savedFile, FileUtil.getCheckpointUploadPath(Long.toString(problemId)));
        //删除系统的临时测试点压缩包
        savedFile.delete();
        return new HttpResponseWrapperUtil(null);
    }

    /**
     * 完成上传之后将课件文件夹中的所有文件打包为ZIP并存放于课件文件夹中，便于下载课件
     * @param teacherId - 教师ID
     * @param courseId - 课程ID
     * @param request
     * @return
     */
    @ApiOperation("上传课件接口")
    @PostMapping("/uploadCourseware/{teacherId}/{courseId}")
    public HttpResponseWrapperUtil uploadCourseware(@PathVariable("teacherId") long teacherId,
                                                    @PathVariable("courseId") int courseId,
                                                    HttpServletRequest request){
        Course course = courseService.getCourseById(courseId);
        if( course == null ){
            return new HttpResponseWrapperUtil(null, -1, "该课程不存在");
        }
        if( course.getTeacherId() != teacherId ){
            return new HttpResponseWrapperUtil(null, -1, "教师权限不足");
        }
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
        if( files == null ){
            return new HttpResponseWrapperUtil(null, -1, "上传失败，请选择文件");
        }
        //获取文件存储路径
        String filePath = FileUtil.getCoursewareUploadPath(Integer.toString(courseId));
        int i;
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
        try {
            toZip(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HttpResponseWrapperUtil(null);
    }

    @ApiOperation("打包下载课程所有课件接口")
    @GetMapping("/downloadCourseware/{studentId}/{courseId}")
    public void downloadCourseware(@PathVariable("studentId") long studentId,
                                   @PathVariable("courseId") int courseId,
                             HttpServletResponse response) throws UnsupportedEncodingException {
        if( courseService.studentJoinCourseStatus(studentId, courseId) != 1 ){
            return;
        }
        String filePath = FileUtil.getCoursewareUploadPath(Integer.toString(courseId));
        LOGGER.info("待下载的文件目录为：" + filePath);
        //设置文件路径
        File zipFile = new File(filePath + "courseware.zip");
        if (zipFile.exists()) {
            //设置强制下载不打开
            response.setContentType("application/force-download");
            //设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("courseware.zip", "UTF-8"));
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(zipFile);
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
        LOGGER.info("下载失败");
    }

    /**
     * 将指定文件夹压缩成ZIP并放在该文件夹下
     * @param filePath
     * @throws IOException
     */
    public void toZip(String filePath) throws IOException {
        String zipName = "courseware.zip";
        File zipFile = new File(filePath + zipName);
        zipFile.delete();
        String tmpPath = FileUtil.getTmpDirPath();
        File tmpFile = new File(tmpPath + zipName);
        File parentFile = tmpFile.getParentFile();
        if( !parentFile.exists() ){
            parentFile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(tmpFile);
        ZipUtil.toZip(filePath,fos,true);
        FileUtil.copyFile(tmpPath + zipName,filePath + zipName);
    }
}
