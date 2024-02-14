package com.ims.supplier.dao;

import com.ims.domain.supplier.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SupplierDao extends JpaRepository<Supplier,String> , JpaSpecificationExecutor<Supplier> {
}
