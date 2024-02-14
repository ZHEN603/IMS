package com.ims.inventory.dao;

import com.ims.domain.inventory.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface InventoryDao extends JpaRepository<Inventory,String> , JpaSpecificationExecutor<Inventory> {
    List<Inventory> findByProductId(String id);
}
