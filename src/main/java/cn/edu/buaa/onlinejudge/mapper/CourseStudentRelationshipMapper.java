package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.CourseStudentRelationship;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseStudentRelationshipMapper {
    /**
     * 根据学生ID和课程ID获取CourseStudentRelationship
     * @param studentId - 学生ID
     * @param courseId - 课程ID
     * @return CourseStudentRelationship对象
     */
    CourseStudentRelationship getCourseStudentRelationship(long studentId, int courseId);

    /**
     * 插入CourseStudentRelationship对象
     * @param relationship - CourseStudentRelationship对象
     */
    void insertCourseStudentRelationship(CourseStudentRelationship relationship);

    /**
     * 获取课程成员
     * @param courseId - 课程ID
     * @return CourseStudentRelationship对象列表
     */
    List<CourseStudentRelationship> getCourseMembers(int courseId);

    /**
     * 获取学生已参加的课程的课程ID列表
     * @param studentId - 学生ID
     * @return 课程ID列表
     */
    List<Integer> getStudentJoinedCourseIdList(long studentId);
}
