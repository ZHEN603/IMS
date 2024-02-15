package com.ims.inventory.service;

import com.ims.common.service.BaseService;
import com.ims.common.utils.IdWorker;
import com.ims.domain.inventory.Order;
import com.ims.domain.inventory.OrderDetail;
import com.ims.inventory.dao.OrderDao;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderService extends BaseService {
    @Autowired
    private OrderDao orderDao;


    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private IdWorker idWorker;

    public void save(Order order) {
        String id = idWorker.nextId()+"";
        order.setId(id);
        order.setState(1);
        Date date = new Date();
        order.setCreateTime(date);
        order.setUpdateTime(date);
        if (order.getType().equals(0)){
            order.setCompleteTime(date);
        }
        List<OrderDetail> orderDetails = order.getOrderItems();
        for (OrderDetail orderDetail : orderDetails){
            orderDetail.setId(idWorker.nextId()+"");
            orderDetail.setCreateTime(date);
            orderDetail.setUpdateTime(date);
            if (order.getType().equals(0)){
                orderDetail.setCompleteTime(date);
            }
            orderDetail.setType(order.getType());
            orderDetail.setOrder(order);
        }
        orderDao.save(order);
    }

    public void update(Order order) {
        Date date = new Date();
        order.setCreateTime(orderDao.findById(order.getId()).get().getCreateTime());
        order.setUpdateTime(date);
        List<OrderDetail> orderDetails = order.getOrderItems();
        for (OrderDetail orderDetail : orderDetails){
            orderDetail.setUpdateTime(date);
            orderDetail.setOrder(order);
        }
        orderDao.save(order);
    }

    public void deleteById(String id) {
        orderDao.deleteById(id);
    }

    public Page<Order> findByPage(Map<String,Object> map, int page, int size){
        String keyword = (String) map.get("keyword");
        Specification<Order> spec = new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();

                if (!StringUtils.isEmpty(map.get("companyId"))){
                    list.add(cb.equal(root.get("companyId").as(String.class) , (String)map.get("companyId")));
                }

                if (!StringUtils.isEmpty(map.get("type"))){
                    list.add(cb.equal(root.get("type").as(String.class) , (String)map.get("type")));
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        return orderDao.findAll(spec, PageRequest.of(page - 1, size));
    }

    public Order findById(String id) {
        return orderDao.findById(id).get();
    }

    public List<Order> findAll(String companyId){
        return orderDao.findAll(getSpec(companyId));
    }

    public void complete(String id) {
        Order order = orderDao.findById(id).get();
        order.setState(0);
        Date date = new Date();
        order.setUpdateTime(date);
        List<OrderDetail> orderDetails = order.getOrderItems();
        for (OrderDetail orderDetail : orderDetails){
            orderDetail.setUpdateTime(date);
            inventoryService.updateInventory(orderDetail.getProductId(), order.getType()==0?(-1)*orderDetail.getQuantity():orderDetail.getQuantity());
        }
        orderDao.save(order);
    }

}