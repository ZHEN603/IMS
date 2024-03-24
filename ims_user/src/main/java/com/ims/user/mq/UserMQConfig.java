package com.ims.user.mq;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserMQConfig {

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("companyExchange");
    }

    @Bean
    public Queue coAdminCreateQueue() {
        return new Queue("coAdminCreateQueue", true);
    }


    @Bean
    public Binding bindingCreate(Queue coAdminCreateQueue, TopicExchange companyExchange) {
        return BindingBuilder.bind(coAdminCreateQueue).to(companyExchange).with("coAdmin.create");
    }

}