package com.ims.user.mq;
import com.ims.domain.company.Company;
import com.ims.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserMessageReceiver {

    @Resource
    UserService userService;

    @Resource
    UserMessageSender userMessageSender;

    @RabbitListener(queues = "coAdminCreateQueue")
    public void handleCompanyCreate(Company company) {
        try{
            userService.saveAdmin(company);
        }catch (Exception e){
            System.out.println(e);
            userMessageSender.sendRollbackMessage(company.getId());
        }
    }

}