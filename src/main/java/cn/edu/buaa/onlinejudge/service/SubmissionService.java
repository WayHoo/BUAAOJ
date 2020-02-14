package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.SubmissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
