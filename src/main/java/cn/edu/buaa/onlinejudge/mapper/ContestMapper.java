package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.Contest;
import cn.edu.buaa.onlinejudge.model.ContestRank;
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
     * 查看课程下的所有可见的竞赛
     * @param courseId - 课程ID
     * @return Contest对象列表
     */
    List<Contest> getVisibleContestsOfCourse(int courseId);

    /**
     * 查看课程下的所有竞赛（包括不可见的竞赛）
     * @param courseId - 课程ID
     * @return Contest对象列表
     */
    List<Contest> getAllContestsOfCourse(int courseId);
    /**
     * 获取所有竞赛的个数
     * @return 竞赛个数
     */
    int getTotalContestsNum();

    /**
     * 对所有可见竞赛按照竞赛ID升序排序，然后获取指定页面大小和页面索引的所有竞赛
     * @param pageSize - 页面大小
     * @param pageIndex - 页面索引，下标从0开始
     * @return - Contest对象列表
     */
    List<Contest> getPageVisibleContests(@Param("pageSize") int pageSize, @Param("pageIndex") int pageIndex);

    /**
     * 获取多个课程下的所有竞赛ID
     * @param courseIdList - 课程ID列表
     * @return 竞赛ID列表
     */
    List<Integer> getVisibleContestIdListByCourseIdList(List<Integer> courseIdList);

    /**
     * 插入竞赛对象
     * @param contest - 竞赛对象
     */
    void insertContest(Contest contest);

    /**
     * 设置竞赛的可见性
     * @param contestId - 竞赛ID
     * @param visibility - 可见性值（可见1，不可见0）
     */
    void setContestVisibility(@Param("contestId") int contestId, @Param("visibility") int visibility);

    /**
     * 设置竞赛的答题状态
     * @param contestId - 竞赛ID
     * @param status - 答题状态（可答题1，不可答题0）
     */
    void setContestStatus(@Param("contestId") int contestId, @Param("status") int status);

    /**
     * 更新竞赛信息
     * @param contest - 竞赛对象
     */
    void updateContest(Contest contest);

    /**
     * 获取指定页面大小和页面下标的竞赛排名
     * @param pageSize - 页面大小
     * @param pageIndex - 页面下标（从0开始）
     * @param contestId - 竞赛ID
     * @return ContestRank对象列表
     */
    List<ContestRank> getContestPageRanks(@Param("pageSize") int pageSize,
                                          @Param("pageIndex") int pageIndex,
                                          @Param("contestId") int contestId);

    /**
     * 删除竞赛
     * 由于由于竞赛数据表与题目数据表之间的外键约束关系设置了`ON DELETE CASCADE ON UPDATE CASCADE`，
     * 因此删除竞赛会自动删除竞赛中所有的题目
     * @param contestId - 竞赛ID
     */
    void deleteContest(int contestId);
}
