package com.ims.inventory.dao;

import com.ims.domain.inventory.Inventory;
import com.ims.domain.inventory.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderDao extends JpaRepository<Order,String> , JpaSpecificationExecutor<Order> {
}
