package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.Contest;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ContestMapper {
    /**
     * 根据竞赛ID获取竞赛对象
     * @param contestId - 竞赛ID
     * @return Contest对象
     */
    Contest getContestById(int contestId);

    /**
     * 查看课程下的所有竞赛
     * @param courseId - 课程ID
     * @return Contest对象列表
     */
    List<Contest> getContestsOfCourse(int courseId);

    /**
     * 获取所有竞赛的个数
     * @return 竞赛个数
     */
    int getTotalContestsNum();

    /**
     * 对所有竞赛按照竞赛ID升序排序，然后获取指定页面大小和页面索引的所有竞赛
     * @param pageSize - 页面大小
     * @param pageIndex - 页面索引，下标从0开始
     * @return - Contest对象列表
     */
    List<Contest> getPageContests(@Param("pageSize") int pageSize, @Param("pageIndex") int pageIndex);

    /**
     * 获取多个课程下的所有竞赛ID
     * @param courseIdList - 课程ID列表
     * @return 竞赛ID列表
     */
    List<Integer> getContestIdListByCourseIdList(List<Integer> courseIdList);
}
