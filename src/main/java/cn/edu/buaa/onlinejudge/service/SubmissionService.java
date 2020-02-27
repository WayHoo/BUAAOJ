package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.ProblemMapper;
import cn.edu.buaa.onlinejudge.mapper.ProblemRankInfoMapper;
import cn.edu.buaa.onlinejudge.mapper.SubmissionMapper;
import cn.edu.buaa.onlinejudge.model.ProblemRankInfo;
import cn.edu.buaa.onlinejudge.model.Submission;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionService {
    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private ProblemRankInfoMapper problemRankInfoMapper;

    public long getProblemSubmitStudents(long problemId) {
        return problemRankInfoMapper.getProblemSubmitStudents(problemId);
    }

    public long getProblemSubmitTimes(long problemId) {
        return submissionMapper.getProblemSubmitTimes(problemId);
    }

    public long getProblemAcceptStudents(long problemId) {
        return problemRankInfoMapper.getProblemAcceptStudents(problemId);
    }

    public Submission getStudentLatestSubmissionOfProblem(long studentId,long problemId) {
        return submissionMapper.getStudentLatestSubmissionOfProblem(studentId,problemId);
    }

    public List<Submission> getSubmissionsByStudentIdAndProblemId(@Param("studentId") long studentId, @Param("problemId") long problemId) {
        return submissionMapper.getSubmissionsByStudentIdAndProblemId(studentId,problemId);
    }

    /**
     * 获取学生对指定竞赛的所有提交记录
     * @param studentId - 学生ID
     * @param contestId - 竞赛ID
     * @return 竞赛对象列表
     */
    public List<Submission> getSubmissionsOfContest(long studentId, int contestId) {
        return submissionMapper.getSubmissionsByStudentIdAndContestId(studentId, contestId);
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
        if( submission != null ){
            if( "PD".equals(submission.getJudgeResult()) ){
                status = 1;
            } else if( "AC".equals(submission.getJudgeResult()) ){
                status = 3;
            } else if( submission.getJudgeScore() > 0 ){
                status = 2;
            } else{
                status = -1;
            }
        }
        return status;
    }

    public ProblemRankInfo getProblemRankInfo(long studentId, long problemId){
        return problemRankInfoMapper.getProblemRankInfoByStudentIdAndProblemId(studentId, problemId);
    }

    public void insertProblemRankInfo(ProblemRankInfo problemRankInfo){
        problemRankInfoMapper.insertProblemRankInfo(problemRankInfo);
    }

    public void updateProblemRankInfo(ProblemRankInfo problemRankInfo){
        problemRankInfoMapper.updateProblemRankInfo(problemRankInfo);
    }
}
