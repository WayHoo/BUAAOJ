package cn.edu.buaa.onlinejudge.controller;

import cn.edu.buaa.onlinejudge.model.*;
import cn.edu.buaa.onlinejudge.service.ContestService;
import cn.edu.buaa.onlinejudge.service.CourseService;
import cn.edu.buaa.onlinejudge.service.ProblemService;
import cn.edu.buaa.onlinejudge.service.SubmissionService;
import cn.edu.buaa.onlinejudge.utils.DateUtil;
import cn.edu.buaa.onlinejudge.utils.HttpResponseWrapperUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "竞赛相关接口")
@RestController
@RequestMapping(value = "BUAAOJ/contests")
public class ContestController {

    @Autowired
    private ContestService contestService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private SubmissionService submissionService;

    @ApiOperation("学生查看课程竞赛接口")
    @GetMapping(value = "/getVisibleContestsOfCourse/{courseId}")
    public HttpResponseWrapperUtil getVisibleContestsOfCourse(@PathVariable("courseId") int courseId) {
        Course course = courseService.getCourseById(courseId);
        if( course == null ){
            return new HttpResponseWrapperUtil(null, -1, "课程不存在");
        }
        List<Contest> contestList = contestService.getVisibleContestsOfCourse(courseId);
        List<Map<String,Object>> data = wrapContests2Json(contestList);
        return new HttpResponseWrapperUtil(data);
    }

    @ApiOperation("学生通过分页查看所有竞赛接口")
    @GetMapping(value = "/getPageContests/{pageSize}/{pageIndex}")
    public HttpResponseWrapperUtil getPageContests(@PathVariable("pageSize") int pageSize,
                                                   @PathVariable("pageIndex") int pageIndex) {
        if( pageSize < 0 || pageIndex < 0 ) {
            return new HttpResponseWrapperUtil(null, -1, "failure");
        }
        List<Contest> contestList = contestService.getPageVisibleContests(pageSize,pageIndex);
        List<Map<String,Object>> contests = wrapContests2Json(contestList);
        Map<String,Object> data = new HashMap<>();
        data.put("contests",contests);
        data.put("totalContestsNum",contestService.getTotalContestsNum());
        return new HttpResponseWrapperUtil(data);
    }

    @ApiOperation("学生根据竞赛名称模糊查找竞赛接口")
    @PostMapping(value = "/fuzzyQueryContestsByName")
    public HttpResponseWrapperUtil fuzzyQueryContestsByName(@RequestParam("contestName") String contestName) {

        List<Contest> contestList = contestService.fuzzyQueryVisibleContestsByName(contestName);
        if( contestList == null || contestList.size() == 0 ){
            return new HttpResponseWrapperUtil(null, -1, "未查询到相关数据");
        }
        List<Map<String,Object>> data = wrapContests2Json(contestList);
        return new HttpResponseWrapperUtil(data);
    }

    @ApiOperation("学生进入竞赛接口")
    @GetMapping(value = "/enterContest/{studentId}/{contestId}")
    public HttpResponseWrapperUtil enterContest(@PathVariable("studentId") long studentId,
                                                @PathVariable("contestId") int contestId) {
        Contest contest = contestService.getContestById(contestId);
        if( contest == null ){
            return new HttpResponseWrapperUtil(null, -1, "竞赛不存在");
        }
        if( !contest.isVisible() ){
            return new HttpResponseWrapperUtil(null, -1, "竞赛不可见");
        }
        if( DateUtil.getCurrentTimestamp().before(contest.getStartTime()) ){
            return new HttpResponseWrapperUtil(null, -1, "竞赛尚未开始");
        }
        Map<String,Object> data = new HashMap<>();
        data.put("introduction",contest.getIntroduction());
        data.put("finishTime",DateUtil.formatTimestamp(contest.getFinishTime()));
        data.put("contestStatus",judgeContestStatus(contest.getStartTime(),contest.getFinishTime()));
        data.put("isAnswerable", contest.isAnswerable() ? 1 : 0);
        data.put("serverCurrentTime",DateUtil.getCurrentTime());
        List<Problem> problemList = problemService.getVisibleProblemsOfContest(contestId);
        List<Object> problemIdList = new ArrayList<>();
        for (Problem problem : problemList) {
            Map<String,Object> metadata = new HashMap<>();
            metadata.put("problemId",problem.getProblemId());
            metadata.put("status",submissionService.getSubmissionStatus(studentId,problem.getProblemId()));
            problemIdList.add(metadata);
        }
        data.put("problemIdList",problemIdList);
        return new HttpResponseWrapperUtil(data);
    }

