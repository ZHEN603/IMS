package com.ims.user.controller;

import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.common.exception.CommonException;
import com.ims.domain.user.Permission;
import com.ims.user.service.PermissionService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/user")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @PostMapping("/permission")
    public Result add(@RequestBody Permission permission) throws Exception {
        permissionService.save(permission);
        return new Result(ResultCode.SUCCESS);
    }

    @PutMapping("/permission/{id}")
    public Result update(@PathVariable(value = "id") String id , @RequestBody Permission permission) throws Exception {
        permission.setId(id);
        permissionService.update(permission);
        return new Result(ResultCode.SUCCESS);
    }

    @GetMapping("/permission/list")
    public Result findAll(@RequestParam() Map<String, Object> map){
        return new Result(ResultCode.SUCCESS , permissionService.findAll(map));
    }

    @GetMapping("/permission/{id}")
    public Result findById(@PathVariable(value = "id") String id) throws CommonException {
        return new Result(ResultCode.SUCCESS , permissionService.findById(id));
    }

    @DeleteMapping("/permission/{id}")
    public Result delete(@PathVariable(value = "id") String id) throws CommonException {
        permissionService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @PutMapping("/permission/updateState/{id}")
    public Result updateState(@PathVariable(value = "id") String id ,@RequestBody Map<String, Integer> body) {
        Permission permission = permissionService.findById(id);
        permission.setState(body.get("state"));
        permissionService.update(permission);
        return new Result(ResultCode.SUCCESS);
    }
}
