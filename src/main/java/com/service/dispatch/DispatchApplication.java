package com.service.dispatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class DispatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(DispatchApplication.class, args);
	}

}
