package cn.edu.buaa.onlinejudge;

import cn.edu.buaa.onlinejudge.mapper.LanguageMapper;
import cn.edu.buaa.onlinejudge.mapper.ProblemMapper;
import cn.edu.buaa.onlinejudge.model.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MybatisTest extends OnlinejudgeApplicationTests {

    @Autowired
    private ProblemMapper problemMapper;

    @Test
    public void problemTest(){
        List<Long> problemIdList = problemMapper.getAllProblemIdOfContest(1);
        for (Long problemId : problemIdList) {
            System.out.println("problemId: " + problemId);
        }
    }
}
