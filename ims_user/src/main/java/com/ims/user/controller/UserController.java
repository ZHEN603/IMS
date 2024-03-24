package com.ims.user.controller;


import com.ims.common.controller.BaseController;
import com.ims.common.entity.PageResult;
import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.common.exception.CommonException;
import com.ims.domain.user.User;

import com.ims.domain.user.response.UserResult;
import com.ims.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;


    //Assign roles to users
    @PutMapping("/assignRoles")
    public Result assignRoles(@RequestBody Map<String,Object> map){
        String userId = (String) map.get("id");
        List<String> roleIds = (List<String>) map.get("roleIds");
        userService.assignRoles(userId , roleIds);
        return new Result(ResultCode.SUCCESS);
    }

    @PostMapping("")
    public Result add(@RequestBody User user) throws CommonException {
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        userService.save(user);
        return new Result(ResultCode.SUCCESS);
    }

    @GetMapping("/page")
    public Result findByPage(int page, int pagesize, @RequestParam() Map<String, Object> map){
        map.put("companyId" , companyId);
        Page<UserResult> pageUser = userService.findByPage(page, pagesize, map);
        PageResult<UserResult> pageResult = new PageResult<>(pageUser.getTotalElements(),pageUser.getContent());
        return new Result(ResultCode.SUCCESS , pageResult);
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable(value = "id") String id){
        return new Result(ResultCode.SUCCESS , userService.findById(id));
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable(value = "id") String id , @RequestBody User user) throws CommonException {
        user.setId(id);
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value = "id") String id){
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @PostMapping("/login")
    public Result login(@RequestBody Map<String,String> loginMap) throws CommonException {
        String token = userService.login(loginMap);
        if (token==null) {
            return new Result(ResultCode.LOGIN_ERROR);
        }
        if (loginMap.get("password").equals("password")){
            return new Result(ResultCode.PASSWORD,token);
        }
        return new Result(ResultCode.SUCCESS,token);
    }


    @PostMapping("/profile")
    public Result profile() {
//        String userId = claims.getId();
        return new Result(ResultCode.SUCCESS, userService.profile(userId));
    }

}
