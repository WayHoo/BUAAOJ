package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.ContestMapper;
import cn.edu.buaa.onlinejudge.model.Contest;
import cn.edu.buaa.onlinejudge.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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

   public List<Contest> getPageContests(int pageSize,int pageIndex) {
       return contestMapper.getPageContests(pageSize,pageIndex);
   }

   public List<Integer> getContestIdListByCourseIdList(List<Integer> courseIdList) {
       return contestMapper.getContestIdListByCourseIdList(courseIdList);
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
}
