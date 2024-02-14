package com.ims.supplier.controller;

import com.ims.common.controller.BaseController;
import com.ims.common.entity.PageResult;
import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.supplier.service.SupplierService;
import com.ims.domain.supplier.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//Cross domain
@CrossOrigin
@RestController
@RequestMapping(value="/supplier")
public class SupplierController extends BaseController {
    @Autowired
    private SupplierService supplierService;


    @RequestMapping(value="",method = RequestMethod.POST)
    public Result save(@RequestBody Supplier supplier) {
        supplier.setCompanyId(companyId);
        supplierService.save(supplier);
        return new Result(ResultCode.SUCCESS);
    }

    //Update
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value="id") String id, @RequestBody Supplier supplier ) {
        supplier.setId(id);
        supplierService.update(supplier);
        return new Result(ResultCode.SUCCESS);
    }

    //Delete
    @RequestMapping(value="/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value="id") String id) {
        supplierService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    //Find supplier by ID
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public Result findSupplierById(@PathVariable(value="id") String id){
        return new Result(ResultCode.SUCCESS, supplierService.findById(id));
    }

    //Find all
    @RequestMapping(value="",method = RequestMethod.GET)
    public Result findAll() {
        return new Result(ResultCode.SUCCESS, supplierService.findAll(companyId));
    }

    //Find by page
    @RequestMapping(value = "/list" , method = RequestMethod.GET)
    public Result findAll(int page, int pagesize, @RequestParam() Map map){
        map.put("companyId", companyId);
        Page<Supplier> pageSupplier = supplierService.findAll(page, pagesize, map);
        PageResult<Supplier> pageResult = new PageResult<>(pageSupplier.getTotalElements(),pageSupplier.getContent());
        return new Result(ResultCode.SUCCESS , pageResult);
    }

}