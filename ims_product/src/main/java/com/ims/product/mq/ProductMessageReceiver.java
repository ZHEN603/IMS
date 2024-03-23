package com.ims.product.mq;
import com.ims.domain.product.Product;
import com.ims.product.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductMessageReceiver {

    @Autowired
    ProductService productService;

    @RabbitListener(queues = "productRollbackQueue")
    public void handleProductCreate(Map<String, String> map) {
        switch (map.get("route")){
            case "create":
                System.out.println("create message: " + map.get("id"));
                productService.deleteById(map.get("id"));
                break;
            case "delete", "update":
                System.out.println("delete message: " + map.get("id"));
                break;
            default:
                break;
        }
    }

}