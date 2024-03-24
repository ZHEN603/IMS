package com.ims.company.mq;

import com.ims.company.service.CompanyService;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CompanyMessageReceiver {

    @Resource
    CompanyService companyService;


    @RabbitListener(queues = "companyRollbackQueue")
    public void handleCompanyRollback(String id) {
        companyService.deleteById(id);
    }

}