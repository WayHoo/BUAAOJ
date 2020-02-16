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
}
