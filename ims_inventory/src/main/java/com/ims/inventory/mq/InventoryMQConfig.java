package com.ims.inventory.mq;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryMQConfig {

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("productExchange");
    }

    @Bean
    public Queue productCreateQueue() {
        return new Queue("productCreateQueue", true);
    }

    @Bean
    public Queue productUpdateQueue() {
        return new Queue("productUpdateQueue", true);
    }

    @Bean
    public Queue productDeleteQueue() {
        return new Queue("productDeleteQueue", true);
    }

    @Bean
    public Binding bindingCreate(Queue productCreateQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(productCreateQueue).to(productExchange).with("product.create");
    }

    @Bean
    public Binding bindingUpdate(Queue productUpdateQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(productUpdateQueue).to(productExchange).with("product.update");
    }

    @Bean
    public Binding bindingDelete(Queue productDeleteQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(productDeleteQueue).to(productExchange).with("product.delete");
    }
}