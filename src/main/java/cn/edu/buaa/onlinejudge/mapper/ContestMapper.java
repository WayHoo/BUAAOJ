package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.Contest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ContestMapper {
    Contest getContestById(int contestId);
    List<Contest> getContestsOfCourse(int courseId);
    int getTotalContestsNum();
    List<Contest> getPageContests(@Param("pageSize") int pageSize, @Param("pageIndex") int pageIndex);
}
