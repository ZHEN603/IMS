package com.ims.inventory.controller;

import com.ims.common.controller.BaseController;
import com.ims.common.entity.PageResult;
import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.domain.inventory.Inventory;
import com.ims.domain.product.Product;
import com.ims.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value="/inventory")
public class InventoryController extends BaseController {
    @Autowired
    private InventoryService inventoryService;

    @RequestMapping(value="",method = RequestMethod.POST)
    public Result save(@RequestBody Map<String, Object> map) {
        inventoryService.save(map);
        return new Result(ResultCode.SUCCESS);
    }

    //Update
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value="id") String id, @RequestBody Inventory inventory ) {
        inventory.setId(id);
        inventoryService.update(inventory);
        return new Result(ResultCode.SUCCESS);
    }

    //Delete
    @RequestMapping(value="/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value="id") String id) {
        inventoryService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    //Find inventory by ID
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value="id") String id){
        return new Result(ResultCode.SUCCESS, inventoryService.findById(id));
    }

    //Update product's state
    @RequestMapping(value="/{id}/state/{state}",method = RequestMethod.PUT)
    public Result updateState(@PathVariable(value = "id") String id ,@PathVariable(value = "state") Integer state) {
        Inventory inventory = inventoryService.findById(id);
        inventory.setState(state);
        inventoryService.update(inventory);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public Result findAll(int page, int pagesize, @RequestParam() Map map) {
        map.put("companyId", companyId);
        Page<Product> productPage = inventoryService.findByPage(map, page, pagesize);
        PageResult<Product> pageResult = new PageResult<>(productPage.getTotalElements(),productPage.getContent());
        return new Result(ResultCode.SUCCESS,pageResult);
    }

    @RequestMapping(value="",method = RequestMethod.GET)
    public Result findAll(@RequestParam() Map map) {
        map.put("companyId", companyId);
        List<Inventory> products = inventoryService.findAll(map);
        return new Result(ResultCode.SUCCESS,products);
    }

    @RequestMapping(value="/ids",method = RequestMethod.POST)
    public Result findByIds(@RequestBody List<String> ids) {
        System.out.println(ids);
        return new Result(ResultCode.SUCCESS,inventoryService.findByIds(ids, companyId));
    }
}