package com.ims.user.dao;

import com.ims.domain.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface RoleDao extends JpaRepository<Role,String>, JpaSpecificationExecutor<Role> {
    List<Role> findByUsersId(String userId);
}
