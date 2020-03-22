package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.ContestMapper;
import cn.edu.buaa.onlinejudge.mapper.ProblemMapper;
import cn.edu.buaa.onlinejudge.model.Contest;
import cn.edu.buaa.onlinejudge.model.ContestRank;
import cn.edu.buaa.onlinejudge.utils.DateUtil;
import cn.edu.buaa.onlinejudge.utils.FileUtil;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ContestService {

    private final ContestMapper contestMapper;

    private final ProblemMapper problemMapper;

    public ContestService(ContestMapper contestMapper, ProblemMapper problemMapper) {
        this.contestMapper = contestMapper;
        this.problemMapper = problemMapper;
    }

    public Contest getContestById(int contestId){
       return contestMapper.getContestById(contestId);
   }

   public List<Contest> getVisibleContestsOfCourse(int courseId) {
       return contestMapper.getVisibleContestsOfCourse(courseId);
   }

   public List<Contest> getAllContestsOfCourse(int courseId){
       return contestMapper.getAllContestsOfCourse(courseId);
   }

   public List<Integer> getAllContestIdOfCourse(int courseId){
        return contestMapper.getAllContestIdOfCourse(courseId);
   }

   public int getTotalContestsNum(){
       return contestMapper.getTotalContestsNum();
   }

    /**
     * 从所有可见的竞赛中获取指定页面大小和页面索引的竞赛
     * @param pageSize - 页面大小
     * @param pageIndex - 页面索引
     * @return
     */
   public List<Contest> getPageVisibleContests(int pageSize, int pageIndex) {
       return contestMapper.getPageVisibleContests(pageSize,pageIndex);
   }

   public List<Contest> fuzzyQueryVisibleContestsByName(String contestName){
       if( contestName == null || contestName.length() == 0 ){
           return null;
       }
       return contestMapper.fuzzyQueryVisibleContestsByName(contestName);
   }

   public List<Integer> getVisibleContestIdListByCourseIdList(List<Integer> courseIdList) {
       return contestMapper.getVisibleContestIdListByCourseIdList(courseIdList);
   }

   public void insertContest(Contest contest){
       contestMapper.insertContest(contest);
   }

   public void reverseContestVisibility(int contestId){
       int visibility = contestMapper.getContestById(contestId).isVisible() ? 1 : 0;
       contestMapper.setContestVisibility(contestId, 1 - visibility);
   }

   public void reverseContestStatus(int contestId){
       int status = contestMapper.getContestById(contestId).isAnswerable() ? 1 : 0;
       contestMapper.setContestStatus(contestId, 1 - status);
   }

   public void updateContest(Contest contest){
       contestMapper.updateContest(contest);
   }

   public List<ContestRank> getContestPageRanks(int pageSize, int pageIndex, int contestId){
       return contestMapper.getContestPageRanks(pageSize, pageIndex, contestId);
   }

   public List<ContestRank> getContestLimitRanks(int contestId, int limit){
       return contestMapper.getContestLimitRanks(contestId, limit);
   }

    /**
     * 删除竞赛会同时删除竞赛下所有题目以及题目测试点文件
     * @param contestId - 竞赛ID
     */
   public void deleteContest(int contestId){
       //在删除竞赛和题目之前获取竞赛下所有题目ID
       List<Long> problemIdList = problemMapper.getAllProblemIdOfContest(contestId);
       contestMapper.deleteContest(contestId);
       /**删除竞赛下所有题目对应的测试点文件
        * 此处不能调用ProblemService中的deleteProblemCheckpointFile方法
        * 否则会造成CourseService、ContestService、ProblemService三者之间循环依赖
        */
       for (Long problemId : problemIdList) {
           String checkpointFileAbsolutePath =
                   FileUtil.getCheckpointUploadPath(Long.toString(problemId));
           //删除题目测试点文件夹及文件夹下所有文件
           FileUtil.delete(checkpointFileAbsolutePath);
       }
   }

   public List<ContestRank> getContestRankByStudentName(int contestId, String studentName) {
       if( studentName == null || studentName.length() == 0 ) {
           return null;
       }
       return contestMapper.getContestRankByStudentName(contestId, studentName);
   }

    /**
     * 判断比赛进行状态，0 - 未开始，1 - 进行中，2 - 已结束
     * @param contest - 竞赛对象
     * @return 竞赛进行状态值
     */
    public int judgeContestStatus(Contest contest) {
        Timestamp startTime = contest.getStartTime();
        Timestamp finishTime = contest.getFinishTime();
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
     * 判断竞赛在当前进行状态下是否可答题
     * @param contest - 竞赛对象
     * @return
     */
    public boolean isContestAnswerable(Contest contest){
        int contestStatus = judgeContestStatus(contest);
        //竞赛未开始一律不可答题
        if( contestStatus == 0 ){
            return false;
        } else if( contestStatus == 1 ){
            //进行中的竞赛一律可答题
            return true;
        } else{
            //已结束的竞赛的可答题情况以竞赛的isAnswerable值为准
            return contest.isAnswerable();
        }
    }
}
