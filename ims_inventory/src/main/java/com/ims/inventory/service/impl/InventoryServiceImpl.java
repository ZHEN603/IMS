package com.ims.inventory.service.impl;

import com.ims.common.entity.ResultCode;
import com.ims.common.exception.CommonException;
import com.ims.common.utils.IdWorker;
import com.ims.domain.inventory.Inventory;
import com.ims.domain.product.Product;
import com.ims.inventory.client.ProductFeignClient;
import com.ims.inventory.dao.InventoryDao;
import com.ims.inventory.service.InventoryService;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class InventoryServiceImpl implements InventoryService {
    @Resource
    private InventoryDao inventoryDao;
    @Resource
    private IdWorker idWorker;

    @Resource
    private ProductFeignClient productFeignClient;

    @Override
    public void save(Map<String,Object> map) {
        String id = idWorker.nextId()+"";
        Inventory inventory = new Inventory();
        inventory.setId(id);
        inventory.setProductId((String)map.get("productId"));
        inventory.setProductName((String)map.get("name"));
        inventory.setCategoryId((String)map.get("categoryId"));
        inventory.setState(1);
        inventory.setQuantity(Integer.valueOf(map.get("quantity").toString()));
        inventory.setLowStock(Integer.valueOf(map.get("lowStock").toString()));
        inventory.setCompanyId((String)map.get("companyId"));
        inventoryDao.save(inventory);
    }
    @Override
    public void update(Inventory inventory) {
        Inventory pre = inventoryDao.findById(inventory.getId()).get();
        pre.setQuantity(inventory.getQuantity());
        pre.setLowStock(inventory.getLowStock());
        pre.setState(inventory.getState());
        inventoryDao.save(pre);
    }
    @Override
    public void updateProductInfo(Product product) {
        Inventory inventory = inventoryDao.findByProductId(product.getId()).get(0);
        inventory.setProductName(product.getName());
        inventory.setCategoryId(product.getCategoryId());
        inventoryDao.save(inventory);
    }

    @Transactional
    @Override
    public void deleteByProductId(String id) {
        inventoryDao.deleteByProductId(id);
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
                    List<String> ids = productFeignClient.findAllChildIds((String)map.get("categoryId"));
                    list.add(root.get("categoryId").as(String.class).in(ids));
                }
                if (keyword != null && !keyword.trim().isEmpty()) {
                    list.add(cb.like(root.get("productName"), "%" + keyword.trim() + "%"));
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        return inventoryDao.findAll(spec, PageRequest.of(page - 1, size,Sort.by(Sort.Direction.ASC, "difference")));
    }
    @Override
    public Inventory findById(String id) {
        return inventoryDao.findById(id).get();
    }
    @Override
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
                    List<String> ids = productFeignClient.findAllChildIds((String)map.get("categoryId"));
                    list.add(root.get("categoryId").as(String.class).in(ids));
                }
                if (keyword != null && !keyword.trim().isEmpty()) {
                    list.add(cb.like(root.get("productName"), "%" + keyword.trim() + "%"));
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        return inventoryDao.findAll(spec, Sort.by(Sort.Direction.ASC, "difference"));
    }
    @Override
    public void updateInventory(String productId, Integer quantity) throws CommonException {
        Inventory inventory = inventoryDao.findByProductId(productId).get(0);
        if (inventory.getQuantity()+quantity<0){
            throw new CommonException(ResultCode.LOW_STOCK);
        }
        inventory.setQuantity(inventory.getQuantity()+quantity);
        inventoryDao.save(inventory);
    }
    @Override
    public void updateState(String id, Integer state) {
        Inventory inventory = inventoryDao.findById(id).get();
        inventory.setState(state);
        inventoryDao.save(inventory);
    }
    @Override
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