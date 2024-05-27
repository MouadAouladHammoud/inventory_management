package com.mouad.stockmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StockManagementApplication {

	public static void main(String[] args) {
		System.out.println("Hello World");
		SpringApplication.run(StockManagementApplication.class, args);

	}

}
