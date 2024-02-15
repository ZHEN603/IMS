package com.ims.inventory.dao;

import com.ims.domain.inventory.Inventory;
import com.ims.domain.inventory.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderDetailDao extends JpaRepository<OrderDetail,String> , JpaSpecificationExecutor<OrderDetail> {
}
