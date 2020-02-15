package cn.edu.buaa.onlinejudge.controller;

import cn.edu.buaa.onlinejudge.model.Contest;
import cn.edu.buaa.onlinejudge.model.Course;
import cn.edu.buaa.onlinejudge.model.Problem;
import cn.edu.buaa.onlinejudge.model.Submission;
import cn.edu.buaa.onlinejudge.service.ContestService;
import cn.edu.buaa.onlinejudge.service.CourseService;
import cn.edu.buaa.onlinejudge.service.ProblemService;
import cn.edu.buaa.onlinejudge.service.SubmissionService;
import cn.edu.buaa.onlinejudge.utils.HttpResponseWrapperUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "题目相关接口")
@RestController
@RequestMapping(value = "BUAAOJ/problems")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ContestService contestService;

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private CourseService courseService;

    @ApiOperation("根据题目ID查看题目接口")
    @RequestMapping(value = "/getProblemById/{studentId}/{problemId}", method = RequestMethod.GET)
    public HttpResponseWrapperUtil getProblemById(@PathVariable("studentId") long studentId,
                                                  @PathVariable("problemId") long problemId) {
        Problem problem = problemService.getProblemById(problemId);
        Contest contest = contestService.getContestById(problem.getContestId());
        if( problem == null || contest == null ){
            return new HttpResponseWrapperUtil(null, -1, "failure");
        }
        Submission submission = submissionService.getStudentLatestSubmissionOfProblem(studentId,problemId);
        Map<String,Object> data = new HashMap<>();
        data.put("isAnswerable",contest.isAnswerable() ? 1 : 0);
        data.put("problemName",problem.getProblemName());
        data.put("timeLimit",problem.getTimeLimit());
        data.put("memoryLimit",problem.getMemoryLimit());
        data.put("description",problem.getDescription());
        data.put("inputFormat",problem.getInputFormat());
        data.put("outputFormat",problem.getOutputFormat());
        data.put("inputSample",problem.getInputSample());
        data.put("outputSample",problem.getOutputSample());
        data.put("sampleExplanation",problem.getSampleExplanation());
        data.put("hint",problem.getHint());
        data.put("code",problem.getCode());
        String submitCode = (submission == null) ? null : submission.getSubmitCode();
        data.put("submitCode",submitCode);
        return new HttpResponseWrapperUtil(data);
    }

    @ApiOperation("学生查看题库接口")
    @RequestMapping(value = "/getPageProblems/{pageSize}/{pageIndex}/{studentId}", method = RequestMethod.GET)
    public HttpResponseWrapperUtil getPageProblems(@PathVariable("pageSize") int pageSize,
                                                   @PathVariable("pageIndex") int pageIndex,
                                                   @PathVariable("studentId") long studentId) {
        if( pageSize < 0 || pageIndex < 0 ) {
            return new HttpResponseWrapperUtil(null, -1, "failure");
        }
        List<Integer> courseIdList = courseService.getStudentJoinedCourseIdList(studentId);
        List<Integer> contestIdList = contestService.getContestIdListByCourseIdList(courseIdList);
        Map<String,Object> problemMap = problemService.getPageProblemsByContestIdList(contestIdList,pageSize,pageIndex);
        List<Problem> problemList = (List<Problem>)problemMap.get("problemList");
        Map<String,Object> data = new HashMap<>();
        data.put("totalProblemNum",problemMap.get("totalProblemNum"));
        if( problemList != null ){
            List<Object> problems = new ArrayList<>();
            for (Problem problem : problemList) {
                Map<String,Object> metadata = wrapProblem2Json(problem);
                metadata.put("author",problem.getAuthor());
                Contest contest = contestService.getContestById(problem.getContestId());
                metadata.put("srcContest",contest.getContestName());
                metadata.put("srcContestId",contest.getContestId());
                problems.add(metadata);
            }
            data.put("problems",problems);
        } else {
            data.put("problems",null);
        }
        return new HttpResponseWrapperUtil(data);
    }

    @ApiOperation("学生查看课程题库接口")
    @RequestMapping(value = "/getProblemsOfCourse/{courseId}", method = RequestMethod.GET)
    public HttpResponseWrapperUtil getProblemsOfCourse(@PathVariable("courseId") int courseId) {
        Course course = courseService.getCourseById(courseId);
        if( course == null ){
            return new HttpResponseWrapperUtil(null, -1, "课程不存在");
        }
        List<Contest> contestList = contestService.getContestsOfCourse(courseId);
        if( contestList == null || contestList.size() == 0 ){
            return new HttpResponseWrapperUtil(null);
        }
        List<Integer> contestIdList = new ArrayList<>();
        for (Contest contest : contestList) {
            contestIdList.add(contest.getContestId());
        }
        List<Problem> problemList = problemService.getProblemsByContestIdList(contestIdList);
        List<Object> data = new ArrayList<>();
        for (Problem problem : problemList) {
            Map<String,Object> metadata = wrapProblem2Json(problem);
            data.add(metadata);
        }
        return new HttpResponseWrapperUtil(data);
    }

    /**
     * 封装Problem对象为JSON数据格式
     * @param problem - Problem对象
     * @return - Map对象
     */
    public Map<String,Object> wrapProblem2Json(Problem problem) {
        Map<String,Object> metadata = new HashMap<>();
        metadata.put("problemId",problem.getProblemId());
        metadata.put("problemName",problem.getProblemName());
        metadata.put("acceptStudents",
                submissionService.getProblemAcceptStudents(problem.getProblemId()));
        metadata.put("submitStudents",
                submissionService.getProblemSubmitStudents(problem.getProblemId()));
        metadata.put("submitTimes",
                submissionService.getProblemSubmitTimes(problem.getProblemId()));
        return metadata;
    }
}