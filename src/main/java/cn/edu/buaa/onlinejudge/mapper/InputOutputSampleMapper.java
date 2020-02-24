package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.InputOutputSample;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InputOutputSampleMapper {
    /**
     * 获取题目的输入输出样例
     * @param problemId - 题目ID
     * @return 输入输出样例对象列表
     */
    List<InputOutputSample> getInputOutputSamplesOfProblem(long problemId);
}
