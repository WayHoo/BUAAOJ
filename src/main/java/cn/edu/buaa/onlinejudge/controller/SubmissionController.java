package cn.edu.buaa.onlinejudge.controller;

import cn.edu.buaa.onlinejudge.model.Contest;
import cn.edu.buaa.onlinejudge.model.Language;
import cn.edu.buaa.onlinejudge.model.Problem;
import cn.edu.buaa.onlinejudge.model.Submission;
import cn.edu.buaa.onlinejudge.service.ContestService;
import cn.edu.buaa.onlinejudge.service.LanguageService;
import cn.edu.buaa.onlinejudge.service.ProblemService;
import cn.edu.buaa.onlinejudge.service.SubmissionService;
import cn.edu.buaa.onlinejudge.utils.DateUtil;
import cn.edu.buaa.onlinejudge.utils.HttpResponseWrapperUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "提交相关接口")
@RestController
@RequestMapping(value = "BUAAOJ/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ContestService contestService;

    @Autowired
    private LanguageService languageService;

    @ApiOperation("查看学生对指定题目的提交记录接口")
    @GetMapping(value = "/getProblemSubmissionsOfStudent/{studentId}/{problemId}")
    public HttpResponseWrapperUtil getProblemSubmissionsOfStudent(@PathVariable("studentId") long studentId,
                                                         @PathVariable("problemId") long problemId) {
        List<Submission> submissionList = submissionService.getSubmissionsByStudentIdAndProblemId(studentId,problemId);
        List<Object> data = new ArrayList<>();
        if( submissionList != null ) {
            for (Submission submission : submissionList) {
                Map<String,Object> metadata = wrapSubmission2Json(submission);
                data.add(metadata);
            }
        }
        return new HttpResponseWrapperUtil(data);
    }

    @ApiOperation("查看学生对指定竞赛的提交记录接口")
    @GetMapping(value = "/getContestSubmissionsOfStudent/{studentId}/{contestId}")
    public HttpResponseWrapperUtil getContestSubmissionsOfStudent(@PathVariable("studentId") long studentId,
                                                                  @PathVariable("contestId") int contestId) {
        List<Long> problemIdList = problemService.getProblemIdListOfContest(contestId);
        List<Submission> submissionList = submissionService.getSubmissionsByStudentIdAndProblemIdList(studentId,problemIdList);
        List<Object> data = new ArrayList<>();
        if( submissionList != null ) {
            for (Submission submission : submissionList) {
                Map<String,Object> metadata = wrapSubmission2Json(submission);
                Problem problem = problemService.getProblemById(submission.getProblemId());
                metadata.put("problemName",problem.getProblemName());
                metadata.put("problemNumber",problem.getProblemNumber());
                Language language = languageService.getLanguageById(submission.getLanguageId());
                metadata.put("language",language.getLanguageName());
                Float codeLength = 0.0f;
                try {
                    codeLength = submission.getSubmitCode().getBytes("utf-8").length / 1024.0f;
                } catch (UnsupportedEncodingException e) {
                    codeLength = null;
                    e.printStackTrace();
                } finally {
                    metadata.put("codeLength",String.format("%.1f",codeLength));
                }
                metadata.put("submitTime",DateUtil.formatTimestamp(submission.getSubmitTime()));
                data.add(metadata);
            }
        }
        return new HttpResponseWrapperUtil(data);
    }

    @ApiOperation("学生提交代码接口")
    @PostMapping(value = "/submitCode")
    public HttpResponseWrapperUtil submitCode(@RequestBody Submission submission) {
        submission.setSubmitTime(DateUtil.getCurrentTimestamp());
        submissionService.insertSubmission(submission);
        if( submission.getSubmissionId() > 0 ){
            Map<String,Object> data = wrapSubmission2Json(submission);
            return new HttpResponseWrapperUtil(data);
        }else{
            return new HttpResponseWrapperUtil(null, -1, "failure");
        }
    }

    /**
     * 将基本的提交记录信息封装成JSON数据格式
     * @param submission - 提交记录对象
     * @return JSON数据
     */
    public Map<String,Object> wrapSubmission2Json(Submission submission) {
        Map<String,Object> data = new HashMap<>();
        data.put("submissionId",submission.getSubmissionId());
        data.put("judgeResult",submission.getJudgeResult());
        data.put("judgeScore",submission.getJudgeScore());
        data.put("usedTime",submission.getUsedTime());
        data.put("usedMemory",submission.getUsedMemory());
        return data;
    }
}
