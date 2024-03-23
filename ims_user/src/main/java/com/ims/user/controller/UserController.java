package com.ims.user.controller;


import com.ims.common.controller.BaseController;
import com.ims.common.entity.PageResult;
import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.common.utils.JwtUtils;
import com.ims.domain.user.Permission;
import com.ims.domain.user.Role;
import com.ims.domain.user.User;

import com.ims.domain.user.response.ProfileResult;
import com.ims.user.service.PermissionService;
import com.ims.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private JwtUtils jwtUtils;

    //Assign roles to users
    @RequestMapping(value = "/assignRoles" , method = RequestMethod.PUT)
    public Result save(@RequestBody Map<String,Object> map){
        String userId = (String) map.get("id");
        List<String> roleIds = (List<String>) map.get("roleIds");
        userService.assignRoles(userId , roleIds);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "" , method = RequestMethod.POST)
    public Result save(@RequestBody User user){
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        userService.save(user);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "" , method = RequestMethod.GET)
    public Result findAll(int page, int pagesize, @RequestParam() Map map){
        map.put("companyId" , companyId);
        Page<User> pageUser = userService.findAll(map, page, pagesize);
        PageResult<User> pageResult = new PageResult<>(pageUser.getTotalElements(),pageUser.getContent());
        return new Result(ResultCode.SUCCESS , pageResult);
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id){
        return new Result(ResultCode.SUCCESS , userService.findById(id));
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id") String id , @RequestBody User user){
        user.setId(id);
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "id") String id){
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value="/login",method = RequestMethod.POST)
    public Result login(@RequestBody Map<String,String> loginMap) {
        String id = loginMap.get("id");
        String password = loginMap.get("password");
        User user = userService.findById(id);
        //failed
        if(user == null || !user.getPassword().equals(password) || user.getState().equals(0)) {
            return new Result(ResultCode.LOGIN_ERROR);
        }else {
            //success
            StringBuilder sb = new StringBuilder();
            for (Role role : user.getRoles()) {
                for (Permission perm : role.getPermissions()) {
                    if(perm.getType() == 3) {
                        sb.append(perm.getCode()).append(",");
                    }
                }
            }
            Map<String,Object> map = new HashMap<>();
            map.put("apis",sb.toString());
            map.put("companyId",user.getCompanyId());
            map.put("companyName",user.getCompanyName());
            String token = jwtUtils.createJwt(user.getId(), user.getName(), map);
            return new Result(ResultCode.SUCCESS,token);
        }
    }


    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public Result profile(HttpServletRequest request) throws Exception {

        String userid = claims.getId();
        User user = userService.findById(userid);
        ProfileResult result = null;
        if("user".equals(user.getLevel())) {
            result = new ProfileResult(user);
        }else {
            Map map = new HashMap();
            if("coAdmin".equals(user.getLevel())) {
                map.put("state",1);
            }
            List<Permission> list = permissionService.findAll(map);
            result = new ProfileResult(user,list);
        }
        return new Result(ResultCode.SUCCESS,result);
    }

    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    Result saveAdmin(@RequestParam("id") String id, @RequestParam("companyName") String companyName, @RequestParam("companyId") String companyId){
        userService.saveAdmin(id, companyName, companyId);
        return new Result(ResultCode.SERVER_ERROR);
    }
}
