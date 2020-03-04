package cn.edu.buaa.onlinejudge.model;

public class ContestRank {
    /**
     * 一次错误提交的罚时
     */
    public static final int TIME_PENALTY = 10;

    /**
     * 竞赛ID
     */
    private int contestId;

    /**
     * 学生ID
     */
    private long studentId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学号
     */
    private String studentNumber;

    /**
     * 学生在竞赛中所得分数
     */
    private int score;

    /**
     * 学生在竞赛中的罚时
     */
    private int wrongSubmitTimes;

    /**
     * 排名值
     */
    private int rankNum;

    public ContestRank() { }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getWrongSubmitTimes() {
        return wrongSubmitTimes;
    }

    public void setWrongSubmitTimes(int wrongSubmitTimes) {
        this.wrongSubmitTimes = wrongSubmitTimes;
    }

    public int getRankNum() {
        return rankNum;
    }

    public void setRankNum(int rankNum) {
        this.rankNum = rankNum;
    }

    @Override
    public String toString() {
        return "ContestRank{" +
                "contestId=" + contestId +
                ", studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", studentNumber='" + studentNumber + '\'' +
                ", score=" + score +
                ", wrongSubmitTimes=" + wrongSubmitTimes +
                ", rankNum=" + rankNum +
                '}';
    }
}
