package com.ims.product.client;

import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.domain.inventory.Inventory;
import com.ims.domain.product.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@FeignClient("ims-inventory")
@FeignClient(name = "ims-inventory", url = "http://ims-inventory:9004")
public interface InventoryFeignClient {
    @RequestMapping(value = "/inventory", method = RequestMethod.POST)
    Result save(@RequestBody Map<String, Object> map);
//    Result save(@RequestParam() Map map);
}
