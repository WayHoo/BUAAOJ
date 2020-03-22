package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.ProblemRankInfoMapper;
import cn.edu.buaa.onlinejudge.mapper.SubmissionMapper;
import cn.edu.buaa.onlinejudge.model.Contest;
import cn.edu.buaa.onlinejudge.model.ProblemRankInfo;
import cn.edu.buaa.onlinejudge.model.Submission;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class SubmissionService {
    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private ProblemRankInfoMapper problemRankInfoMapper;

    @Autowired
    private ContestService contestService;

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

    /**
     * 将学生的提交记录插入数据表
     * 初次插入submissions数据表示尚未经过评测机评测，默认judgeResult为"PD"
     * 因此无需插入`problem_rank_info`数据表
     * @param submission - 提交记录对象
     */
    public void insertSubmission(Submission submission){
        submissionMapper.insertSubmission(submission);
    }

    /**
     * 更新学生的提交记录，主要用于回写经过代码评测后的提交记录
     * 如果该提交是在竞赛进行期间完成的，会将提交记录稍作处理插入`problem_rank_info`数据表
     * @param submission - 提交记录对象（主要存储评测结果信息，其余字段为空）
     */
    public void updateSubmission(Submission submission){
        submissionMapper.updateSubmission(submission);
        Submission realSubmission = submissionMapper.getSubmissionById(submission.getSubmissionId());
        insertOrUpdateProblemRankInfo(realSubmission);
    }

    /**
     * 将在竞赛时间范围内提交的记录稍作处理插入或更新problem_rank_info数据表，便于排名
     * @param submission - 提交记录对象
     */
    public void insertOrUpdateProblemRankInfo(Submission submission){
        Contest contest = contestService.getContestById(submission.getContestId());
        if( contest == null ){
            return;
        }
        Timestamp startTime = contest.getStartTime();
        Timestamp finishTime = contest.getFinishTime();
        if( submission.getSubmissionId() > 0 &&
                submission.getSubmitTime().after(startTime) &&
                submission.getSubmitTime().before(finishTime) ) {
            ProblemRankInfo problemRankInfo = problemRankInfoMapper.getProblemRankInfoByStudentIdAndProblemId(submission.getStudentId(), submission.getProblemId());
            if (problemRankInfo == null) {
                int wrongSubmitTimes = "AC".equals(submission.getJudgeResult()) ? 0 : 1;
                problemRankInfo = new ProblemRankInfo(submission.getStudentId(),
                        submission.getContestId(), submission.getProblemId(),
                        submission.getJudgeScore(), submission.getJudgeResult(),
                        wrongSubmitTimes, submission.getSubmitTime());
                problemRankInfoMapper.insertProblemRankInfo(problemRankInfo);
            } else {
                problemRankInfo.setJudgeResult(submission.getJudgeResult());
                problemRankInfo.setScore(submission.getJudgeScore());
                problemRankInfo.setSubmitTime(submission.getSubmitTime());
                if (!"AC".equals(submission.getJudgeResult())) {
                    problemRankInfo.setWrongSubmitTimes(problemRankInfo.getWrongSubmitTimes() + 1);
                }
                problemRankInfoMapper.updateProblemRankInfo(problemRankInfo);
            }
        }
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
}
