package com.ims.company.mq;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompanyMQConfig {

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("companyRollbackExchange");
    }

    @Bean
    public Queue companyRollbackQueue() {
        return new Queue("companyRollbackQueue", true);
    }


    @Bean
    public Binding bindingRollback(Queue companyRollbackQueue, TopicExchange companyRollbackExchange) {
        return BindingBuilder.bind(companyRollbackQueue).to(companyRollbackExchange).with("companyRollback");
    }

}