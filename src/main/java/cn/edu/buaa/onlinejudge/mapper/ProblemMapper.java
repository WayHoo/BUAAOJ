package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.Problem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemMapper {
    List<Problem> getProblemsByContestIdList(List<Integer> contestIdlist);
    List<Problem> getProblemsOfContest(int contestId);
}
