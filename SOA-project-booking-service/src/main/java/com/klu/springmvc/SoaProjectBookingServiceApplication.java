package com.klu.springmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableFeignClients
@SpringBootApplication
public class SoaProjectBookingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoaProjectBookingServiceApplication.class, args);
	}

}
