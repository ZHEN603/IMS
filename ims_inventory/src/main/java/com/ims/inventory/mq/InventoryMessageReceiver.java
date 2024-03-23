package com.ims.inventory.mq;
import com.ims.domain.product.Product;
import com.ims.inventory.service.InventoryService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InventoryMessageReceiver {

    @Autowired
    InventoryService inventoryService;

    @Autowired
    InventoryMessageSender inventoryMessageSender;

    @RabbitListener(queues = "productCreateQueue")
    public void handleProductCreate(Product product) {
        // 处理商品创建
        System.out.println("create message: " + product.getName());
        try{
            Map<String, Object> map = new HashMap<>();
            inventoryService.save(map);
        }catch (Exception e){
            Map<String,String> message = new HashMap<>();
            message.put("id", product.getId());
            message.put("route","create");
            inventoryMessageSender.sendRollbackMessage(message);
        }
    }

    @RabbitListener(queues = "productUpdateQueue")
    public void handleProductUpdate(Product message) {
        // 处理商品更新
        System.out.println("up message: " + message.getName());
    }

    @RabbitListener(queues = "productDeleteQueue")
    public void handleProductDelete(Product message) {
        // 处理商品删除
        System.out.println("del message: " + message.getName());
    }
}