package com.ims.user.mq;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserMessageSender {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendRollbackMessage(String id) {
        rabbitTemplate.convertAndSend("companyRollbackExchange", "companyRollback", id);
    }
}
