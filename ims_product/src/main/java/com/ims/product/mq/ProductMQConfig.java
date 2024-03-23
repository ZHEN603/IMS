package com.ims.product.mq;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductMQConfig {

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("productRollbackExchange");
    }

    @Bean
    public Queue productRollbackQueue() {
        return new Queue("productRollbackQueue", true);
    }


    @Bean
    public Binding bindingRollback(Queue productRollbackQueue, TopicExchange productRollbackExchange) {
        return BindingBuilder.bind(productRollbackQueue).to(productRollbackExchange).with("productRollback");
    }

}