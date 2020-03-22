package cn.edu.buaa.onlinejudge;

import cn.edu.buaa.onlinejudge.service.JMSProducerService;
import cn.edu.buaa.onlinejudge.utils.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class MessageTest extends OnlinejudgeApplicationTests {

    @Autowired
    private JMSProducerService jmsProducerService;

    @Test
    public void testSendMessage(){
//        Map<String, Object> message = new HashMap<>();
//        message.put("submissionId", 10);
//        message.put("contestId", 1);
//        message.put("problemId", 1520);
//        message.put("submitTime", null);
//        message.put("submitCode", null);
        long message = 10;
        System.out.println("============>>>>> 发送消息, " + message);
        jmsProducerService.sendMessage(message);
    }
}
