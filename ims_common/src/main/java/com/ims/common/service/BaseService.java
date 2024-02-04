package com.ims.common.service;

import org.springframework.data.jpa.domain.Specification;

public class BaseService<T> {

    protected Specification<T> getSpec(String companyId){
        return (Specification<T>) (Specification) (root, query, cb) -> cb.equal(root.get("companyId").as(String.class) , companyId);
    }
}
