package com.ims.product.service;

import com.ims.common.service.BaseService;
import com.ims.common.utils.IdWorker;
import com.ims.domain.product.Category;
import com.ims.product.dao.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CategoryService extends BaseService {
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private IdWorker idWorker;
    public void save(Category category) {
        String id = idWorker.nextId()+"";
        category.setId(id);
        category.setCreateTime(new Date());
        categoryDao.save(category);
    }

    public void update(Category category) {
        categoryDao.save(category);
    }

    public void deleteById(String id) {
        categoryDao.deleteById(id);
        List<Category> subs = categoryDao.findAll((Specification<Category>) (Specification) (root, query, cb) -> cb.equal(root.get("pid").as(String.class) , id));
        for (Category sub : subs) {
            deleteById(sub.getId());
        }
    }

    public Category findById(String id) {
        return categoryDao.findById(id).get();
    }

    public List<Category> findAll(String companyId) {
        return categoryDao.findAll(getSpec(companyId));
    }

    public void findAllChildIds(String categoryId, List<String> ids) {
        ids.add(categoryId);
        List<Category> subs = categoryDao.findAll((Specification<Category>) (Specification) (root, query, cb) -> cb.equal(root.get("pid").as(String.class) , categoryId));
        for (Category sub : subs) {
            findAllChildIds(sub.getId(),ids);
        }
    }

}
