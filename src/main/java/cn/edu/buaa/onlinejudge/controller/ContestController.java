package cn.edu.buaa.onlinejudge.controller;

import cn.edu.buaa.onlinejudge.model.Contest;
import cn.edu.buaa.onlinejudge.model.Course;
import cn.edu.buaa.onlinejudge.model.Problem;
import cn.edu.buaa.onlinejudge.service.ContestService;
import cn.edu.buaa.onlinejudge.service.CourseService;
import cn.edu.buaa.onlinejudge.service.ProblemService;
import cn.edu.buaa.onlinejudge.utils.DateUtil;
import cn.edu.buaa.onlinejudge.utils.HttpResponseWrapperUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("学生查看课程竞赛接口")
    @RequestMapping(value = "/getContestsOfCourse/{courseId}", method = RequestMethod.GET)
    public HttpResponseWrapperUtil getContestsOfCourse(@PathVariable("courseId") int courseId) {
        Course course = courseService.getCourseById(courseId);
        if( course == null ){
            return new HttpResponseWrapperUtil(null, -1, "课程不存在");
        }
        List<Contest> contestList = contestService.getContestsOfCourse(courseId);
        Map<String,Object> data = wrapContests2Json(contestList);
        return new HttpResponseWrapperUtil(data.get("contests"));
    }

    @ApiOperation("学生通过分页查看所有竞赛接口")
    @RequestMapping(value = "/getPageContests/{pageSize}/{pageIndex}", method = RequestMethod.GET)
    public HttpResponseWrapperUtil getPageContests(@PathVariable("pageSize") int pageSize,
                                                   @PathVariable("pageIndex") int pageIndex) {
        if( pageSize < 0 || pageIndex < 0 ) {
            return new HttpResponseWrapperUtil(null, -1, "failure");
        }
        List<Contest> contestList = contestService.getPageContests(pageSize,pageIndex);
        Map<String,Object> data = wrapContests2Json(contestList);
        data.put("totalContestsNum",contestService.getTotalContestsNum());
        return new HttpResponseWrapperUtil(data);
    }

    @ApiOperation("学生进入竞赛接口")
    @RequestMapping(value = "/enterContest/{contestId}", method = RequestMethod.GET)
    public HttpResponseWrapperUtil enterContest(@PathVariable("contestId") int contestId) {
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
        List<Long> problemIdList = new ArrayList<>();
        for (Problem problem : problemList) {
            problemIdList.add(problem.getProblemId());
        }
        data.put("problemIdList",problemIdList);
        return new HttpResponseWrapperUtil(data);
    }

    /**
     * 将竞赛列表封装成JSON数据
     * @param contestList - 竞赛列表
     * @return
     */
    public Map<String,Object> wrapContests2Json(List<Contest> contestList) {
        Map<String,Object> data = new HashMap<>();
        List<Object> contests = new ArrayList<>();
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
        data.put("contests",contests);
        return data;
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
