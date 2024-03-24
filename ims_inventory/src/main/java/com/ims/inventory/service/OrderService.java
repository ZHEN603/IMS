package com.ims.inventory.service;

import com.ims.common.exception.CommonException;
import com.ims.domain.inventory.Order;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface OrderService{

    public void save(Order order) throws CommonException;

    public void update(Order order);

    public void deleteById(String id);

    public Page<Order> findByPage(Map<String,Object> map, int page, int size);

    public Order findById(String id);

    public List<Order> findAll(String companyId);

    public void completeOrder(String id) throws CommonException;

}