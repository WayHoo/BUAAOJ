package cn.edu.buaa.onlinejudge.controller;

import cn.edu.buaa.onlinejudge.model.Course;
import cn.edu.buaa.onlinejudge.model.Student;
import cn.edu.buaa.onlinejudge.model.Teacher;
import cn.edu.buaa.onlinejudge.service.*;
import cn.edu.buaa.onlinejudge.utils.HttpResponseWrapperUtil;
import cn.edu.buaa.onlinejudge.utils.MD5Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(tags = "课程相关接口")
@RestController
@RequestMapping(value = "BUAAOJ/courses")
public class CourseController {

    private final CourseService courseService;

    private final StudentService studentService;

    private final TeacherService teacherService;

    public CourseController(CourseService courseService,
                            StudentService studentService,
                            TeacherService teacherService) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @ApiOperation("学生查看所有课程接口")
    @GetMapping(value = "/getAllCourses")
    public HttpResponseWrapperUtil getAllCourses() {
        List<Course> allCourses = courseService.getAllCourses();
        return wrapCourseList2JSON(allCourses);
    }

    /**
     * 学生根据课程名称模糊查询课程接口
     * 采用前缀完全匹配模式，查询范围为所有课程
     * @param courseName
     * @return
     */
    @ApiOperation("学生根据课程名称模糊查询课程接口")
    @PostMapping(value = "/fuzzyQueryCoursesByName")
    public HttpResponseWrapperUtil fuzzyQueryCoursesByName(@RequestParam("courseName") String courseName) {
        List<Course> courses = courseService.fuzzyQueryCoursesByCourseName(courseName);
        if( courses == null || courses.size() == 0 ){
            return new HttpResponseWrapperUtil(null, -1, "无相关查询信息");
        }
        return wrapCourseList2JSON(courses);
    }

    /**
     * 学生查看课程主页
     * 课程主页对所有学生开放（尚未加入课程的学生依然可见）
     * @param studentId - 学生ID
     * @param courseId - 课程ID
     * @return 课程基本信息的JSON数据
     */
    @ApiOperation("学生查看课程主页接口")
    @GetMapping(value = "/getCourseHomepage/{studentId}/{courseId}")
    public HttpResponseWrapperUtil getCourseHomepage(@PathVariable("studentId") long studentId,
                                                     @PathVariable("courseId") int courseId) {
        Course course = courseService.getCourseById(courseId);
        if( course == null ) {
            return new HttpResponseWrapperUtil(null, -1, "访问失败");
        }
        Map<String,Object> data = new HashMap<>();
        data.put("introduction", course.getIntroduction());
        data.put("downloadable", courseService.isCoursewareExists(courseId) ? 1 : 0);
        int status = courseService.studentJoinCourseStatus(studentId, courseId);
        data.put("isStudentAccept", status);
        return new HttpResponseWrapperUtil(data);
    }

    /**
     * 处理学生的加入课程申请，将请求插入数据库，等待课程教师处理
     * @param studentId - 学生ID
     * @param courseId - 课程ID
     * @return
     */
    @ApiOperation("学生申请加入课程接口")
    @GetMapping(value = "/joinCourse/{studentId}/{courseId}")
    public HttpResponseWrapperUtil joinCourse(@PathVariable("studentId") long studentId,
                                              @PathVariable("courseId") int courseId) {
        Course course = courseService.getCourseById(courseId);
        Student student = studentService.getStudentById(studentId);
        if( course == null || student == null ){
            return new HttpResponseWrapperUtil(null, -1, "操作失败");
        }
        //学生是否提交加入课程申请并通过。0 - 申请尚未通过，1 - 已通过，-1 - 未提交申请
        int status = courseService.studentJoinCourseStatus(studentId, courseId);
        if( status >= 0 ){
            return new HttpResponseWrapperUtil(null, -1, "请勿重复提交申请");
        }
        //status = -1的情况：学生和课程均存在且学生尚未提交申请
        courseService.joinCourse(studentId,courseId);
        return new HttpResponseWrapperUtil(null);
    }

