package com.ims.supplier.service.impl;

import com.ims.common.utils.IdWorker;
import com.ims.supplier.dao.SupplierDao;
import com.ims.domain.supplier.Supplier;
import com.ims.supplier.service.SupplierService;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
public class SupplierServiceImpl implements SupplierService {
    @Resource
    private SupplierDao supplierDao;
    @Resource
    private IdWorker idWorker;
    @Override
    public void save(Supplier supplier) {
        String id = idWorker.nextId()+"";
        supplier.setId(id);
        supplier.setCreateTime(new Date());
        supplierDao.save(supplier);
    }
    @Override
    public void update(Supplier supplier) { supplierDao.save(supplier); }
    @Override
    public void deleteById(String id) {
        supplierDao.deleteById(id);
    }
    @Override
    public Supplier findById(String id) {
        return supplierDao.findById(id).get();
    }
    @Override
    public List<Supplier> findAll(String companyId) {
        System.out.println(companyId);
        return supplierDao.findAll((Specification) (root, query, cb) -> cb.equal(root.get("companyId").as(String.class) , companyId));
    }
    @Override
    public Page<Supplier> findAll(int page, int size, Map<String,Object> map){
        String keyword = (String) map.get("keyword");
        Specification<Supplier> spec = new Specification<Supplier>() {
            @Override
            public Predicate toPredicate(Root<Supplier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(map.get("companyId"))){
                    list.add(cb.equal(root.get("companyId").as(String.class) , (String)map.get("companyId")));
                }
                if (keyword != null && !keyword.trim().isEmpty()) {
                    list.add(cb.like(root.get("name"), "%" + keyword.trim() + "%"));
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        return supplierDao.findAll(spec, PageRequest.of(page - 1, size));
    }
}