    @ApiOperation("学生查看竞赛排名接口")
    @GetMapping(value = "/getContestPageRanks/{pageSize}/{pageIndex}/{contestId}")
    public HttpResponseWrapperUtil getContestPageRanks(@PathVariable("pageSize") int pageSize,
                                                       @PathVariable("pageIndex") int pageIndex,
                                                       @PathVariable("contestId") int contestId) {
        if( pageSize < 0 || pageIndex < 0 ) {
            return new HttpResponseWrapperUtil(null, -1, "分页参数错误");
        }
        List<ContestRank> contestRankList = contestService.getContestPageRanks(pageSize, pageIndex, contestId);
        return wrapContestRankInfo2Json(contestId, contestRankList);
    }

    @ApiOperation("学生根据姓名查看竞赛排名接口")
    @PostMapping(value = "/getContestRankByStudentName")
    public HttpResponseWrapperUtil getContestRankByStudentName(@RequestParam("contestId") int contestId,
                                                               @RequestParam("studentName") String studentName) {

        List<ContestRank> contestRankList = contestService.getContestRankByStudentName(contestId, studentName);
        if( contestRankList == null || contestRankList.size() == 0 ){
            return new HttpResponseWrapperUtil(null, -1, "未查询到相关信息");
        }
        return wrapContestRankInfo2Json(contestId, contestRankList);
    }

    @ApiOperation("教师查看课程的所有竞赛接口")
    @GetMapping(value = "/getAllContestsOfCourse/{teacherId}/{courseId}")
    public HttpResponseWrapperUtil getAllContestsOfCourse(@PathVariable("teacherId") long teacherId,
                                                          @PathVariable("courseId") int courseId) {
        Course course = courseService.getCourseById(courseId);
        if( course == null ){
            return new HttpResponseWrapperUtil(null, -1, "课程不存在");
        }
        if( course.getTeacherId() != teacherId ){
            return new HttpResponseWrapperUtil(null, -1, "权限不足");
        }
        List<Contest> contestList = contestService.getAllContestsOfCourse(courseId);
        List<Map<String,Object>> contests = wrapContests2Json(contestList);
        for (int i = 0; i < contestList.size(); i++) {
            contests.get(i).put("isVisible",contestList.get(i).isVisible() ? 1 : 0);
        }
        return new HttpResponseWrapperUtil(contests);
    }

    @ApiOperation("教师新建竞赛接口")
    @PostMapping(value = "/createContest")
    public HttpResponseWrapperUtil createContest(@RequestBody Contest contest) {
        Course course = courseService.getCourseById(contest.getCourseId());
        if( course == null ){
            return new HttpResponseWrapperUtil(null, -1, "课程不存在");
        }
        contestService.insertContest(contest);
        if( contest.getContestId() == 0 ){
            return new HttpResponseWrapperUtil(null, -1, "新建课程失败");
        }
        Map<String,Object> data = new HashMap<>();
        data.put("contestId", contest.getContestId());
        return new HttpResponseWrapperUtil(data);
    }

    @ApiOperation("教师删除竞赛接口")
    @GetMapping(value = "/deleteContest/{courseId}/{contestId}")
    public HttpResponseWrapperUtil deleteContest(@PathVariable("courseId") int courseId,
                                                 @PathVariable("contestId") int contestId) {
        Contest contest = contestService.getContestById(contestId);
        if( contest == null ){
            return new HttpResponseWrapperUtil(null, -1, "竞赛不存在");
        }
        if( contest.getCourseId() != courseId ){
            return new HttpResponseWrapperUtil(null, -1, "权限不足");
        }
        contestService.deleteContest(contestId);
        return new HttpResponseWrapperUtil(null);
    }

