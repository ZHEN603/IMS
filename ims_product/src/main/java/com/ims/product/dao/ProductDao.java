package com.ims.product.dao;

import com.ims.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductDao extends JpaRepository<Product,String>, JpaSpecificationExecutor<Product> {
}
