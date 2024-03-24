package com.ims.inventory.service;

import com.ims.common.exception.CommonException;
import com.ims.domain.inventory.Inventory;
import com.ims.domain.product.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


public interface InventoryService {
    public void save(Map<String,Object> map);

    public void update(Inventory inventory);

    public void updateProductInfo(Product product);

    public void deleteByProductId(String id);

    public Page<Inventory> findByPage(Map<String,Object> map, int page, int size);

    public Inventory findById(String id);

    public List<Inventory> findAll(Map map);

    public void updateInventory(String productId, Integer quantity) throws CommonException;

    public void updateState(String id, Integer state);

    public List<Inventory> findByIds(List<String> ids, String companyId);
}