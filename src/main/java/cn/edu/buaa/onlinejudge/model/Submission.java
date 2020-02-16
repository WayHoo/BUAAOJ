package cn.edu.buaa.onlinejudge.model;

import java.sql.Timestamp;

public class Submission {
    /**
     * 提交ID
     */
    private long submissionId;

    /**
     * 学生ID
     */
    private long studentId;

    /**
     * 题目ID
     */
    private long problemId;

    /**
     * 提交的代码所使用的编程语言ID
     * 默认为Python 3，即ID = 1
     */
    private int languageId = 1;

    /**
     * 提交时间
     */
    private Timestamp submitTime;

    /**
     * 代码评测执行时间
     */
    private Timestamp executeTime;

    /**
     * 代码执行耗时
     */
    private Integer usedTime;

    /**
     * 代码执行内存消耗
     */
    private Integer usedMemory;

    /**
     * 评测得分
     */
    private int judgeScore;

    /**
     * 评测结果，默认为"PD"(Pending)
     */
    private String judgeResult = "PD";

    /**
     * 提交的代码
     */
    private String submitCode;

    /**
     * 编译输出
     */
    private String compileOutput;

    public Submission() { }

    public long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(long submissionId) {
        this.submissionId = submissionId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getProblemId() {
        return problemId;
    }

    public void setProblemId(long problemId) {
        this.problemId = problemId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public Timestamp getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Timestamp submitTime) {
        this.submitTime = submitTime;
    }

    public Timestamp getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Timestamp executeTime) {
        this.executeTime = executeTime;
    }

    public Integer getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Integer usedTime) {
        this.usedTime = usedTime;
    }

    public Integer getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(Integer usedMemory) {
        this.usedMemory = usedMemory;
    }

    public int getJudgeScore() {
        return judgeScore;
    }

    public void setJudgeScore(int judgeScore) {
        this.judgeScore = judgeScore;
    }

    public String getJudgeResult() {
        return judgeResult;
    }

    public void setJudgeResult(String judgeResult) {
        this.judgeResult = judgeResult;
    }

    public String getSubmitCode() {
        return submitCode;
    }

    public void setSubmitCode(String submitCode) {
        this.submitCode = submitCode;
    }

    public String getCompileOutput() {
        return compileOutput;
    }

    public void setCompileOutput(String compileOutput) {
        this.compileOutput = compileOutput;
    }
}
