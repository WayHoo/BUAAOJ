package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.Submission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionMapper {

    /**
     * 获取指定题目的提交总次数
     * @param problemId - 题目ID
     * @return 提交总次数
     */
    long getProblemSubmitTimes(long problemId);

    /**
     * 获取指定题目的通过人数
     * @param problemId - 题目ID
     * @return 通过人数
     */
//    long getProblemAcceptStudents(long problemId);

    /**
     * 根据ID查询提交记录
     * @param submissionId - 提交记录ID
     * @return 提交记录对象
     */
    Submission getSubmissionById(long submissionId);

    /**
     * 获取指定学生对指定题目的最近一次提交记录
     * @param studentId - 学生ID
     * @param problemId - 题目ID
     * @return Submission对象
     */
    Submission getStudentLatestSubmissionOfProblem(@Param("studentId") long studentId, @Param("problemId") long problemId);

    /**
     * 查看指定学生对指定题目的所有提交记录，结果按照提交记录的时间先后顺序排序
     * @param studentId - 学生ID
     * @param problemId - 题目ID
     * @return Submission对象列表
     */
    List<Submission> getSubmissionsByStudentIdAndProblemId(@Param("studentId") long studentId,
                                                           @Param("problemId") long problemId);

    /**
     * 查看学生对竞赛的所有提交记录，结果按照提交时间先后顺序排序
     * @param studentId - 学生ID
     * @param contestId - 竞赛ID
     * @return Submission对象列表
     */
    List<Submission> getSubmissionsByStudentIdAndContestId(@Param("studentId") long studentId,
                                                           @Param("contestId") int contestId);

    /**
     * 插入提交记录
     * @param submission - 提交记录对象
     */
    void insertSubmission(Submission submission);

    /**
     * 更新提交记录
     * @param submission - 提交记录对象
     */
    void updateSubmission(Submission submission);
}
