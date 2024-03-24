package com.ims.inventory.controller;

import com.ims.common.controller.BaseController;
import com.ims.common.entity.PageResult;
import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.domain.inventory.Inventory;
import com.ims.inventory.service.InventoryService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value="/inventory")
public class InventoryController extends BaseController {
    @Resource
    private InventoryService inventoryService;


    //Update
    @PutMapping("/{id}")
    public Result update(@PathVariable(value="id") String id, @RequestBody Inventory inventory ) {
        inventory.setId(id);
        inventoryService.update(inventory);
        return new Result(ResultCode.SUCCESS);
    }


    //Find inventory by ID
    @GetMapping("/{id}")
    public Result findById(@PathVariable(value="id") String id){
        return new Result(ResultCode.SUCCESS, inventoryService.findById(id));
    }

    //Update product's state
    @PutMapping("/updateState/{id}")
    public Result updateState(@PathVariable(value = "id") String id ,@RequestBody Map<String, Integer> body) {
        inventoryService.updateState(id, body.get("state"));
        return new Result(ResultCode.SUCCESS);
    }

    @GetMapping("/page")
    public Result findByPage(int page, int pagesize, @RequestParam() Map<String, Object> map) {
        map.put("companyId", companyId);
        Page<Inventory> inventoryPage = inventoryService.findByPage(map, page, pagesize);
        PageResult<Inventory> pageResult = new PageResult<>(inventoryPage.getTotalElements(),inventoryPage.getContent());
        return new Result(ResultCode.SUCCESS,pageResult);
    }

    @GetMapping("/list")
    public Result findAll(@RequestParam() Map<String, Object> map) {
        map.put("companyId", companyId);
        List<Inventory> products = inventoryService.findAll(map);
        return new Result(ResultCode.SUCCESS,products);
    }

    @PostMapping("/ids")
    public Result findByIds(@RequestBody List<String> ids) {
        System.out.println(ids);
        return new Result(ResultCode.SUCCESS,inventoryService.findByIds(ids, companyId));
    }
}