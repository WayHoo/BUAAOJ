package cn.edu.buaa.onlinejudge.controller;

import cn.edu.buaa.onlinejudge.model.Contest;
import cn.edu.buaa.onlinejudge.model.Course;
import cn.edu.buaa.onlinejudge.model.Problem;
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
        List<Contest> contestList = contestService.getPageContests(pageSize,pageIndex);
        List<Map<String,Object>> contests = wrapContests2Json(contestList);
        Map<String,Object> data = new HashMap<>();
        data.put("contests",contests);
        data.put("totalContestsNum",contestService.getTotalContestsNum());
        return new HttpResponseWrapperUtil(data);
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

    @ApiOperation("教师反转竞赛可见性接口")
    @GetMapping(value = "/reverseContestVisibility/{courseId}/{contestId}")
    public HttpResponseWrapperUtil reverseContestVisibility(@PathVariable("courseId") int courseId,
                                                            @PathVariable("contestId") int contestId) {
        Contest contest = contestService.getContestById(contestId);
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

    @ApiOperation("学生进入竞赛接口")
    @GetMapping(value = "/enterContest/{studentId}/{contestId}")
    public HttpResponseWrapperUtil enterContest(@PathVariable("studentId") long studentId,
                                                @PathVariable("contestId") int contestId) {
        Contest contest = contestService.getContestById(contestId);
        if( contest == null || !contest.isVisible() || !contest.isAnswerable() ||
                DateUtil.getCurrentTimestamp().before(contest.getStartTime())){
            return new HttpResponseWrapperUtil(null, -1, "failure");
        }
        Map<String,Object> data = new HashMap<>();
        data.put("introduction",contest.getIntroduction());
        data.put("finishTime",DateUtil.formatTimestamp(contest.getFinishTime()));
        data.put("contestStatus",judgeContestStatus(contest.getStartTime(),contest.getFinishTime()));
        data.put("serverCurrentTime",DateUtil.getCurrentTime());
        List<Problem> problemList = problemService.getProblemsOfContest(contestId);
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
}
