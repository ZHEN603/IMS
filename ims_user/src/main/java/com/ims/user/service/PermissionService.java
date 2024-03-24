package com.ims.user.service;

import com.ims.domain.user.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionService {

    public void save(Permission permission);

    public void update(Permission permission);

    public Permission findById(String id);

    public List<Permission> findAll(Map<String,Object> map);

    public void deleteById(String id);
}

