package com.ims.inventory.service;

import com.ims.common.entity.PageResult;
import com.ims.common.entity.Result;
import com.ims.common.service.BaseService;
import com.ims.common.utils.IdWorker;
import com.ims.domain.inventory.Inventory;
import com.ims.domain.product.Product;
import com.ims.inventory.client.ProductFeignClient;
import com.ims.inventory.dao.InventoryDao;
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
import java.util.stream.Collectors;

@Service
public class InventoryService extends BaseService {
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private ProductFeignClient productFeignClient;

    public void save(Map<String,Object> map) {
        String id = idWorker.nextId()+"";
        Inventory inventory = new Inventory();
        inventory.setId(id);
        inventory.setProductId((String)map.get("productId"));
        inventory.setProductName((String)map.get("name"));
        inventory.setCategoryId((String)map.get("categoryId"));
        inventory.setState(1);
        inventory.setQuantity((Integer) map.get("quantity"));
        inventory.setLowStock((Integer) map.get("lowStock"));
        inventory.setCompanyId((String)map.get("companyId"));
        inventoryDao.save(inventory);
    }

    public void update(Inventory inventory) {
        Inventory inventory1 = inventoryDao.findById(inventory.getId()).get();
        inventory1.setQuantity(inventory.getQuantity());
        inventory1.setState(inventory.getState());
        inventory1.setLowStock(inventory.getLowStock());
        inventoryDao.save(inventory1);
    }

    public void deleteById(String id) {
        inventoryDao.deleteById(id);
    }

    public Page<Inventory> findByPage(Map<String,Object> map, int page, int size){
        String keyword = (String) map.get("keyword");
        Specification<Inventory> spec = new Specification<Inventory>() {
            @Override
            public Predicate toPredicate(Root<Inventory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();

                if (!StringUtils.isEmpty(map.get("companyId"))){
                    list.add(cb.equal(root.get("companyId").as(String.class) , (String)map.get("companyId")));
                }

                if (!StringUtils.isEmpty(map.get("categoryId")) && !((String)map.get("categoryId")).equals("0")){
                    List<String> ids = productFeignClient.findAllChildIds((String)map.get("categoryId"), (String)map.get("companyId"));
                    list.add(root.get("categoryId").as(String.class).in(ids));
                }
                if (keyword != null && !keyword.trim().isEmpty()) {
                    list.add(cb.like(root.get("productName"), "%" + keyword.trim() + "%"));
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        return inventoryDao.findAll(spec, PageRequest.of(page - 1, size));
    }

    public Inventory findById(String id) {
        return inventoryDao.findById(id).get();
    }

    public List<Inventory> findAll(Map map){

        String keyword = (String) map.get("keyword");
        Specification<Inventory> spec = new Specification<Inventory>() {
            @Override
            public Predicate toPredicate(Root<Inventory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();

                if (!StringUtils.isEmpty(map.get("companyId"))){
                    list.add(cb.equal(root.get("companyId").as(String.class) , (String)map.get("companyId")));
                }

                if (!StringUtils.isEmpty(map.get("categoryId")) && !((String)map.get("categoryId")).equals("0")){
                    List<String> ids = productFeignClient.findAllChildIds((String)map.get("categoryId"), (String)map.get("companyId"));
                    list.add(root.get("categoryId").as(String.class).in(ids));
                }
                if (keyword != null && !keyword.trim().isEmpty()) {
                    list.add(cb.like(root.get("productName"), "%" + keyword.trim() + "%"));
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        return inventoryDao.findAll(spec);
    }

    public void updateInventory(String productId, Integer quantity) {
        Inventory inventory = inventoryDao.findByProductId(productId).get(0);
        inventory.setQuantity(inventory.getQuantity()+quantity);
        inventoryDao.save(inventory);
    }

    public List<Inventory> findByIds(List<String> ids, String companyId) {
        Specification<Inventory> spec = new Specification<Inventory>() {
            @Override
            public Predicate toPredicate(Root<Inventory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(companyId)){
                    list.add(cb.equal(root.get("companyId").as(String.class) , companyId));
                }
                if (!ids.isEmpty()){
                    list.add(root.get("productId").as(String.class).in(ids));
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        return inventoryDao.findAll(spec);
    }
}