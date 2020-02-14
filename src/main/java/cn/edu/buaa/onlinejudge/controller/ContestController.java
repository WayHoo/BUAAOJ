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
        Map<String,Object> data = wrapContestsJson(contestList);
        return new HttpResponseWrapperUtil(data);
    }

    @ApiOperation("学生通过分页查看所有竞赛接口")
    @RequestMapping(value = "/getPageContests/{pageSize}/{pageIndex}", method = RequestMethod.GET)
    public HttpResponseWrapperUtil getPageContests(@PathVariable("pageSize") int pageSize,
                                                   @PathVariable("pageIndex") int pageIndex) {
        if( pageSize < 0 || pageIndex < 0 ) {
            return new HttpResponseWrapperUtil(null, -1, "failure");
        }
        List<Contest> contestList = contestService.getPageContests(pageSize,pageIndex);
        Map<String,Object> data = wrapContestsJson(contestList);
        data.put("totalContestsNum",contestService.getTotalContestsNum());
        return new HttpResponseWrapperUtil(data);
    }

    @ApiOperation("学生通过分页查看所有竞赛接口")
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
    public Map<String,Object> wrapContestsJson(List<Contest> contestList) {
        Map<String,Object> data = new HashMap<>();
        data.put("serverCurrentTime",DateUtil.getCurrentTime());
        List<Object> contests = new ArrayList<>();
        for (Contest contest : contestList) {
            Map<String,Object> metadata = new HashMap<>();
            metadata.put("contestId",contest.getContestId());
            metadata.put("contestName",contest.getContestName());
            metadata.put("contestStatus",contest.isAnswerable() ? 1 : 0);
            metadata.put("startTime", DateUtil.formatTimestamp(contest.getStartTime()));
            metadata.put("finishTime",DateUtil.formatTimestamp(contest.getFinishTime()));
            contests.add(metadata);
        }
        data.put("contests",contests);
        return data;
    }
}
