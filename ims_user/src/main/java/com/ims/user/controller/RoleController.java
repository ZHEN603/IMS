package com.ims.user.controller;

import com.ims.common.controller.BaseController;
import com.ims.common.entity.PageResult;
import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.domain.user.Role;
import com.ims.domain.user.response.RoleResult;
import com.ims.user.service.RoleService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


//解决跨域
@CrossOrigin
@RestController
@RequestMapping(value = "/user")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    @PostMapping("/role")
    public Result add(@RequestBody Role role){
        role.setCompanyId(companyId);
        roleService.save(role);
        return Result.SUCCESS();
    }

    @PutMapping("/role/{id}")
    public Result update(@PathVariable(name = "id") String id, @RequestBody Role role) throws Exception {
        role.setId(id);
        roleService.update(role);
        return Result.SUCCESS();
    }

    @DeleteMapping("/role/{id}")
    public Result delete(@PathVariable(name = "id") String id) throws Exception {
        roleService.delete(id);
        return Result.SUCCESS();
    }

    @GetMapping("/role/{id}")
    public Result findById(@PathVariable(name = "id") String id) throws Exception {
        Role role = roleService.findById(id);
        RoleResult roleResult = new RoleResult(role);
        return new Result(ResultCode.SUCCESS,roleResult);
    }

    @GetMapping("/role/page")
    public Result findByPage(int page,int pagesize) {
        Page<Role> searchPage = roleService.findByPage(companyId, page, pagesize);
        PageResult<Role> pr = new PageResult(searchPage.getTotalElements(),searchPage.getContent());
        return new Result(ResultCode.SUCCESS,pr);
    }

    @GetMapping("/role/list")
    public Result findAll() {
        return new Result(ResultCode.SUCCESS,roleService.findAll(companyId));
    }

    @GetMapping("/role/userId/{id}")
    public Result findByUserId(@PathVariable(name = "id") String id) {
        return new Result(ResultCode.SUCCESS, roleService.findRolesByUserId(id));
    }

    @PutMapping("/role/assignPerm")
    public Result assignPerm(@RequestBody Map<String,Object> map){
        String roleId = (String) map.get("id");
        List<String> permIds = (List<String>) map.get("permIds");
        roleService.assignPerms(roleId , permIds);
        return new Result(ResultCode.SUCCESS);
    }
}
