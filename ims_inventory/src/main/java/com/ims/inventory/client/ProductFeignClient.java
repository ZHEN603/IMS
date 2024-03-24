package com.ims.inventory.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ims-product", url = "http://ims-product:9003")
public interface ProductFeignClient {
    @GetMapping("/product/category/child/{id}")
    public List<String> findAllChildIds(@PathVariable(value="id") String id);
}
