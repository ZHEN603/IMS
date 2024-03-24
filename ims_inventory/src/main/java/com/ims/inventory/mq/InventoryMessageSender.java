package com.ims.inventory.mq;
import com.ims.domain.product.Product;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InventoryMessageSender {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendRollbackMessage(Map<String, String> map) {
        rabbitTemplate.convertAndSend("productRollbackExchange", "productRollback", map);
    }
}
