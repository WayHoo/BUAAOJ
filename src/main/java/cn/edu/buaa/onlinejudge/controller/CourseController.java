package cn.edu.buaa.onlinejudge.controller;

import cn.edu.buaa.onlinejudge.model.Course;
import cn.edu.buaa.onlinejudge.model.Student;
import cn.edu.buaa.onlinejudge.service.*;
import cn.edu.buaa.onlinejudge.utils.HttpResponseWrapperUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Api(tags = "课程相关接口")
@RestController
@RequestMapping(value = "BUAAOJ/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ContestService contestService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private SubmissionService submissionService;

    @ApiOperation("查看所有课程接口")
    @RequestMapping(value = "/getAllCourses", method = RequestMethod.GET)
    public HttpResponseWrapperUtil getAllCourses() {
        List<Course> allCourses = courseService.getAllCourses();
        if( allCourses == null ){
            return new HttpResponseWrapperUtil(null, -1, "failure");
        }
        List<Object> data = new ArrayList<>();
        for (Course course : allCourses) {
            Map<String,Object> metadata = new HashMap<>();
            metadata.put("courseId", course.getCourseId());
            metadata.put("courseName", course.getCourseName());
            data.add(metadata);
        }
        return new HttpResponseWrapperUtil(data);
    }

    @ApiOperation("学生查看课程主页接口")
    @RequestMapping(value = "/getCourseHomepage/{studentId}/{courseId}", method = RequestMethod.GET)
    public HttpResponseWrapperUtil getCourseHomepage(@PathVariable("studentId") long studentId,
                                                     @PathVariable("courseId") int courseId) {
        Course course = courseService.getCourseById(courseId);
        if( course == null ) {
            return new HttpResponseWrapperUtil(null, -1, "failure");
        }
        Map<String,Object> data = new HashMap<>();
        data.put("introduction", course.getIntroduction());
        data.put("coursewareUrl", course.getCoursewareUrl());
        int status = courseService.isStudentJoinCourse(studentId,courseId);
        data.put("isStudentAccept", status);
        return new HttpResponseWrapperUtil(data);
    }

    @ApiOperation("学生申请加入课程接口")
    @RequestMapping(value = "/joinCourse/{studentId}/{courseId}", method = RequestMethod.GET)
    public HttpResponseWrapperUtil joinCourse(@PathVariable("studentId") long studentId,
                                                     @PathVariable("courseId") int courseId) {
        Map<String,Object> map = wrapCourseStudentRelationship2Json(studentId,courseId);
        HttpResponseWrapperUtil httpResponse = (HttpResponseWrapperUtil)map.get("httpResponse");
        if( httpResponse != null ){
            return httpResponse;
        }
        if( (int)map.get("status") >= 0 ){
            return new HttpResponseWrapperUtil(null, -1, "请勿重复提交申请");
        }
        courseService.joinCourse(studentId,courseId);
        return new HttpResponseWrapperUtil(null);
    }

    @ApiOperation("学生查看课程成员接口")
    @RequestMapping(value = "/getCourseMembers/{studentId}/{courseId}", method = RequestMethod.GET)
    public HttpResponseWrapperUtil getCourseMembers(@PathVariable("studentId") long studentId,
                                              @PathVariable("courseId") int courseId) {
        Map<String,Object> map = wrapCourseStudentRelationship2Json(studentId,courseId);
        HttpResponseWrapperUtil httpResponse = (HttpResponseWrapperUtil)map.get("httpResponse");
        if( httpResponse != null ){
            return httpResponse;
        }
        if( (int)map.get("status") != 1 ){
            return new HttpResponseWrapperUtil(null, -1, "请先加入课程");
        }
        List<Object> data = courseService.getCourseMembers(courseId);
        return new HttpResponseWrapperUtil(data);
    }

    /**
     * 将学生与课程的关系封装为Map
     * 为了避免代码冗余
     * @param studentId - 学生ID
     * @param courseId - 课程ID
     * @return
     */
    public Map<String,Object> wrapCourseStudentRelationship2Json(long studentId, int courseId) {
        Map<String,Object> result = new HashMap<>();
        Course course = courseService.getCourseById(courseId);
        Student student = studentService.getStudentById(studentId);
        if( course == null ){
            result.put("httpResponse",new HttpResponseWrapperUtil(null, -1, "课程不存在"));
        } else if( student == null ){
            result.put("httpResponse",new HttpResponseWrapperUtil(null, -1, "学生不存在"));
        } else{
            result.put("httpResponse",null);
        }
        result.put("status",courseService.isStudentJoinCourse(studentId,courseId));
        return result;
    }
}