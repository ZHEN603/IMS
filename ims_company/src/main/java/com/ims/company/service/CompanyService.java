package com.ims.company.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.common.utils.IdWorker;
import com.ims.company.client.UserFeignClient;
import com.ims.company.dao.CompanyDao;
import com.ims.domain.company.Company;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserFeignClient userFeignClient;

    public void save(Company company) {
        String id = idWorker.nextId()+"";
        String userId = idWorker.nextId()+"";
        company.setId(id);
        company.setManagerId(userId);
        company.setCreateTime(new Date());
        companyDao.save(company);
        userFeignClient.saveAdmin(userId, company.getName(), company.getId());
    }

    public void update(Company company) { companyDao.save(company); }

    public void deleteById(String id) {
        companyDao.deleteById(id);
    }

    public Company findById(String id) {
        return companyDao.findById(id).get();
    }

    public List<Company> findAll() {
        return companyDao.findAll();
    }

    public Page<Company> findAll(int page, int size, String keyword){
        Specification<Company> spec = new Specification<Company>() {
            @Override
            public Predicate toPredicate(Root<Company> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (keyword != null && !keyword.trim().isEmpty()) {
                    list.add(cb.like(root.get("name"), "%" + keyword.trim() + "%"));
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        return companyDao.findAll(spec, PageRequest.of(page - 1, size));
    }
}