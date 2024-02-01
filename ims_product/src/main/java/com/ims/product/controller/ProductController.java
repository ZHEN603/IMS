package com.ims.product.controller;

import com.ims.common.controller.BaseController;
import com.ims.common.entity.PageResult;
import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.domain.inventory.Inventory;
import com.ims.domain.product.Product;
import com.ims.domain.user.Role;
import com.ims.domain.user.User;
import com.ims.product.ProductApplication;
import com.ims.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/product")
public class ProductController extends BaseController {
    @Autowired
    private ProductService productService;

    @RequestMapping(value="",method = RequestMethod.POST)
    public Result save(@RequestBody Map<String, Object> map) {
        map.put("companyId", companyId);
        productService.save(map);
        return new Result(ResultCode.SUCCESS);
    }
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public Result findAll(int page, int pagesize, @RequestParam() Map map) {
        map.put("companyId", companyId);
        Page<Product> productPage = productService.findByPage(map, page, pagesize);
        PageResult<Product> pageResult = new PageResult<>(productPage.getTotalElements(),productPage.getContent());
        return new Result(ResultCode.SUCCESS,pageResult);
    }

    @RequestMapping(value="",method = RequestMethod.GET)
    public Result findAll() {
        return new Result(ResultCode.SUCCESS,productService.findAll(companyId));
    }

    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value="id") String id) {
        return new Result(ResultCode.SUCCESS,productService.findById(id));
    }

    @RequestMapping(value="/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value="id") String id,@RequestBody Product product) {
        product.setId(id);
        productService.update(product);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value="/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value="id") String id) {
        productService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value="/ids",method = RequestMethod.POST)
    public Result findByIds(@RequestBody List<String> ids, String companyId) {
        return new Result(ResultCode.SUCCESS,productService.findByIds(ids, companyId));
    }

}
