package com.ims.gate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ims")
public class GateApplication {

    public static void main(String[] args) {
        SpringApplication.run(GateApplication.class);
    }

}
