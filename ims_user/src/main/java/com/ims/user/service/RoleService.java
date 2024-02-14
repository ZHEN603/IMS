package com.ims.user.service;

import com.ims.common.service.BaseService;
import com.ims.common.utils.IdWorker;
import com.ims.domain.user.Permission;
import com.ims.domain.user.Role;
import com.ims.user.dao.PermissionDao;
import com.ims.user.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService extends BaseService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;


    public void save(Role role){
        role.setId(idWorker.nextId() + "");
        roleDao.save(role);
    }

    public void update(Role role){ roleDao.save(role); }

    public Role findById(String id){
        return roleDao.findById(id).get();
    }

    public List<Role> findAll(String companyId){
        return roleDao.findAll(getSpec(companyId));
    }

    public void delete(String id){
        roleDao.deleteById(id);
    }

    public Page<Role> findByPage(String companyId, int page, int size){
        return roleDao.findAll(getSpec(companyId) , PageRequest.of(page-1 , size));
    }

    public void assignPerms(String roleId, List<String> permIds) {
        Role role = roleDao.findById(roleId).get();
        Set<Permission> perms = new HashSet<>();
        for (String permId : permIds) {
            Permission perm = permissionDao.findById(permId).get();
            perms.add(perm);
        }
        role.setPermissions(perms);
        roleDao.save(role);
    }

    public List<String> findRolesByUserId(String userId){
        List<Role> roles = roleDao.findByUsersId(userId);
        return roles.stream().map(Role::getId).collect(Collectors.toList());
    }

}
