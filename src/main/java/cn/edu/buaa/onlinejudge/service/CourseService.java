package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.CourseMapper;
import cn.edu.buaa.onlinejudge.mapper.CourseStudentRelationshipMapper;
import cn.edu.buaa.onlinejudge.mapper.StudentMapper;
import cn.edu.buaa.onlinejudge.model.Course;
import cn.edu.buaa.onlinejudge.model.CourseStudentRelationship;
import cn.edu.buaa.onlinejudge.model.Student;
import cn.edu.buaa.onlinejudge.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseService {
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseStudentRelationshipMapper courseStudentRelationshipMapper;

    @Autowired
    private StudentMapper studentMapper;

    public List<Course> getAllCourses() {
        return courseMapper.getAllCourses();
    }

    public List<Course> getCoursesOfTeacher(long teacherId){
        return courseMapper.getAllCoursesByTeacherId(teacherId);
    }

    public Course getCourseById(int courseId) {
        return courseMapper.getCourseById(courseId);
    }

    /**
     * 学生是否提交加入课程申请并通过
     * @param studentId - 学生ID
     * @param courseId - 课程ID
     * @return 0 - 申请尚未通过，1 - 已通过，-1 - 未提交申请
     */
    public int isStudentJoinCourse(long studentId, int courseId) {
        CourseStudentRelationship relationship =
                courseStudentRelationshipMapper.getCourseStudentRelationship(studentId,courseId);
        if (relationship == null ){
            return -1;
        } else if ( relationship.isStudentAccept() ){
            return 1;
        } else
            return 0;
    }

    public void joinCourse(long studentId, int courseId) {
        CourseStudentRelationship relationship =
                courseStudentRelationshipMapper.getCourseStudentRelationship(studentId,courseId);
        if (relationship != null ) {
            return ;
        }
        CourseStudentRelationship newRelationship = new CourseStudentRelationship(courseId,studentId);
        courseStudentRelationshipMapper.insertCourseStudentRelationship(newRelationship);
    }

    /**
     * 获取课程审核通过或未通过的成员
     * @param courseId - 课程ID
     * @param tag - 选择标签，tag = 0表示获取审核未通过的成员，tag = 1表示获取审核通过的成员
     * @return
     */
    public List<Map<String,Object>> getCourseMembers(int courseId, int tag) {
        if( tag != 0 && tag != 1 ){
            return null;
        }
        List<Map<String,Object>> memberList = new ArrayList<>();
        List<CourseStudentRelationship> relationshipList =
                courseStudentRelationshipMapper.getCourseMembers(courseId,tag);
        for (CourseStudentRelationship relationship : relationshipList) {
            Map<String,Object> metadata = new HashMap<>();
            metadata.put("studentId",relationship.getStudentId());
            metadata.put("studentRole",relationship.getStudentRole());
            Student student = studentMapper.getStudentById(relationship.getStudentId());
            metadata.put("studentName",student.getUserName());
            metadata.put("studentNumber",student.getUserNumber());
            memberList.add(metadata);
        }
        return memberList;
    }

    public List<Integer> getStudentJoinedCourseIdList(long studentId) {
        return courseStudentRelationshipMapper.getStudentJoinedCourseIdList(studentId);
    }

    public void insertCourse(Course course){
        courseMapper.insertCourse(course);
    }

    public void updateCourse(Course course){
        courseMapper.updateCourse(course);
    }

    /**
     * 判断课程是否有课件供下载
     * @param courseId - 课程ID
     * @return
     */
    public boolean isCoursewareExists(int courseId){
        String filePath = FileUtil.getUploadPath(Integer.toString(courseId));
        File file = new File(filePath + "courseware.zip");
        return file.exists();
    }

    public void updateCourseMemberRole(int courseId, long studentId, int role){
        courseStudentRelationshipMapper.updateCourseMemberRole(courseId, studentId, role);
    }

    public void auditStudent(int courseId, long studentId, int isStudentAccept){
        courseStudentRelationshipMapper.auditStudent(courseId, studentId, isStudentAccept);
    }

    public void deleteCourse(int courseId){
        courseMapper.deleteCourse(courseId);
    }
}