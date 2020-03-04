package cn.edu.buaa.onlinejudge;

import cn.edu.buaa.onlinejudge.model.Contest;
import cn.edu.buaa.onlinejudge.service.ContestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Date;

public class TimestampTest extends OnlinejudgeApplicationTests {

    @Autowired
    private ContestService contestService;

    @Test
    public void testTimestamp(){
        //当前时间
        Timestamp current = new Timestamp(new Date().getTime());
        System.out.println("current = " + current.toString());
        Contest contest = contestService.getContestById(1);
        System.out.println("startTime = " + contest.getStartTime());
    }
}
