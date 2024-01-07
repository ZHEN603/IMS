package com.ims.user.dao;

import com.ims.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDao extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {
}
