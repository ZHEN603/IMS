package com.ims.user.client;

import com.ims.common.entity.Result;
import com.ims.domain.company.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("ims-company")
public interface CompanyFeignClient {
    @RequestMapping(value = "/company", method = RequestMethod.GET)
    Result findAll();
}
