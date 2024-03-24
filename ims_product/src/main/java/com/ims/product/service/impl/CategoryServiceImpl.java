package com.ims.product.service.impl;

import com.ims.common.utils.IdWorker;
import com.ims.domain.product.Category;
import com.ims.product.dao.CategoryDao;
import com.ims.product.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryDao categoryDao;
    @Resource
    private IdWorker idWorker;
    public void save(Category category) {
        String id = idWorker.nextId()+"";
        category.setId(id);
        category.setCreateTime(new Date());
        categoryDao.save(category);
    }
    @Override
    public void update(Category category) {
        categoryDao.save(category);
    }
    @Override
    public void deleteById(String id) {
        categoryDao.deleteById(id);
        List<Category> subs = categoryDao.findAll((Specification<Category>) (Specification) (root, query, cb) -> cb.equal(root.get("pid").as(String.class) , id));
        for (Category sub : subs) {
            deleteById(sub.getId());
        }
    }
    @Override
    public Category findById(String id) {
        return categoryDao.findById(id).get();
    }
    @Override
    public List<Category> findAll(String companyId) {
        return categoryDao.findAll((Specification) (root, query, cb) -> cb.equal(root.get("companyId").as(String.class) , companyId));
    }
    @Override
    public void findAllChildIds(String categoryId, List<String> ids) {
        ids.add(categoryId);
        List<Category> subs = categoryDao.findAll((Specification<Category>) (Specification) (root, query, cb) -> cb.equal(root.get("pid").as(String.class) , categoryId));
        for (Category sub : subs) {
            findAllChildIds(sub.getId(),ids);
        }
    }

}