    @ApiOperation("教师反转竞赛可见性接口")
    @GetMapping(value = "/reverseContestVisibility/{courseId}/{contestId}")
    public HttpResponseWrapperUtil reverseContestVisibility(@PathVariable("courseId") int courseId,
                                                            @PathVariable("contestId") int contestId) {
        Contest contest = contestService.getContestById(contestId);
        if( contest == null ){
            return new HttpResponseWrapperUtil(null, -1, "竞赛不存在");
        }
        if( contest.getCourseId() != courseId ){
            return new HttpResponseWrapperUtil(null, -1, "权限不足");
        }
        contestService.reverseContestVisibility(contestId);
        return new HttpResponseWrapperUtil(null);
    }

    @ApiOperation("教师反转竞赛答题状态接口")
    @GetMapping(value = "/reverseContestStatus/{courseId}/{contestId}")
    public HttpResponseWrapperUtil reverseContestStatus(@PathVariable("courseId") int courseId,
                                                        @PathVariable("contestId") int contestId) {
        Contest contest = contestService.getContestById(contestId);
        if( contest == null ){
            return new HttpResponseWrapperUtil(null, -1, "该竞赛不存在");
        }
        if( contest.getCourseId() != courseId ){
            return new HttpResponseWrapperUtil(null, -1, "权限不足");
        }
        Timestamp currentTime = DateUtil.getCurrentTimestamp();
        if( currentTime.after(contest.getFinishTime()) ){
            contestService.reverseContestStatus(contestId);
            return new HttpResponseWrapperUtil(null);
        } else{
            return new HttpResponseWrapperUtil(null, -1, "当前竞赛状态不可更改");
        }
    }

    @ApiOperation("教师查看竞赛接口")
    @GetMapping(value = "/getContestById/{contestId}")
    public HttpResponseWrapperUtil getContestById(@PathVariable("contestId") int contestId) {
        Contest contest = contestService.getContestById(contestId);
        if( contest == null ){
            return new HttpResponseWrapperUtil(null, -1, "该竞赛不存在");
        }
        Map<String,Object> data = new HashMap<>();
        data.put("contestName", contest.getContestName());
        data.put("introduction",contest.getIntroduction());
        data.put("startTime",DateUtil.formatTimestamp(contest.getStartTime()));
        data.put("finishTime",DateUtil.formatTimestamp(contest.getFinishTime()));
        return new HttpResponseWrapperUtil(data);
    }

    @ApiOperation("教师修改竞赛信息接口")
    @PostMapping(value = "/updateContest")
    public HttpResponseWrapperUtil updateContest(@RequestBody Contest contest) {
        Contest realContest = contestService.getContestById(contest.getContestId());
        if( contest == null ){
            return new HttpResponseWrapperUtil(null, -1, "该竞赛不存在");
        }
        if( realContest.getCourseId() != contest.getCourseId() ){
            return new HttpResponseWrapperUtil(null, -1, "权限不足");
        }
        contestService.updateContest(contest);
        return new HttpResponseWrapperUtil(null);
    }

    /**
     * 将竞赛列表封装成JSON数据
     * @param contestList - 竞赛列表
     * @return
     */
    public List<Map<String,Object>> wrapContests2Json(List<Contest> contestList) {
        List<Map<String,Object>> contests = new ArrayList<>();
        for (Contest contest : contestList) {
            Map<String,Object> metadata = new HashMap<>();
            metadata.put("contestId",contest.getContestId());
            metadata.put("contestName",contest.getContestName());
            int contestStatus = judgeContestStatus(contest.getStartTime(),contest.getFinishTime());
            metadata.put("contestStatus",contestStatus);
            metadata.put("isAnswerable",contest.isAnswerable() ? 1 : 0);
            metadata.put("startTime", DateUtil.formatTimestamp(contest.getStartTime()));
            metadata.put("finishTime",DateUtil.formatTimestamp(contest.getFinishTime()));
            contests.add(metadata);
        }
        return contests;
    }

