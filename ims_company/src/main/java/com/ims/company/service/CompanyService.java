package com.ims.company.service;

import com.ims.domain.company.Company;
import org.springframework.data.domain.Page;

import java.util.List;


public interface CompanyService {

    public void save(Company company);

    public void update(Company company) ;

    public void deleteById(String id) ;

    public Company findById(String id);

    public List<Company> findAll();

    public Page<Company> findByPage(int page, int size, String keyword);

    public List<String> findTypes();
}