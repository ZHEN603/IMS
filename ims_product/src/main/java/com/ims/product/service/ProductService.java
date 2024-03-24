package com.ims.product.service;

import com.ims.domain.product.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


public interface ProductService {
    public void save(Map<String, Object> map);

    public void update(Product product);

    public void deleteById(String id);

    public Product findById(String id);

    public List<Product> findAll(String companyId);

    public Page<Product> findByPage(Map<String, Object> map, int page, int size);

    public List<Product> findByIds(List<String> ids, String companyId);

    public void rollBack(String id);
}
