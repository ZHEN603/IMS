package com.ims.company.controller;

import com.ims.common.entity.PageResult;
import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.company.service.CompanyService;
import com.ims.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//Cross domain
@CrossOrigin
@RestController
@RequestMapping(value="/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;


    @RequestMapping(value="",method = RequestMethod.POST)
    public Result save(@RequestBody Company company) {
        companyService.save(company);
        return new Result(ResultCode.SUCCESS);
    }

    //Update
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value="id") String id, @RequestBody Company company ) {
        company.setId(id);
        companyService.update(company);
        return new Result(ResultCode.SUCCESS);
    }

    //Delete
    @RequestMapping(value="/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value="id") String id) {
        companyService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    //Find company by ID
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public Result findCompanyById(@PathVariable(value="id") String id){
        return new Result(ResultCode.SUCCESS, companyService.findById(id));
    }

    //Find all companies
    @RequestMapping(value="",method = RequestMethod.GET)
    public Result findAll() {
        return new Result(ResultCode.SUCCESS, companyService.findAll());
    }

    //Find all companies by page
    @RequestMapping(value = "/list" , method = RequestMethod.GET)
    public Result findAll(int page, int pagesize, String keyword){
        Page<Company> pageUser = companyService.findAll(page, pagesize, keyword);
        PageResult<Company> pageResult = new PageResult<>(pageUser.getTotalElements(),pageUser.getContent());
        return new Result(ResultCode.SUCCESS , pageResult);
    }

    //Update company's state
    @RequestMapping(value="/{id}/state/{state}",method = RequestMethod.PUT)
    public Result updateState(@PathVariable(value = "id") String id ,@PathVariable(value = "state") Integer state) {
        Company company = companyService.findById(id);
        company.setState(state);
        companyService.update(company);
        return new Result(ResultCode.SUCCESS);
    }
}