    /**
     * 判断比赛进行状态，0 - 未开启，1 - 进行中，2 - 已结束
     * @param startTime - 竞赛开始时间
     * @param finishTime - 竞赛结束时间
     * @return 竞赛进行状态值
     */
    public int judgeContestStatus(Timestamp startTime, Timestamp finishTime) {
        int contestStatus = 0;
        Timestamp currentTimestamp = DateUtil.getCurrentTimestamp();
        if( currentTimestamp.after(startTime) && currentTimestamp.before(finishTime) ) {
            contestStatus = 1;
        } else if( currentTimestamp.after(finishTime) ){
            contestStatus = 2;
        }
        return contestStatus;
    }

    /**
     * 将竞赛排名信息封装成JSON数据
     * @param contestId - 竞赛ID
     * @param contestRankList - 竞赛排名对象列表
     * @return JSON数据
     */
    public HttpResponseWrapperUtil wrapContestRankInfo2Json(int contestId, List<ContestRank> contestRankList){
        List<Problem> problemList = problemService.getVisibleProblemsOfContest(contestId);
        if( problemList == null || problemList.size() == 0 ){
            return new HttpResponseWrapperUtil(null, -1, "该竞赛无排名信息");
        }
        if( contestRankList == null || contestRankList.size() == 0 ){
            return new HttpResponseWrapperUtil(null, -1, "未查询到相关信息");
        }
        Map<String,Object> data = new HashMap<>();
        List<Object> acceptRate = new ArrayList<>();
        for (Problem problem : problemList) {
            Map<String,Object> metadata = new HashMap<>();
            metadata.put("problemNumber", problem.getProblemNumber());
            metadata.put("acceptStudents", submissionService.getProblemAcceptStudents(problem.getProblemId()));
            metadata.put("submitStudents", submissionService.getProblemSubmitStudents(problem.getProblemId()));
            acceptRate.add(metadata);
        }
        data.put("acceptRate", acceptRate);
        List<Object> ranks = new ArrayList<>();
        for (ContestRank contestRank : contestRankList) {
            Map<String,Object> metadata = new HashMap<>();
            metadata.put("rankNum", contestRank.getRankNum());
            metadata.put("studentName", contestRank.getStudentName());
            metadata.put("studentNumber", contestRank.getStudentNumber());
            metadata.put("score", contestRank.getScore());
            metadata.put("timePenalty", contestRank.getWrongSubmitTimes() * ContestRank.TIME_PENALTY);
            List<Object> submitInfo = new ArrayList<>();
            for (Problem problem : problemList) {
                Map<String,Object> submitInfoMetadata = new HashMap<>();
                ProblemRankInfo problemRankInfo =
                        submissionService.getProblemRankInfo(contestRank.getStudentId(), problem.getProblemId());
                submitInfoMetadata.put("problemNumer", problem.getProblemNumber());
                int status = 0;//未提交
                Timestamp submitTime = null;
                int wrongSubmitTimes = 0;
                if( problemRankInfo != null ){
                    if( "AC".equals(problemRankInfo.getJudgeResult()) ){
                        status = 2;//完全正确
                    } else if( problemRankInfo.getScore() > 0 ){
                        status = 1;//部分正确
                    } else{
                        status = -1;//错误
                    }
                    submitTime = problemRankInfo.getSubmitTime();
                    wrongSubmitTimes = problemRankInfo.getWrongSubmitTimes();
                }
                submitInfoMetadata.put("status", status);
                submitInfoMetadata.put("submitTime", DateUtil.formatTimestamp(submitTime));
                submitInfoMetadata.put("wrongSubmitTimes", wrongSubmitTimes);
                submitInfo.add(submitInfoMetadata);
            }
            metadata.put("submitInfo", submitInfo);
            ranks.add(metadata);
        }
        data.put("ranks", ranks);
        return new HttpResponseWrapperUtil(data);
    }
}
