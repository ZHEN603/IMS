package com.ims.company.dao;

import com.ims.domain.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompanyDao extends JpaRepository<Company,String> , JpaSpecificationExecutor<Company> {
}
