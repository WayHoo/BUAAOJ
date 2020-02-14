package cn.edu.buaa.onlinejudge.mapper;

import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionMapper {
    long getProblemSubmitStudents(long problemId);
    long getProblemSubmitTimes(long problemId);
    long getProblemAcceptStudents(long problemId);
}
