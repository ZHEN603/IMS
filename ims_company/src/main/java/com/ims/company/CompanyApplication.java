package com.ims.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.common.utils.IdWorker;
import com.ims.common.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

@SpringBootApplication(scanBasePackages = "com.ims")
@EntityScan(value="com.ims.domain.company")
//@EnableDiscoveryClient
public class CompanyApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class,args);
    }
    @Bean
    public IdWorker idWorker() {
        return new IdWorker();
    }
    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

}
