package com.ims.product.service;

import com.ims.common.entity.Result;
import com.ims.common.service.BaseService;
import com.ims.common.utils.IdWorker;
import com.ims.domain.product.Product;
import com.ims.domain.user.Role;
import com.ims.product.client.InventoryFeignClient;
import com.ims.product.dao.CategoryDao;
import com.ims.product.dao.ProductDao;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ProductService extends BaseService {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private InventoryFeignClient inventoryFeignClient;

    @Autowired
    private IdWorker idWorker;

    public void save(Map<String, Object> map) {
        // save new product
        String id = idWorker.nextId()+"";
        Product product = new Product();
        product.setId(id);
        product.setCompanyId((String) map.get("companyId"));
        product.setName((String) map.get("name"));
        product.setCategoryId((String) map.get("categoryId"));
        product.setCategoryName((String) map.get("categoryName"));
        product.setPrice(Double.parseDouble((String) map.get("price")));
        product.setDiscount(Double.parseDouble((String) map.get("discount")));
        product.setCost(Double.parseDouble((String) map.get("cost")));
        product.setDescription((String) map.get("description"));
        productDao.save(product);

        // set up required product data that inventory need
        map.put("productId", product.getId());
        map.remove("price");
        map.remove("discount");
        map.remove("cost");
        map.remove("description");

        inventoryFeignClient.save(map);
    }

    public void update(Product product) {
        productDao.save(product);
    }

    public void deleteById(String id) {
        productDao.deleteById(id);
    }

    public Product findById(String id) {
        return productDao.findById(id).get();
    }

    public List<Product> findAll(String companyId){
        return productDao.findAll(getSpec(companyId));
    }
    public Page<Product> findByPage(Map<String,Object> map, int page, int size){
        String keyword = (String) map.get("keyword");
        Specification<Product> spec = new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();

                if (!StringUtils.isEmpty(map.get("companyId"))){
                    list.add(cb.equal(root.get("companyId").as(String.class) , (String)map.get("companyId")));
                }

                if (!StringUtils.isEmpty(map.get("categoryId")) && !((String)map.get("categoryId")).equals("0")){
                    List<String> ids = new ArrayList<>();
                    categoryService.findAllChildIds((String)map.get("categoryId"),ids);
                    list.add(root.get("categoryId").as(String.class).in(ids));
                }
                if (keyword != null && !keyword.trim().isEmpty()) {
                    list.add(cb.like(root.get("name"), "%" + keyword.trim() + "%"));
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        return productDao.findAll(spec, PageRequest.of(page - 1, size));
    }

    public void deleteByIds(Map<String, List<String>> map) {
        List<String> ids = map.get("ids");
        for (String id : ids) {
            productDao.deleteById(id);
        }
    }

    public List<Product> findByIds(List<String> ids, String companyId) {
        Specification<Product> spec = new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(companyId)){
                    list.add(cb.equal(root.get("companyId").as(String.class) , companyId));
                }
                list.add(root.get("id").as(String.class).in(ids));
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        return productDao.findAll(spec);
    }
}
