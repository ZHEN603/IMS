package com.ims.inventory.controller;

import com.ims.common.controller.BaseController;
import com.ims.common.entity.PageResult;
import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.common.exception.CommonException;
import com.ims.domain.inventory.Order;
import com.ims.domain.product.Product;
import com.ims.inventory.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value="/inventory")
public class OrderController extends BaseController {
    @Resource
    private OrderService orderService;

    @PostMapping("/order")
    public Result save(@RequestBody Order order) throws CommonException {
        order.setCompanyId(companyId);
        orderService.save(order);
        return new Result(ResultCode.SUCCESS);
    }

    //Update
    @PutMapping("/order/{id}")
    public Result update(@PathVariable(value="id") String id, @RequestBody Order order ) {
        order.setId(id);
        orderService.update(order);
        return new Result(ResultCode.SUCCESS);
    }

    //Delete
    @DeleteMapping("/order/{id}")
    public Result delete(@PathVariable(value="id") String id) {
        orderService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    //Find order by ID
    @GetMapping("/order/{id}")
    public Result findById(@PathVariable(value="id") String id){
        return new Result(ResultCode.SUCCESS, orderService.findById(id));
    }

    //Update product's state
    @PutMapping("/order/updateState/{id}")
    public Result updateState(@PathVariable(value = "id") String id ,@RequestBody Map<String, Integer> map) {
        Order order = orderService.findById(id);
        order.setState(map.get("state"));
        orderService.update(order);
        return new Result(ResultCode.SUCCESS);
    }

    @GetMapping("/order/page")
    public Result findByPage(int page, int pagesize, @RequestParam() Map map) {
        map.put("companyId", companyId);
        Page<Product> productPage = orderService.findByPage(map, page, pagesize);
        PageResult<Product> pageResult = new PageResult<>(productPage.getTotalElements(),productPage.getContent());
        return new Result(ResultCode.SUCCESS,pageResult);
    }

    @GetMapping("/order/list")
    public Result findAll() {
        return new Result(ResultCode.SUCCESS,orderService.findAll(companyId));
    }

    @PutMapping("/order/complete/{id}")
    public Result completeOrder(@PathVariable(value = "id") String id) throws CommonException {
        orderService.completeOrder(id);
        return new Result(ResultCode.SUCCESS);
    }

}