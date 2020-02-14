package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.ProblemMapper;
import cn.edu.buaa.onlinejudge.model.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemService {
    @Autowired
    private ProblemMapper problemMapper;

    public List<Problem> getProblemsByContestIdList(List<Integer> contestIdList) {
        return problemMapper.getProblemsByContestIdList(contestIdList);
    }

    public List<Problem> getProblemsOfContest(int contestId){
        return problemMapper.getProblemsOfContest(contestId);
    }
}
