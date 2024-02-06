package com.ims.user.dao;

import com.ims.domain.user.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PermissionDao extends JpaRepository<Permission, String>, JpaSpecificationExecutor<Permission> {
}