package com.ims.product.service;

import com.ims.domain.product.Category;

import java.util.List;


public interface CategoryService {
    public void save(Category category);

    public void update(Category category);

    public void deleteById(String id);

    public Category findById(String id);

    public List<Category> findAll(String companyId);

    public void findAllChildIds(String categoryId, List<String> ids) ;

}
