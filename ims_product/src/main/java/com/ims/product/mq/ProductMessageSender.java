package com.ims.product.mq;
import com.ims.common.message.ProductMessage;
import com.ims.domain.product.Product;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductMessageSender {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ProductMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendProductUpdateMessage(Product message) {
        rabbitTemplate.convertAndSend("productExchange", "product.update", message);
    }

    public void sendProductDeleteMessage(Product message) {
        rabbitTemplate.convertAndSend("productExchange", "product.delete", message);
    }

    public void sendProductCreateMessage(Product message) {
        rabbitTemplate.convertAndSend("productExchange", "product.create", message);
    }
}
