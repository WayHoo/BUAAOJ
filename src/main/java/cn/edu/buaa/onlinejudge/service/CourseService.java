package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.CourseMapper;
import cn.edu.buaa.onlinejudge.mapper.CourseStudentRelationshipMapper;
import cn.edu.buaa.onlinejudge.mapper.StudentMapper;
import cn.edu.buaa.onlinejudge.model.Course;
import cn.edu.buaa.onlinejudge.model.CourseStudentRelationship;
import cn.edu.buaa.onlinejudge.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Object> getCourseMembers(int courseId) {
        List<Object> memberList = new ArrayList<>();
        List<CourseStudentRelationship> relationshipList =
                courseStudentRelationshipMapper.getCourseMembers(courseId);
        for (CourseStudentRelationship relationship : relationshipList) {
            Map<String,Object> metadata = new HashMap<>();
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
}