package com.klu.springmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient 
public class S59ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(S59ApiGatewayApplication.class, args);
	}

}
