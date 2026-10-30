package com.klu.springmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SoaProjectInquiryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoaProjectInquiryServiceApplication.class, args);
    }
}