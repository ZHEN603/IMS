package com.ims.user.controller;

import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.common.exception.CommonException;
import com.ims.domain.company.Company;
import com.ims.domain.user.Permission;
import com.ims.user.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/user")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = "/permission" , method = RequestMethod.POST)
    public Result save(@RequestBody Permission permission) throws Exception {
        permissionService.save(permission);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/permission/{id}" , method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id") String id , @RequestBody Permission permission) throws Exception {
        permission.setId(id);
        permissionService.update(permission);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/permission" , method = RequestMethod.GET)
    public Result findAll(@RequestParam() Map map){
        return new Result(ResultCode.SUCCESS , permissionService.findAll(map));
    }

    @RequestMapping(value = "/permission/{id}" , method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id) throws CommonException {
        return new Result(ResultCode.SUCCESS , permissionService.findById(id));
    }

    @RequestMapping(value = "/permission/{id}" , method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "id") String id) throws CommonException {
        permissionService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
    @RequestMapping(value="/permission/{id}/state/{state}",method = RequestMethod.PUT)
    public Result updateState(@PathVariable(value = "id") String id ,@PathVariable(value = "state") Integer state) {
        Permission permission = permissionService.findById(id);
        permission.setState(state);
        permissionService.update(permission);
        return new Result(ResultCode.SUCCESS);
    }
}
