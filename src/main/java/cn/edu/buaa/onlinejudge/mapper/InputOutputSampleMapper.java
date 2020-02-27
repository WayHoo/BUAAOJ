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

    /**
     * 插入输入输出样例
     * @param inputOutputSample - 输入输出样例对象
     */
    void insertInputOutputSample(InputOutputSample inputOutputSample);

    /**
     * 修改输入输出样例
     * @param inputOutputSample - 输入输出样例对象
     */
    void updateInputOutputSample(InputOutputSample inputOutputSample);

    /**
     * 获取题目的输入输出样例组数
     * @param problemId - 题目ID
     * @return
     */
    int getInputOutputSampleNumOfProblem(long problemId);

    /**
     * 删除题目的所有输入输出样例
     * @param problemId - 题目ID
     */
    void deleteInputOutputSamplesOfProblem(long problemId);
}
