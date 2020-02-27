package cn.edu.buaa.onlinejudge.model;

import java.util.List;

public class Problem {
    /**
     * 题目ID
     */
    private long problemId;

    /**
     * 题目所属竞赛ID
     */
    private int contestId;

    /**
     * 题目在竞赛中的序号
     */
    private int problemNumber;

    /**
     * 题目是否对学生可见，数据库默认可见
     */
    private boolean isVisible;

    /**
     * 题目的评测机制，0 - 在线评测，1 - 人工评测
     */
    private int judgeMechanism;

    /**
     * 题目作者
     */
    private String author;

    /**
     * 题目名称
     */
    private String problemName;

    /**
     * 题目运行最长时间限制，单位为毫秒
     */
    private int timeLimit;

    /**
     * 题目运行最大内存限制，单位为KB
     */
    private int memoryLimit;

    /**
     * 题目描述
     */
    private String description;

    /**
     * 题目输入格式说明
     */
    private String inputFormat;

    /**
     * 题目输出格式说明
     */
    private String outputFormat;

    /**
     * 题目输入输出样例数组
     */
    List<InputOutputSample> inputOutputSamples;

    /**
     * 样例解释
     */
    private String sampleExplanation;

    /**
     * 题目提示
     */
    private String hint;

    /**
     * 题目已有代码，用于填空题
     */
    private String code;

    public Problem() { }

    public long getProblemId() {
        return problemId;
    }

    public void setProblemId(long problemId) {
        this.problemId = problemId;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public int getProblemNumber() {
        return problemNumber;
    }

    public void setProblemNumber(int problemNumber) {
        this.problemNumber = problemNumber;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public int getJudgeMechanism() {
        return judgeMechanism;
    }

    public void setJudgeMechanism(int judgeMechanism) {
        this.judgeMechanism = judgeMechanism;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(int memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInputFormat() {
        return inputFormat;
    }

    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public List<InputOutputSample> getInputOutputSamples() {
        return inputOutputSamples;
    }

    public void setInputOutputSamples(List<InputOutputSample> inputOutputSamples) {
        this.inputOutputSamples = inputOutputSamples;
    }

    public String getSampleExplanation() {
        return sampleExplanation;
    }

    public void setSampleExplanation(String sampleExplanation) {
        this.sampleExplanation = sampleExplanation;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
