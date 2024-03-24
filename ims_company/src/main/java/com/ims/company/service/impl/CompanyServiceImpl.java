package com.ims.company.service.impl;

import com.ims.common.utils.IdWorker;
import com.ims.company.dao.CompanyDao;
import com.ims.company.mq.CompanyMessageSender;
import com.ims.company.service.CompanyService;
import com.ims.domain.company.Company;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Resource
    private CompanyDao companyDao;
    @Resource
    private IdWorker idWorker;
    @Resource
    private CompanyMessageSender companyMessageSender;
    @Resource
    private EntityManager entityManager;
    @Override
    public void save(Company company) {
        String id = idWorker.nextId()+"";
        String userId = idWorker.nextId()+"";
        company.setId(id);
        company.setManagerId(userId);
        company.setCreateTime(new Date());
        companyDao.save(company);
        companyMessageSender.sendCompanyCreateMessage(company);
//        if (!res.getCode().equals(10000)){
//            throw new RuntimeException("Create Admin Account Failed");
//        }
    }
    @Override
    public void update(Company company) { companyDao.save(company); }
    @Override
    public void deleteById(String id) {
        companyDao.deleteById(id);
    }
    @Override
    public Company findById(String id) {
        return companyDao.findById(id).get();
    }
    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }
    @Override
    public Page<Company> findByPage(int page, int size, String keyword){
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

    @Override
    public List<String> findTypes(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<Company> companyRoot = cq.from(Company.class);
        cq.select(companyRoot.get("type")).distinct(true);
        return entityManager.createQuery(cq).getResultList();
    }

}