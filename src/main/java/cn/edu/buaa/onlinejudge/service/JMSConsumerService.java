package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.model.Submission;
import cn.edu.buaa.onlinejudge.utils.DateUtil;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

@Service
public class JMSConsumerService {

    private final SubmissionService submissionService;

    public JMSConsumerService(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @JmsListener(destination = "judge_result_queue")
    public void receiveMessage(Message message){
        if( message instanceof MapMessage){
            final MapMessage mapMessage = (MapMessage)message;
            Submission submission = new Submission();
            try {
                submission.setSubmissionId(mapMessage.getLong("submissionId"));
                submission.setExecuteTime(DateUtil.strToTimestamp(mapMessage.getString("executeTime")));
                submission.setUsedTime(mapMessage.getInt("usedTime"));
                submission.setUsedMemory(mapMessage.getInt("usedMemory"));
                submission.setJudgeScore(mapMessage.getInt("judgeScore"));
                submission.setJudgeResult(mapMessage.getString("judgeResult"));
                submissionService.updateSubmission(submission);
//                System.out.println("<<<<<<============ 收到消息，" + submission);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
