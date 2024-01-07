package com.ims.user.dao;

import com.ims.domain.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface RoleDao extends JpaRepository<Role,String>, JpaSpecificationExecutor<Role> {
}
