package com.ims.inventory.controller;

import com.ims.common.controller.BaseController;
import com.ims.common.entity.PageResult;
import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.domain.inventory.Order;
import com.ims.domain.product.Product;
import com.ims.inventory.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value="/inventory")
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(value="/order",method = RequestMethod.POST)
    public Result save(@RequestBody Order order) {
        order.setCompanyId(companyId);
        orderService.save(order);
        return new Result(ResultCode.SUCCESS);
    }

    //Update
    @RequestMapping(value = "/order/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value="id") String id, @RequestBody Order order ) {
        order.setId(id);
        orderService.update(order);
        return new Result(ResultCode.SUCCESS);
    }

    //Delete
    @RequestMapping(value="/order/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value="id") String id) {
        orderService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    //Find order by ID
    @RequestMapping(value="/order/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value="id") String id){
        return new Result(ResultCode.SUCCESS, orderService.findById(id));
    }

    //Update product's state
    @RequestMapping(value="/order/{id}/state/{state}",method = RequestMethod.PUT)
    public Result updateState(@PathVariable(value = "id") String id ,@PathVariable(value = "state") Integer state) {
        Order order = orderService.findById(id);
        order.setState(state);
        orderService.update(order);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value="/order/list",method = RequestMethod.GET)
    public Result findAll(int page, int pagesize, @RequestParam() Map map) {
        map.put("companyId", companyId);
        Page<Product> productPage = orderService.findByPage(map, page, pagesize);
        PageResult<Product> pageResult = new PageResult<>(productPage.getTotalElements(),productPage.getContent());
        return new Result(ResultCode.SUCCESS,pageResult);
    }

    @RequestMapping(value="/order",method = RequestMethod.GET)
    public Result findAll() {
        return new Result(ResultCode.SUCCESS,orderService.findAll(companyId));
    }

    @RequestMapping(value="/order/complete/{id}",method = RequestMethod.PUT)
    public Result complete(@PathVariable(value = "id") String id) {
        orderService.complete(id);
        return new Result(ResultCode.SUCCESS);
    }

}