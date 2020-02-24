package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.InputOutputSampleMapper;
import cn.edu.buaa.onlinejudge.mapper.ProblemMapper;
import cn.edu.buaa.onlinejudge.model.InputOutputSample;
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

    @Autowired
    private InputOutputSampleMapper inputOutputSampleMapper;

    /**
     * 根据题目ID查询题目
     * 由于题目的基本信息与输入输出样例存储在不同数据表中，因此需要查表两次
     * @param problemId
     * @return
     */
    public Problem getProblemById(long problemId){
        Problem problem = problemMapper.getProblemById(problemId);
        List<InputOutputSample> sampleList = inputOutputSampleMapper.getInputOutputSamplesOfProblem(problemId);
        problem.setInputOutputSamples(sampleList);
        return problem;
    }

    /**
     * 获取多个竞赛下的所有题目，查询的题目中不包含输入输出样例
     * @param contestIdList - 竞赛ID列表
     * @return Problem对象列表
     */
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
