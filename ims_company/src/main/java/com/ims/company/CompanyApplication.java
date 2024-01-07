package com.ims.company;

import com.ims.common.utils.IdWorker;
import com.ims.common.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.bind.annotation.RestController;

//1.配置springboot的包扫描
@SpringBootApplication(scanBasePackages = "com.ims")
//2.配置jpa注解的扫描
@EntityScan(value="com.ims.domain.company")
@EnableFeignClients
public class CompanyApplication {

    /**
     * 启动方法
     */
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

    @Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
        return new OpenEntityManagerInViewFilter();
    }
}
