package com.ims.product.dao;

import com.ims.domain.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryDao extends JpaRepository<Category,String>, JpaSpecificationExecutor<Category> {
}
