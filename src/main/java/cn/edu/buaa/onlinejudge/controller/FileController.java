package cn.edu.buaa.onlinejudge.controller;

import cn.edu.buaa.onlinejudge.model.Course;
import cn.edu.buaa.onlinejudge.model.Problem;
import cn.edu.buaa.onlinejudge.service.CourseService;
import cn.edu.buaa.onlinejudge.service.ProblemService;
import cn.edu.buaa.onlinejudge.service.TeacherService;
import cn.edu.buaa.onlinejudge.utils.FileUtil;
import cn.edu.buaa.onlinejudge.utils.HttpResponseWrapperUtil;
import cn.edu.buaa.onlinejudge.utils.ZipUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ProblemService problemService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @ApiOperation("上传题目测试用例接口")
    @PostMapping(value = "/uploadProblemCheckpoints/{contestId}/{problemId}")
    public HttpResponseWrapperUtil singleUpload(@PathVariable("contestId") int contestId,
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
        return null;
//        //获取文件名
//        String fileName = file.getOriginalFilename();
//        //设置文件存储路径（Windows环境）
//        String filePath = FileUtil.getUploadPath(Integer.toString(courseId));
//        File dest = FileUtil.getFileDest(filePath,fileName);
//        try {
//            file.transferTo(dest);
//            LOGGER.info("上传成功");
//            toZip(filePath);
//            return new HttpResponseWrapperUtil(null,200,"上传成功");
//        } catch (IOException e) {
//            LOGGER.info("上传失败");
//            e.printStackTrace();
//        }
//        return new HttpResponseWrapperUtil(null, -1, "上传失败");
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
            return new HttpResponseWrapperUtil(null, -1, "课程不存在");
        }
        if( course.getTeacherId() != teacherId ){
            return new HttpResponseWrapperUtil(null, -1, "上传权限不足");
        }
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
        if( files == null ){
            return new HttpResponseWrapperUtil(null, -1, "上传失败，请选择文件");
        }
        //设置文件存储路径（Windows环境）
        String filePath = FileUtil.getUploadPath(Integer.toString(courseId));
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
        try {
            toZip(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HttpResponseWrapperUtil(null,200,"上传成功");
    }

    @ApiOperation("打包下载课程所有课件接口")
    @GetMapping("/downloadCourseware/{studentId}/{courseId}")
    public void downloadCourseware(@PathVariable("studentId") long studentId,
                                   @PathVariable("courseId") int courseId,
                             HttpServletResponse response) throws UnsupportedEncodingException {
        if( courseService.isStudentJoinCourse(studentId, courseId) != 1 ){
            return;
        }
        String filePath = FileUtil.getUploadPath(Integer.toString(courseId));
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
        FileUtil.removeFile(tmpPath + zipName,filePath + zipName);
    }
}
