package cn.edu.buaa.onlinejudge.model;

import java.sql.Timestamp;
import java.util.Date;

public class Contest {
    /**
     * 竞赛ID
     */
    private int contestId;

    /**
     * 竞赛名称
     */
    private String contestName;

    /**
     * 竞赛所属课程ID
     */
    private int courseId;

    /**
     * 竞赛开始时间
     */
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp startTime;

    /**
     * 竞赛结束时间
     */
    private Timestamp finishTime;

    /**
     * 竞赛是否对学生可见
     */
    private boolean isVisible;

    /**
     * 竞赛的可答题状态，针对已结束的竞赛。
     * true - 可答题，false - 不可答题
     */
    private boolean isAnswerable;

    /**
     * 竞赛简介
     */
    private String introduction;

    /**
     * 竞赛状态.
     * 分别表示未开始、进行中和已结束.
     */
    public enum CONTEST_STATUS { READY, LIVE, DONE }

    public Contest() { }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isAnswerable() {
        return isAnswerable;
    }

    public void setAnswerable(boolean answerable) {
        isAnswerable = answerable;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        return "Contest{" +
                "contestId=" + contestId +
                ", contestName='" + contestName + '\'' +
                ", courseId=" + courseId +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", isVisible=" + isVisible +
                ", isAnswerable=" + isAnswerable +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