    /**
     * 学生查看课程成员
     * 课程成员对所有学生开放（尚未加入课程的学生依然可见）
     * @param studentId - 学生ID
     * @param courseId - 课程ID
     * @return
     */
    @ApiOperation("学生查看课程成员接口")
    @GetMapping(value = "/getCourseMembers/{studentId}/{courseId}")
    public HttpResponseWrapperUtil getCourseMembers(@PathVariable("studentId") long studentId,
                                                    @PathVariable("courseId") int courseId) {
        Course course = courseService.getCourseById(courseId);
        Student student = studentService.getStudentById(studentId);
        if( course == null || student == null ){
            return new HttpResponseWrapperUtil(null, -1, "访问失败");
        }
        //获取课程中已审核通过的成员
        List<Map<String,Object>> data = courseService.getCourseMembers(courseId,1);
        //学生查看课程成员不需要返回学生ID
        for (Map<String, Object> metadata : data) {
            metadata.remove("studentId");
        }
        return new HttpResponseWrapperUtil(data);
    }

    /**
     * 审核已通过的教师查看个人开课列表，无法访问其余教师的课程
     * @param teacherId - 教师ID
     * @return
     */
    @ApiOperation("教师查看课程列表接口")
    @GetMapping(value = "/getCourseList/{teacherId}")
    public HttpResponseWrapperUtil getCourseList(@PathVariable("teacherId") long teacherId) {
        Teacher teacher = teacherService.getTeacherById(teacherId);
        if( teacher == null ){
            return new HttpResponseWrapperUtil(null, -1, "该教师不存在");
        }
        if( !teacher.isAccept() ){
            return new HttpResponseWrapperUtil(null, 100, "该教师尚未通过审核");
        }
        List<Course> courses = courseService.getCoursesOfTeacher(teacherId);
        return wrapCourseList2JSON(courses);
    }

    /**
     * 教师根据课程名称模糊查询课程
     * 采用前缀完全匹配模式，查询范围为指定教师的所有课程
     * @param teacherId - 教师ID
     * @param courseName - 课程名称
     * @return
     */
    @ApiOperation("教师根据课程名称查找课程接口")
    @PostMapping(value = "/fuzzyQueryCoursesOfTeacherByName")
    public HttpResponseWrapperUtil fuzzyQueryCoursesOfTeacherByName(@RequestParam("teacherId") long teacherId,
                                                                    @RequestParam("courseName") String courseName) {
        Teacher teacher = teacherService.getTeacherById(teacherId);
        if( teacher == null ){
            return new HttpResponseWrapperUtil(null, -1, "该教师不存在");
        }
        if( !teacher.isAccept() ){
            return new HttpResponseWrapperUtil(null, -1, "该教师尚未通过审核");
        }
        List<Course> courses = courseService.fuzzyQueryCoursesOfTeacherByCourseName(teacherId, courseName);
        if( courses == null || courses.size() == 0 ){
            return new HttpResponseWrapperUtil(null, 200, null);
        }
        return wrapCourseList2JSON(courses);
    }

    /**
     * 教师查看课程信息（课程名称和简介）
     * @param teacherId - 教师ID
     * @param courseId - 课程ID
     * @return
     */
    @ApiOperation("教师查看课程信息接口")
    @GetMapping(value = "/getCourseById/{teacherId}/{courseId}")
    public HttpResponseWrapperUtil getCourseById(@PathVariable("teacherId") long teacherId,
                                                 @PathVariable("courseId") int courseId) {
        Course course = courseService.getCourseById(courseId);
        Teacher teacher = teacherService.getTeacherById(teacherId);
        if( course == null ){
            return new HttpResponseWrapperUtil(null, -1, "该课程不存在");
        }
        if( teacher == null ){
            return new HttpResponseWrapperUtil(null, -1, "该教师不存在");
        }
        if( !teacher.isAccept() || teacherId != course.getTeacherId() ){
            return new HttpResponseWrapperUtil(null, -1, "教师权限不足");
        }
        Map<String,Object> data = new HashMap<>();
        data.put("courseName",course.getCourseName());
        data.put("introduction",course.getIntroduction());
        return new HttpResponseWrapperUtil(data);
    }

