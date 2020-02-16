package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.Problem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemMapper {
    /**
     * 根据题目ID获取题目对象
     * @param problemId - 题目ID
     * @return Problem对象
     */
    Problem getProblemById(long problemId);

    /**
     * 获取多个竞赛下的所有题目
     * @param contestIdlist - 竞赛ID列表
     * @return Problem列表
     */
    List<Problem> getProblemsByContestIdList(List<Integer> contestIdlist);

    /**
     * 获取指定竞赛下的所有题目
     * @param contestId - 竞赛ID
     * @return Problem列表
     */
    List<Problem> getProblemsOfContest(int contestId);

    /**
     * 获取指定竞赛下的所有题目ID
     * @param contestId - 竞赛ID
     * @return 题目ID列表
     */
    List<Long> getProblemIdListOfContest(int contestId);
}
