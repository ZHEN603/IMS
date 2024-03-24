package com.ims.company.controller;

import com.ims.common.entity.PageResult;
import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.company.service.CompanyService;
import com.ims.domain.company.Company;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping(value="/company")
public class CompanyController {

    @Resource
    private CompanyService companyService;


    @PostMapping("")
    public Result add(@RequestBody Company company) {
        companyService.save(company);
        return new Result(ResultCode.SUCCESS);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable(value="id") String id, @RequestBody Company company ) {
        company.setId(id);
        companyService.update(company);
        return new Result(ResultCode.SUCCESS);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value="id") String id) {
        companyService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    //Find company by ID
    @GetMapping("/{id}")
    public Result findById(@PathVariable(value="id") String id){
        return new Result(ResultCode.SUCCESS, companyService.findById(id));
    }

    //Find all companies
    @GetMapping("/list")
    public Result findAll() {
        return new Result(ResultCode.SUCCESS, companyService.findTypes());
    }

    //Find all companies by page
    @GetMapping("/page")
    public Result findByPage(int page, int pagesize, String keyword){
        Page<Company> pageUser = companyService.findByPage(page, pagesize, keyword);
        PageResult<Company> pageResult = new PageResult<>(pageUser.getTotalElements(),pageUser.getContent());
        return new Result(ResultCode.SUCCESS , pageResult);
    }

    //Update company's state
//    @RequestMapping(value="/{id}/state/{state}",method = RequestMethod.PUT)
    @PutMapping("/updateState/{id}")
    public Result updateState(@PathVariable(value = "id") String id ,@RequestBody Map<String, Integer> body) {
        Company company = companyService.findById(id);
        company.setState(body.get("state"));
        companyService.update(company);
        return new Result(ResultCode.SUCCESS);
    }
}