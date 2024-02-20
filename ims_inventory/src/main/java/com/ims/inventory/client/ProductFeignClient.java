package com.ims.inventory.client;

import com.ims.common.entity.Result;
import com.ims.domain.product.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

//@FeignClient("ims-product")
@FeignClient(name = "ims-product", url = "http://ims-product:9003")
public interface ProductFeignClient {
    @RequestMapping(value="product/category/child/{id}",method = RequestMethod.GET)
    public List<String> findAllChildIds(@PathVariable(value="id") String id, @PathVariable(value="companyId") String companyId);
}
