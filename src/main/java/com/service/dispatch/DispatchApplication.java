package com.service.dispatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DispatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(DispatchApplication.class, args);
	}

}
