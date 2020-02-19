package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.SubmissionMapper;
import cn.edu.buaa.onlinejudge.model.Submission;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionService {
    @Autowired
    private SubmissionMapper submissionMapper;

    public long getProblemSubmitStudents(long problemId) {
        return submissionMapper.getProblemSubmitStudents(problemId);
    }

    public long getProblemSubmitTimes(long problemId) {
        return submissionMapper.getProblemSubmitTimes(problemId);
    }

    public long getProblemAcceptStudents(long problemId) {
        return submissionMapper.getProblemAcceptStudents(problemId);
    }

    public Submission getStudentLatestSubmissionOfProblem(long studentId,long problemId) {
        return submissionMapper.getStudentLatestSubmissionOfProblem(studentId,problemId);
    }

    public List<Submission> getSubmissionsByStudentIdAndProblemId(@Param("studentId") long studentId, @Param("problemId") long problemId) {
        return submissionMapper.getSubmissionsByStudentIdAndProblemId(studentId,problemId);
    }

    public List<Submission> getSubmissionsByStudentIdAndProblemIdList(long studentId, List<Long> problemIdList) {
        return submissionMapper.getSubmissionsByStudentIdAndProblemIdList(studentId,problemIdList);
    }

    public void insertSubmission(Submission submission){
        submissionMapper.insertSubmission(submission);
    }

    /**
     * 判断学生答题状态
     * 答错-1（红色），未答题0（白色），判题中1（灰色），部分正确2（黄色），通过3（绿色）
     * @param studentId - 学生ID
     * @param problemId - 题目ID
     * @return
     */
    public int getSubmissionStatus(long studentId,long problemId){
        Submission submission = submissionMapper.getStudentLatestSubmissionOfProblem(studentId,problemId);
        int status = 0;
        if( submission == null ){
            status = 0;
        } else if( "PD".equals(submission.getJudgeResult()) ){
            status = 1;
        } if( "AC".equals(submission.getJudgeResult()) ){
            status = 3;
        } else if( submission.getJudgeScore() > 0 ){
            status = 2;
        } else{
            status = -1;
        }
        return status;
    }
}
