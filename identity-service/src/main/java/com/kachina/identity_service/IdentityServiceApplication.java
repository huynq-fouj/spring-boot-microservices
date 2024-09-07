package com.kachina.identity_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@EnableFeignClients
public class IdentityServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(IdentityServiceApplication.class, args);
		log.info("\u001B[32mIdentity Service Running...\u001B[0m");
	}

}