    /**
     * 教师新建课程，前提是教师具备开课权限
     * @param course - 课程对象
     * @return
     */
    @ApiOperation("教师新建课程接口")
    @PostMapping(value = "/createCourse")
    public HttpResponseWrapperUtil createCourse(@RequestBody Course course) {
        Teacher teacher = teacherService.getTeacherById(course.getTeacherId());
        if( teacher == null ){
            return new HttpResponseWrapperUtil(null, -1, "该教师不存在");
        }
        if( !teacher.isAccept() ){
            return new HttpResponseWrapperUtil(null, -1, "教师尚未通过审核");
        }
        courseService.insertCourse(course);
        Map<String,Object> data = new HashMap<>();
        data.put("courseId", course.getCourseId());
        return new HttpResponseWrapperUtil(data);
    }

    /**
     * 教师删除课程，包括课程下的所有竞赛、题目、题目测试点文件和课件
     * @param teacherId - 教师ID
     * @param courseId - 课程ID
     * @return
     */
    @ApiOperation("教师删除课程接口")
    @GetMapping(value = "/deleteCourse/{teacherId}/{courseId}")
    public HttpResponseWrapperUtil deleteCourse(@PathVariable("teacherId") int teacherId,
                                                @PathVariable("courseId") int courseId) {
        Course course = courseService.getCourseById(courseId);
        if( course == null ){
            return new HttpResponseWrapperUtil(null, -1, "该课程不存在");
        }
        if( course.getTeacherId() != teacherId ){
            return new HttpResponseWrapperUtil(null, -1, "教师权限不足");
        }
        //删除课程、课程中课件、课程下所有竞赛、竞赛中所有题目、题目对应的测试点文件
        courseService.deleteCourse(courseId);
        return new HttpResponseWrapperUtil(null);
    }

    /**
     * 教师修改课程信息（课程名称和简介）
     * @param course - 课程对象
     * @return
     */
    @ApiOperation("教师修改课程信息")
    @PostMapping(value = "/updateCourse")
    public HttpResponseWrapperUtil updateCourseIntro(@RequestBody Course course) {
        Course realCourse = courseService.getCourseById(course.getCourseId());
        if( realCourse == null ){
            return new HttpResponseWrapperUtil(null, -1, "该课程不存在");
        }
        if( realCourse.getTeacherId() != course.getTeacherId() ){
            return new HttpResponseWrapperUtil(null, -1, "教师权限不足");
        }
        courseService.updateCourse(course);
        return new HttpResponseWrapperUtil(null);
    }

    /**
     * 教师查看课程成员
     * @param teacherId - 教师ID
     * @param courseId - 课程ID
     * @param tag - 0表示查看尚未通过的成员、1表示查看已通过审核的成员
     * @return
     */
    @ApiOperation("教师查看课程成员接口")
    @GetMapping(value = "/auditCourseMembers/{teacherId}/{courseId}/{tag}")
    public HttpResponseWrapperUtil auditCourseMembers(@PathVariable("teacherId") long teacherId,
                                                      @PathVariable("courseId") int courseId,
                                                      @PathVariable("tag") int tag) {
        Course course = courseService.getCourseById(courseId);
        if( course == null ){
            return new HttpResponseWrapperUtil(null, -1, "该课程不存在");
        }
        if( teacherId != course.getTeacherId() ){
            return new HttpResponseWrapperUtil(null, -1, "教师权限不足");
        }
        List<Map<String,Object>> data = courseService.getCourseMembers(courseId,tag);
        return new HttpResponseWrapperUtil(data);
    }

