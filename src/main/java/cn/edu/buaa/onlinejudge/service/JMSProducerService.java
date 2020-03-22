package cn.edu.buaa.onlinejudge.service;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JMSProducerService {

    private final JmsMessagingTemplate jmsMessagingTemplate;

    public JMSProducerService(JmsMessagingTemplate jmsMessagingTemplate) {
        this.jmsMessagingTemplate = jmsMessagingTemplate;
    }

    /**
     * 默认的消息队列名称
     */
    private static final String DESTINATION_NAME = "submission_queue";

    /**
     * 向默认消息队列发送Map<String, Object>类型消息
     * @param message - 消息
     */
    public void sendMessage(Map<String, Object> message){
        sendMessage(DESTINATION_NAME, message);
    }

    /**
     * 向指定消息队列发送Map<String, Object>类型消息
     * @param destinationName - 目的消息队列名称
     * @param message - 消息
     */
    public void sendMessage(String destinationName, Map<String, Object> message){
        jmsMessagingTemplate.convertAndSend(new ActiveMQQueue(destinationName), message);
    }

    /**
     * 向默认消息队列发送Map<String, Object>类型消息
     * @param message - long类型消息
     */
    public void sendMessage(long message){
        sendMessage(DESTINATION_NAME, message);
    }

    /**
     * 向指定消息队列发送Map<String, Object>类型消息
     * @param destinationName - 目的消息队列名称
     * @param message - long类型的消息
     */
    public void sendMessage(String destinationName, long message){
        jmsMessagingTemplate.convertAndSend(new ActiveMQQueue(destinationName), message);
    }
}
