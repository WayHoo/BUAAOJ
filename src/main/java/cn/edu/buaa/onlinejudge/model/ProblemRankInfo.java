package cn.edu.buaa.onlinejudge.model;

import java.sql.Timestamp;

/**
 * 对应`problem_rank_info`数据表
 * 用于存储学生在竞赛进行期间的提交统计数据，便于排名和统计
 */
public class ProblemRankInfo {
    /**
     * 学生ID
     */
    private long studentId;

    /**
     * 题目所属竞赛ID
     */
    private int contestId;

    /**
     * 题目ID
     */
    private long problemId;

    /**
     * 得分（以在竞赛时间范围内的最后一次提交为准）
     */
    private int score;

    /**
     * 评测结果（以在竞赛时间范围内的最后一次提交为准）
     */
    private String judgeResult;

    /**
     * 错误提交次数（仅计算在竞赛时间范围内的错误提交次数）
     */
    private int wrongSubmitTimes;

    /**
     * 代码提交时间（在竞赛时间范围内的最后一次提交时间）
     */
    private Timestamp submitTime;

    public ProblemRankInfo() { }

    public ProblemRankInfo(long studentId, int contestId, long problemId, int score, String judgeResult, int wrongSubmitTimes, Timestamp submitTime) {
        this.studentId = studentId;
        this.contestId = contestId;
        this.problemId = problemId;
        this.score = score;
        this.judgeResult = judgeResult;
        this.wrongSubmitTimes = wrongSubmitTimes;
        this.submitTime = submitTime;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public long getProblemId() {
        return problemId;
    }

    public void setProblemId(long problemId) {
        this.problemId = problemId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getJudgeResult() {
        return judgeResult;
    }

    public void setJudgeResult(String judgeResult) {
        this.judgeResult = judgeResult;
    }

    public int getWrongSubmitTimes() {
        return wrongSubmitTimes;
    }

    public void setWrongSubmitTimes(int wrongSubmitTimes) {
        this.wrongSubmitTimes = wrongSubmitTimes;
    }

    public Timestamp getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Timestamp submitTime) {
        this.submitTime = submitTime;
    }
}
