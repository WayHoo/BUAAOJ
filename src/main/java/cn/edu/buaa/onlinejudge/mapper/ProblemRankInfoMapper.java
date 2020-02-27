package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.Problem;
import cn.edu.buaa.onlinejudge.model.ProblemRankInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRankInfoMapper {
    /**
     * 获取题目在竞赛时间范围内的提交人数
     * @param problemId - 题目ID
     * @return 提交人数
     */
    long getProblemSubmitStudents(long problemId);

    /**
     * 获取题目在竞赛时间范围内的通过人数
     * @param problemId - 题目ID
     * @return 通过人数
     */
    long getProblemAcceptStudents(long problemId);

    /**
     * 查询学生对指定题目的排名统计信息
     * @param studentId
     * @param problemId
     * @return
     */
    ProblemRankInfo getProblemRankInfoByStudentIdAndProblemId(@Param("studentId") long studentId,
                                                              @Param("problemId") long problemId);

    /**
     * 插入学生对指定题目的排名统计信息
     * @param problemRankInfo - 排名统计信息对象
     */
    void insertProblemRankInfo(ProblemRankInfo problemRankInfo);

    /**
     * 更新学生对指定题目的排名统计信息
     * @param problemRankInfo - 排名统计信息对象
     */
    void updateProblemRankInfo(ProblemRankInfo problemRankInfo);
}
