package com.klu.springmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SoaProjectVisitServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoaProjectVisitServiceApplication.class, args);
    }
}