    /**
     * 教师修改课程成员的角色
     * @param teacherId - 教师ID
     * @param courseId - 课程ID
     * @param studentId - 学生ID
     * @param role - 角色值，0表示学生，1表示助教
     * @return
     */
    @ApiOperation("教师修改课程成员角色接口")
    @GetMapping(value = "/changeCourseMemberRole/{teacherId}/{courseId}/{studentId}/{role}")
    public HttpResponseWrapperUtil changeCourseMemberRole(@PathVariable("teacherId") long teacherId,
                                                          @PathVariable("courseId") int courseId,
                                                          @PathVariable("studentId") long studentId,
                                                          @PathVariable("role") int role) {
        Course course = courseService.getCourseById(courseId);
        if( course == null ){
            return new HttpResponseWrapperUtil(null, -1, "该课程不存在");
        }
        if( teacherId != course.getTeacherId() ){
            return new HttpResponseWrapperUtil(null, -1, "教师权限不足");
        }
        if( role != 0 && role != 1 ){
            return new HttpResponseWrapperUtil(null, -1, "角色非法");
        }
        courseService.updateCourseMemberRole(courseId, studentId, role);
        return new HttpResponseWrapperUtil(null);
    }

    /**
     * 教师审核学生，反转学生的加入课程申请状态
     * 若学生当前已加入课程，该操作会将学生移除课程
     * 若学生提交的申请尚未通过，该操作会通过学生申请
     * @param teacherId - 教师ID
     * @param courseId - 课程ID
     * @param studentId - 学生ID
     * @return
     */
    @ApiOperation("教师审核学生接口")
    @GetMapping(value = "/auditStudent/{teacherId}/{courseId}/{studentId}")
    public HttpResponseWrapperUtil auditStudent(@PathVariable("teacherId") long teacherId,
                                                @PathVariable("courseId") int courseId,
                                                @PathVariable("studentId") long studentId) {
        Course course = courseService.getCourseById(courseId);
        if( course == null ){
            return new HttpResponseWrapperUtil(null, -1, "该课程不存在");
        }
        if( teacherId != course.getTeacherId() ){
            return new HttpResponseWrapperUtil(null, -1, "教师权限不足");
        }
        courseService.revertStudentAcceptance(courseId, studentId);
        return new HttpResponseWrapperUtil(null);
    }

    /**
     * 教师重置其课程中学生密码
     * 重置后的密码与该学生的注册邮箱一致
     * @param courseId
     * @param studentId
     * @return
     */
    @ApiOperation("教师重置其课程中学生密码接口")
    @GetMapping(value = "/resetStudentPasswordByTeacher/{courseId}/{studentId}")
    public HttpResponseWrapperUtil resetStudentPasswordByTeacher(@PathVariable(value = "courseId") int courseId,
                                                                 @PathVariable(value = "studentId") long studentId){
        Course course = courseService.getCourseById(courseId);
        if( course == null ){
            return new HttpResponseWrapperUtil(null, -1, "该课程不存在");
        }
        if( courseService.studentJoinCourseStatus(studentId, courseId) != 1 ){
            return new HttpResponseWrapperUtil(null, -1, "教师权限不足");
        }
        Student student = studentService.getStudentById(studentId);
        //将学生密码重置为账号
        student.setPassword(MD5Util.encryptedByMD5(student.getEmail()));
        studentService.resetPassword(student);
        return new HttpResponseWrapperUtil(null);
    }

    /**
     * 将课程对象列表基本信息封装成JSON数据
     * @param courseList - 课程对象列表
     * @return JSON数据
     */
    public HttpResponseWrapperUtil wrapCourseList2JSON(List<Course> courseList){
        if( courseList == null ){
            return new HttpResponseWrapperUtil(null, -1, "查询失败");
        }
        List<Object> data = new ArrayList<>();
        for (Course course : courseList) {
            Map<String,Object> metadata = new HashMap<>();
            metadata.put("courseId", course.getCourseId());
            metadata.put("courseName", course.getCourseName());
            data.add(metadata);
        }
        return new HttpResponseWrapperUtil(data);
    }
}