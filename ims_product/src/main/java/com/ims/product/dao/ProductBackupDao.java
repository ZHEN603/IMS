package com.ims.product.dao;

import com.ims.domain.product.ProductBackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductBackupDao extends JpaRepository<ProductBackup,String>, JpaSpecificationExecutor<ProductBackup> {
}
