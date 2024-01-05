package com.ims.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

//1.配置springboot的包扫描
@SpringBootApplication(scanBasePackages = "com.ims.company")
//2.配置jpa注解的扫描
@EntityScan(value="com.ims.domain.company")

public class CompanyApplication {

    /**
     * 启动方法
     */
    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class,args);
    }
}
