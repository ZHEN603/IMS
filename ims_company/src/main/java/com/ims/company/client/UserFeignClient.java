package com.ims.company.client;

import com.ims.common.entity.Result;
import com.ims.domain.company.Company;
import com.ims.domain.user.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

//@FeignClient("ims-user")
@FeignClient(name = "ims-user", url = "http://ims-user:9002")
public interface UserFeignClient {
    //Full path
    @RequestMapping(value = "/user/admin", method = RequestMethod.POST)
    Result saveAdmin(@RequestParam("id") String id, @RequestParam("companyName") String companyName, @RequestParam("companyId") String companyId);
}
