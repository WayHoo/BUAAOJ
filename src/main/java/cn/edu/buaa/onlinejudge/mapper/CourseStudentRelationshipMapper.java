package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.CourseStudentRelationship;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseStudentRelationshipMapper {
    CourseStudentRelationship getCourseStudentRelationship(long studentId, int courseId);
    void insertCourseStudentRelationship(CourseStudentRelationship relationship);
    List<CourseStudentRelationship> getCourseMembers(int courseId);
}
