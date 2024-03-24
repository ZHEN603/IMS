package com.ims.company.mq;
import com.ims.domain.company.Company;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class CompanyMessageSender {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendCompanyCreateMessage(Company company) {
        rabbitTemplate.convertAndSend("companyExchange", "coAdmin.create", company);
    }
}
