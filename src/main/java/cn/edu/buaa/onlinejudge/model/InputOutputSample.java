package cn.edu.buaa.onlinejudge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class InputOutputSample {

    /**
     * 题目ID
     */
    @JsonIgnore
    private long problemId;

    /**
     * 题目的输入输出样例序号
     */
    @JsonIgnore
    private int sampleNumber;

    /**
     * 样例输入
     */
    private String sampleInput;

    /**
     * 样例输出
     */
    private String sampleOutput;

    public InputOutputSample() { }

    public InputOutputSample(long problemId, int sampleNumber, String sampleInput, String sampleOutput) {
        this.problemId = problemId;
        this.sampleNumber = sampleNumber;
        this.sampleInput = sampleInput;
        this.sampleOutput = sampleOutput;
    }

    public long getProblemId() {
        return problemId;
    }

    public void setProblemId(long problemId) {
        this.problemId = problemId;
    }

    public int getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(int sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public String getSampleInput() {
        return sampleInput;
    }

    public void setSampleInput(String sampleInput) {
        this.sampleInput = sampleInput;
    }

    public String getSampleOutput() {
        return sampleOutput;
    }

    public void setSampleOutput(String sampleOutput) {
        this.sampleOutput = sampleOutput;
    }
}
