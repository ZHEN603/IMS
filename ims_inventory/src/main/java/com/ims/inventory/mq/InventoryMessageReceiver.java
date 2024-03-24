package com.ims.inventory.mq;
import com.ims.domain.product.Product;
import com.ims.inventory.service.InventoryService;
import com.ims.inventory.service.impl.InventoryServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InventoryMessageReceiver {

    @Resource
    InventoryService inventoryService;

    @Resource
    InventoryMessageSender inventoryMessageSender;

    @RabbitListener(queues = "productCreateQueue")
    public void handleProductCreate(Map<String, Object> map) {
        try{
            inventoryService.save(map);
        }catch (Exception e){
            System.out.println(e);
            Map<String,String> message = new HashMap<>();
            message.put("id", (String) map.get("productId"));
            message.put("route","create");
            inventoryMessageSender.sendRollbackMessage(message);
        }
    }

    @RabbitListener(queues = "productUpdateQueue")
    public void handleProductUpdate(Product product) {
        try{
            inventoryService.updateProductInfo(product);
        }catch (Exception e){
            System.out.println(e);
            Map<String,String> message = new HashMap<>();
            message.put("id", product.getId());
            message.put("route","update");
            inventoryMessageSender.sendRollbackMessage(message);
        }
    }

    @RabbitListener(queues = "productDeleteQueue")
    public void handleProductDelete(String id) {
        try{
            inventoryService.deleteByProductId(id);
        }catch (Exception e){
            System.out.println(e);
            Map<String,String> message = new HashMap<>();
            message.put("id", id);
            message.put("route","delete");
            inventoryMessageSender.sendRollbackMessage(message);
        }
    }
}