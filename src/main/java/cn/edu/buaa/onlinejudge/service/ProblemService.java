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
        if( problem == null ){
            return null;
        }
        List<InputOutputSample> sampleList = inputOutputSampleMapper.getInputOutputSamplesOfProblem(problemId);
        problem.setInputOutputSamples(sampleList);
        return problem;
    }

    /**
     * 获取多个竞赛下的所有可见题目，查询的题目中不包含输入输出样例
     * @param contestIdList - 竞赛ID列表
     * @return Problem对象列表
     */
    public List<Problem> getVisibleProblemsByContestIdList(List<Integer> contestIdList) {
        return problemMapper.getVisibleProblemsByContestIdList(contestIdList);

    }

    public Map<String,Object> getPageVisibleProblemsByContestIdList(List<Integer> contestIdList,
                                                                    int pageSize, int pageIndex) {
        Map<String,Object> map = new HashMap<>();
        List<Problem> problemList = getVisibleProblemsByContestIdList(contestIdList);
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

    public List<Problem> getVisibleProblemsOfContest(int contestId){
        return problemMapper.getVisibleProblemsOfContest(contestId);
    }

    public List<Problem> getAllProblemsOfContest(int contestId){
        return problemMapper.getAllProblemsOfContest(contestId);
    }

    public void insertProblem(Problem problem){
        problemMapper.insertProblem(problem);
        insertInputOutputSamplesOfProblem(problem);
    }

    public void updateProblem(Problem problem){
        problemMapper.updateProblem(problem);
        //更新题目输入输出样例通过“先删除再插入”实现
        inputOutputSampleMapper.deleteInputOutputSamplesOfProblem(problem.getProblemId());
        insertInputOutputSamplesOfProblem(problem);
    }

    public void insertInputOutputSamplesOfProblem(Problem problem){
        List<InputOutputSample> inputOutputSamples = problem.getInputOutputSamples();
        if( inputOutputSamples != null ){
            int i = 1;
            for (InputOutputSample inputOutputSample : inputOutputSamples) {
                inputOutputSample.setProblemId(problem.getProblemId());
                inputOutputSample.setSampleNumber(i++);
                inputOutputSampleMapper.insertInputOutputSample(inputOutputSample);
            }
        }
    }

    /**
     * 删除题目
     * 题目输入输出样例与题目存储在不同数据表中，但由于两表使用外键连接，
     * 并设置了`ON DELETE CASCADE ON UPDATE CASCADE`，因此删除题目数据表中的题目后，
     * 输入输出数据表中的相关数据自动删除
     * @param problemId
     */
    public void deleteProblem(long problemId){
        problemMapper.deleteProblem(problemId);
    }
}
