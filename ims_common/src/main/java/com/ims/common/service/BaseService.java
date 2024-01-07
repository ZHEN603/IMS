package com.ims.common.service;

import org.springframework.data.jpa.domain.Specification;

/**
 * @author: hyl
 * @date: 2020/01/04
 **/
public class BaseService<T> {

    protected Specification<T> getSpec(String companyId){
        return (Specification<T>) (Specification) (root, query, cb) -> cb.equal(root.get("companyId").as(String.class) , companyId);
    }
}
