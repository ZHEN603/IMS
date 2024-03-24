package com.ims.user.service;

import com.ims.domain.user.Role;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleService{

    public void save(Role role);

    public void update(Role role);

    public Role findById(String id);

    public List<Role> findAll(String companyId);

    public void delete(String id);

    public Page<Role> findByPage(String companyId, int page, int size);

    public void assignPerms(String roleId, List<String> permIds);

    public List<String> findRolesByUserId(String userId);

}
