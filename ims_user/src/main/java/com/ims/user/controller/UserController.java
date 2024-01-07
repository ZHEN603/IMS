package com.ims.user.controller;


import com.ims.common.controller.BaseController;
import com.ims.common.entity.PageResult;
import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.common.exception.CommonException;
import com.ims.common.utils.JwtUtils;
import com.ims.common.utils.PermissionConstants;
import com.ims.domain.company.Company;
import com.ims.domain.user.Permission;
import com.ims.domain.user.Role;
import com.ims.domain.user.User;

import com.ims.domain.user.response.ProfileResult;
import com.ims.user.client.CompanyFeignClient;
import com.ims.user.service.PermissionService;
import com.ims.user.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2020/01/09
 **/
//解决跨域
//@CrossOrigin
@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CompanyFeignClient companyFeignClient;

    //测试通过系统微服务调用企业微服务方法
    @RequestMapping(value = "/test")
    public Result testFeign() {
        return companyFeignClient.findAll();
    }


    /**
     * 分配角色
     */
    @RequestMapping(value = "/user/assignRoles" , method = RequestMethod.PUT)
    public Result save(@RequestBody Map<String,Object> map){

        //获取被分配的用户id
        String userId = (String) map.get("id");
        //获取到角色的id列表
        List<String> roleIds = (List<String>) map.get("roleIds");
        //调用service完成角色分配
        userService.assignRoles(userId , roleIds);

        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 保存
     * @return
     */
    @RequestMapping(value = "/user" , method = RequestMethod.POST)
    public Result save(@RequestBody User user){
        //设置保存的用户id
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        //调用service完成保存用户
        userService.save(user);
        //构造返回结果
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询用户列表
     * @return
     */
    @RequestMapping(value = "/user" , method = RequestMethod.GET)
    public Result findAll(int page, int size, @RequestParam() Map map){

        //获取当前的企业id
        map.put("companyId" , companyId);
        Page<User> pageUser = userService.findAll(map, page, size);
        //构造返回结果
        PageResult<User> pageResult = new PageResult<>(pageUser.getTotalElements(),pageUser.getContent());
        return new Result(ResultCode.SUCCESS , pageResult);
    }

    /**
     * 根据Id查询
     */
    @RequestMapping(value = "/user/{id}" , method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id){
        //添加roleIds(用户已经具有的角色id数组)
        User user = userService.findById(id);
        return new Result(ResultCode.SUCCESS , user);
    }

    /**
     * 修改User
     */
    @RequestMapping(value = "/user/{id}" , method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id") String id , @RequestBody User user){
        //调用Service更新
        userService.update(id , user);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据Id删除
     */
    @RequestMapping(value = "/user/{id}" , method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "id") String id){
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value="/login",method = RequestMethod.POST)
    public Result login(@RequestBody Map<String,String> loginMap) {
        String id = loginMap.get("id");
        String password = loginMap.get("password");
        User user = userService.findById(id);
        //登录失败
        if(user == null || !user.getPassword().equals(password)) {
            return new Result(ResultCode.LOGIN_ERROR);
        }else {
            //登录成功
            //api权限字符串
            StringBuilder sb = new StringBuilder();
            //获取到所有的可访问API权限
            for (Role role : user.getRoles()) {
                for (Permission perm : role.getPermissions()) {
                    if(perm.getType() == PermissionConstants.PY_API) {
                        sb.append(perm.getCode()).append(",");
                    }
                }
            }
            Map<String,Object> map = new HashMap<>();
            map.put("apis",sb.toString());//可访问的api权限字符串
            map.put("companyId",user.getCompanyId());
            map.put("companyName",user.getCompanyName());
            String token = jwtUtils.createJwt(user.getId(), user.getUsername(), map);
            return new Result(ResultCode.SUCCESS,token);

        }
    }


    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public Result profile(HttpServletRequest request) throws Exception {

        String userid = claims.getId();
        //获取用户信息
        User user = userService.findById(userid);
        //根据不同的用户级别获取用户权限
        ProfileResult result = null;
        if("user".equals(user.getLevel())) {
            result = new ProfileResult(user);
        }else {
            Map map = new HashMap();
            if("coAdmin".equals(user.getLevel())) {
                map.put("enVisible","1");
            }
            List<Permission> list = permissionService.findAll(map);
            result = new ProfileResult(user,list);
        }
        return new Result(ResultCode.SUCCESS,result);


    }

}
