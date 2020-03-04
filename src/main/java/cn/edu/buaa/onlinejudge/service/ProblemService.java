package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.InputOutputSampleMapper;
import cn.edu.buaa.onlinejudge.mapper.ProblemMapper;
import cn.edu.buaa.onlinejudge.model.InputOutputSample;
import cn.edu.buaa.onlinejudge.model.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ProblemService {
    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private InputOutputSampleMapper inputOutputSampleMapper;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ContestService contestService;

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

    public List<Problem> getVisibleProblemsByContestIdList(List<Integer> contestIdList){
        return problemMapper.getVisibleProblemsByContestIdList(contestIdList);
    }

    /**
     * 根据题目ID查询题目，不查询题目的输入输出样例
     * @param problemId - 题目ID
     * @return
     */
    public Problem getBasicProblemById(long problemId){
        return problemMapper.getProblemById(problemId);
    }

    /**
     * 获取学生个人题库，查询的题目中不包含输入输出样例
     * 题库仅展示该学生已加入课程中的所有题目
     * @param studentId - 学生ID
     * @return 题目对象列表
     */
    public List<Problem> getPersonalProblemLibrary(long studentId){
        List<Integer> courseIdList = courseService.getStudentJoinedCourseIdList(studentId);
        List<Integer> contestIdList = contestService.getVisibleContestIdListByCourseIdList(courseIdList);
        return problemMapper.getVisibleProblemsByContestIdList(contestIdList);
    }

    public Map<String,Object> getPageProblemsFromPersonalLibrary(long studentId, int pageSize, int pageIndex) {
        Map<String,Object> map = new HashMap<>();
        List<Problem> problemList = getPersonalProblemLibrary(studentId);
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

    /**
     * 在个人题库（已加入课程中的所有题目）中根据题目名称模糊查询
     * @param studentId - 学生ID
     * @param problemName - 题目名称
     * @return
     */
    public List<Problem> fuzzySearchProblemsByNameFromPersonalLibrary(long studentId, String problemName){
        //校验前端所传数据是否无效
        if( problemName == null || problemName.length() == 0 ){
            return null;
        }
        List<Problem> problemList = getPersonalProblemLibrary(studentId);
        if( problemList == null || problemList.size() == 0 ){
            return null;
        }
        List<Problem> data = new ArrayList<>();
        for (Problem problem : problemList) {
            if( problem.getProblemName().length() >= problemName.length() &&
                problem.getProblemName().substring(0, problemName.length()).equals(problemName) ){
                data.add(problem);
            }
        }
        return data;
    }

    public List<Problem> fuzzySearchProblemsByAuthorFromPersonalLibrary(long studentId, String author){
        //校验前端所传数据是否无效
        if( author == null || author.length() == 0 ){
            return null;
        }
        List<Problem> problemList = getPersonalProblemLibrary(studentId);
        if( problemList == null || problemList.size() == 0 ){
            return null;
        }
        List<Problem> data = new ArrayList<>();
        for (Problem problem : problemList) {
            if( problem.getAuthor().length() >= author.length() &&
                    problem.getAuthor().substring(0, author.length()).equals(author) ){
                data.add(problem);
            }
        }
        return data;
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

    /**
     * 反转题目的可见性
     * @param problemId - 题目ID
     */
    public void reverseProblemVisibility(long problemId){
        int visibility = problemMapper.getProblemById(problemId).isVisible() ? 1 : 0;
        problemMapper.setProblemVisibility(problemId, 1 - visibility);
    }
}
