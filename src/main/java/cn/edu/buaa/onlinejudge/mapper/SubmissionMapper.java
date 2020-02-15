package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.Submission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionMapper {
    /**
     * 获取指定题目的提交人数
     * @param problemId - 题目ID
     * @return 提交人数
     */
    long getProblemSubmitStudents(long problemId);

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
    long getProblemAcceptStudents(long problemId);

    /**
     * 获取指定学生对指定题目的最近一次提交记录
     * @param studentId - 学生ID
     * @param problemId - 题目ID
     * @return Submission对象
     */
    Submission getStudentLatestSubmissionOfProblem(@Param("studentId") long studentId, @Param("problemId") long problemId);
}
