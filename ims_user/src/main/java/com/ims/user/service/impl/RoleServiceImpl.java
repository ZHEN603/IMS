package com.ims.user.service.impl;

import com.ims.common.utils.IdWorker;
import com.ims.domain.user.Permission;
import com.ims.domain.user.Role;
import com.ims.user.dao.RoleDao;
import com.ims.user.service.RoleService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private IdWorker idWorker;

    @Resource
    private RoleDao roleDao;

    @Resource
    private PermissionServiceImpl permissionService;

    @Override
    public void save(Role role){
        role.setId(idWorker.nextId() + "");
        roleDao.save(role);
    }
    @Override
    public void update(Role role){
        Set<Permission> permissions = roleDao.findById(role.getId()).get().getPermissions();
        role.setPermissions(permissions);
        roleDao.save(role);
    }
    @Override
    public Role findById(String id){
        return roleDao.findById(id).get();
    }
    @Override
    public List<Role> findAll(String companyId){
        return roleDao.findAll((Specification) (root, query, cb) -> cb.equal(root.get("companyId").as(String.class) , companyId));
    }
    @Override
    public void delete(String id){
        roleDao.deleteById(id);
    }
    @Override
    public Page<Role> findByPage(String companyId, int page, int size){
        return roleDao.findAll((Specification) (root, query, cb) -> cb.equal(root.get("companyId").as(String.class) , companyId), PageRequest.of(page-1 , size));
    }
    @Override
    public void assignPerms(String roleId, List<String> permIds) {
        Role role = roleDao.findById(roleId).get();
        Set<Permission> perms = new HashSet<>();
        for (String permId : permIds) {
            Permission perm = permissionService.findById(permId);
            perms.add(perm);
        }
        role.setPermissions(perms);
        roleDao.save(role);
    }
    @Override
    public List<String> findRolesByUserId(String userId){
        List<Role> roles = roleDao.findByUsersId(userId);
        return roles.stream().map(Role::getId).collect(Collectors.toList());
    }

}
