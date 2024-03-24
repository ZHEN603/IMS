package com.ims.product.mq;
import com.ims.product.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductMessageReceiver {

    @Resource
    private ProductService productService;

    @RabbitListener(queues = "productRollbackQueue")
    public void handleProductRollback(Map<String, String> map) {
        switch (map.get("route")){
            case "create":
                productService.deleteById(map.get("id"));
                break;
            case "delete", "update":
                productService.rollBack(map.get("id"));
                break;
            default:
                break;
        }
    }

}