package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.ContestMapper;
import cn.edu.buaa.onlinejudge.model.Contest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ContestService {
   @Autowired
    private ContestMapper contestMapper;

   public Contest getContestById(int contestId){
       return contestMapper.getContestById(contestId);
   }

   public List<Contest> getContestsOfCourse(int courseId) {
       return contestMapper.getContestsOfCourse(courseId);
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
}
