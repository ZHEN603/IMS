package com.ims.product.controller;

import com.ims.common.controller.BaseController;
import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.domain.product.Category;
import com.ims.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping(value = "/product")
public class CategoryController extends BaseController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value="/category",method = RequestMethod.POST)
    public Result save(@RequestBody Category category) {
        category.setCompanyId(companyId);
        categoryService.save(category);
        return new Result(ResultCode.SUCCESS);
    }
    @RequestMapping(value="/category",method = RequestMethod.GET)
    public Result findAll() {
        return new Result(ResultCode.SUCCESS,categoryService.findAll(companyId));
    }

    @RequestMapping(value="/category/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value="id") String id) {
        return new Result(ResultCode.SUCCESS,categoryService.findById(id));
    }

    @RequestMapping(value="/category/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value="id") String id,@RequestBody Category category) {
        category.setId(id);
        categoryService.update(category);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value="/category/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value="id") String id) {
        categoryService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value="/category/child/{id}",method = RequestMethod.GET)
    public List<String> findAllChildIds(@PathVariable(value="id") String id) {
        List<String> ids = new ArrayList<>();
        categoryService.findAllChildIds(id, ids);
        return ids;
    }
}
