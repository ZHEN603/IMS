package com.ims.user.controller;

import com.ims.common.controller.BaseController;
import com.ims.common.entity.PageResult;
import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.domain.user.Role;
import com.ims.domain.user.response.RoleResult;
import com.ims.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2020/01/15
 **/

//解决跨域
@CrossOrigin
@RestController
@RequestMapping(value = "/user")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public Result add(@RequestBody Role role) throws Exception {
        role.setCompanyId(companyId);
        roleService.save(role);
        return Result.SUCCESS();
    }

    @RequestMapping(value = "/role/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable(name = "id") String id, @RequestBody Role role) throws Exception {
        role.setId(id);
        roleService.update(role);
        return Result.SUCCESS();
    }

    @RequestMapping(value = "/role/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable(name = "id") String id) throws Exception {
        roleService.delete(id);
        return Result.SUCCESS();
    }

    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable(name = "id") String id) throws Exception {
        Role role = roleService.findById(id);
        RoleResult roleResult = new RoleResult(role);
        return new Result(ResultCode.SUCCESS,roleResult);
    }

    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public Result findByPage(int page,int pagesize) throws Exception {
        Page<Role> searchPage = roleService.findByPage(companyId, page, pagesize);
        PageResult<Role> pr = new PageResult(searchPage.getTotalElements(),searchPage.getContent());
        return new Result(ResultCode.SUCCESS,pr);
    }

    @RequestMapping(value="/role/list" ,method=RequestMethod.GET)
    public Result findAll() throws Exception {
        return new Result(ResultCode.SUCCESS,roleService.findAll(companyId));
    }

    @RequestMapping(value = "/role/userId/{id}", method = RequestMethod.GET)
    public Result findRolesByUserId(@PathVariable(name = "id") String id) {
        return new Result(ResultCode.SUCCESS, roleService.findRolesByUserId(id));
    }

    @RequestMapping(value = "/role/assignPrem" , method = RequestMethod.PUT)
    public Result save(@RequestBody Map<String,Object> map){
        String roleId = (String) map.get("id");
        List<String> permIds = (List<String>) map.get("permIds");
        roleService.assignPerms(roleId , permIds);
        return new Result(ResultCode.SUCCESS);
    }
}
