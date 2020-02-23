package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.ProblemMapper;
import cn.edu.buaa.onlinejudge.model.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProblemService {
    @Autowired
    private ProblemMapper problemMapper;

    public Problem getProblemById(long problemId){
        return problemMapper.getProblemById(problemId);
    }

    public List<Problem> getProblemsByContestIdList(List<Integer> contestIdList) {
        return problemMapper.getProblemsByContestIdList(contestIdList);
    }

    public Map<String,Object> getPageProblemsByContestIdList(List<Integer> contestIdList,
                                                        int pageSize, int pageIndex) {
        Map<String,Object> map = new HashMap<>();
        List<Problem> problemList = getProblemsByContestIdList(contestIdList);
        map.put("totalProblemNum",problemList.size());
        if( pageSize * pageIndex >= problemList.size() ){
            map.put("problemList",null);
        } else{
            int toIndex = (pageIndex + 1) * pageSize;
            toIndex = toIndex > problemList.size() ? problemList.size() : toIndex;
            map.put("problemList",problemList.subList(pageSize * pageIndex, toIndex));
        }
        return map;
    }

    public List<Problem> getProblemsOfContest(int contestId){
        return problemMapper.getProblemsOfContest(contestId);
    }

    public List<Long> getProblemIdListOfContest(int contestId){
        return problemMapper.getProblemIdListOfContest(contestId);
    }

    public void insertProblem(Problem problem){
        problemMapper.insertProblem(problem);
    }

    public void deleteProblem(long problemId){
        problemMapper.deleteProblem(problemId);
    }
}
