package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.ContestMapper;
import cn.edu.buaa.onlinejudge.model.Contest;
import cn.edu.buaa.onlinejudge.model.ContestRank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContestService {
   @Autowired
    private ContestMapper contestMapper;

   public Contest getContestById(int contestId){
       return contestMapper.getContestById(contestId);
   }

   public List<Contest> getVisibleContestsOfCourse(int courseId) {
       return contestMapper.getVisibleContestsOfCourse(courseId);
   }

   public List<Contest> getAllContestsOfCourse(int courseId){
       return contestMapper.getAllContestsOfCourse(courseId);
   }

   public int getTotalContestsNum(){
       return contestMapper.getTotalContestsNum();
   }

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

   public void deleteContest(int contestId){
       contestMapper.deleteContest(contestId);
   }

   public List<ContestRank> getContestRankByStudentName(int contestId, String studentName) {
       if( studentName == null || studentName.length() == 0 ) {
           return null;
       }
       return contestMapper.getContestRankByStudentName(contestId, studentName);
   }
}
