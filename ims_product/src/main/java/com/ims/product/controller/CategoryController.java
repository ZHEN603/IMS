package com.ims.product.controller;

import com.ims.common.controller.BaseController;
import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.domain.product.Category;
import com.ims.product.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping(value = "/product")
public class CategoryController extends BaseController {
    @Resource
    private CategoryService categoryService;

    @PostMapping("/category")
    public Result add(@RequestBody Category category) {
        category.setCompanyId(companyId);
        categoryService.save(category);
        return new Result(ResultCode.SUCCESS);
    }

    @GetMapping("/category/list")
    public Result findAll() {
        return new Result(ResultCode.SUCCESS,categoryService.findAll(companyId));
    }


    @GetMapping("/category/{id}")
    public Result findById(@PathVariable(value="id") String id) {
        return new Result(ResultCode.SUCCESS,categoryService.findById(id));
    }

    @PutMapping("/category/{id}")
    public Result update(@PathVariable(value="id") String id,@RequestBody Category category) {
        category.setId(id);
        categoryService.update(category);
        return new Result(ResultCode.SUCCESS);
    }

    @DeleteMapping("/category/{id}")
    public Result delete(@PathVariable(value="id") String id) {
        categoryService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @GetMapping("/category/child/{id}")
    public List<String> findAllChildIds(@PathVariable(value="id") String id) {
        List<String> ids = new ArrayList<>();
        categoryService.findAllChildIds(id, ids);
        return ids;
    }
}
