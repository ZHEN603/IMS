package com.ims.product.mq;
import com.ims.domain.product.Product;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProductMessageSender {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendProductUpdateMessage(Product message) {
        rabbitTemplate.convertAndSend("productExchange", "product.update", message);
    }

    public void sendProductDeleteMessage(String message) {
        rabbitTemplate.convertAndSend("productExchange", "product.delete", message);
    }

    public void sendProductCreateMessage(Map<String, Object> message) {
        rabbitTemplate.convertAndSend("productExchange", "product.create", message);
    }
}
