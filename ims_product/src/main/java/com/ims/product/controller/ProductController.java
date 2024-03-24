package com.ims.product.controller;

import com.ims.common.controller.BaseController;
import com.ims.common.entity.PageResult;
import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.domain.product.Product;
import com.ims.product.service.impl.ProductServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/product")
public class ProductController extends BaseController {
    @Resource
    private ProductServiceImpl productService;

    @PostMapping("")
    public Result add(@RequestBody Map<String, Object> map) {
        map.put("companyId", companyId);
        productService.save(map);
        return new Result(ResultCode.SUCCESS);
    }
    @GetMapping("/page")
    public Result findByPage(int page, int pagesize, @RequestParam() Map map) {
        map.put("companyId", companyId);
        Page<Product> productPage = productService.findByPage(map, page, pagesize);
        PageResult<Product> pageResult = new PageResult<>(productPage.getTotalElements(),productPage.getContent());
        return new Result(ResultCode.SUCCESS,pageResult);
    }

    @GetMapping("/list")
    public Result findAll() {
        return new Result(ResultCode.SUCCESS,productService.findAll(companyId));
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable(value="id") String id) {
        return new Result(ResultCode.SUCCESS,productService.findById(id));
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable(value="id") String id,@RequestBody Product product) {
        product.setId(id);
        product.setCompanyId(companyId);
        productService.update(product);
        return new Result(ResultCode.SUCCESS);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value="id") String id) {
        productService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @PostMapping("/ids")
    public Result findByIds(@RequestBody List<String> ids) {
        return new Result(ResultCode.SUCCESS,productService.findByIds(ids, companyId));
    }

}
