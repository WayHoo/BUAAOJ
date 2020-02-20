package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.CourseStudentRelationship;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

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
     * 获取课程审核通过或未通过的成员
     * @param courseId - 课程ID
     * @param tag - 选择标签，tag = 0表示获取审核未通过的成员，tag = 1表示获取审核通过的成员
     * @return CourseStudentRelationship对象列表
     */
    List<CourseStudentRelationship> getCourseMembers(@Param("courseId") int courseId,
                                                     @Param("tag") int tag);

    /**
     * 获取学生已参加的课程的课程ID列表
     * @param studentId - 学生ID
     * @return 课程ID列表
     */
    List<Integer> getStudentJoinedCourseIdList(long studentId);

    /**
     * 改变学生在课程中的角色
     * @param courseId - 课程ID
     * @param studentId - 学生ID
     * @param role - 学生角色值
     */
    void updateCourseMemberRole(@Param("courseId") int courseId,
                                @Param("studentId") long studentId,
                                @Param("role") int role);

    /**
     * 审核学生的加入课程申请（接受1，拒绝0）
     * @param courseId - 课程ID
     * @param studentId - 学生ID
     * @param isStudentAccept - 是否接受学生的申请
     */
    void auditStudent(@Param("courseId") int courseId,
                       @Param("studentId") long studentId,
                       @Param("isStudentAccept") int isStudentAccept);
}
