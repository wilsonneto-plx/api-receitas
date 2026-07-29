package com.wilson.api_receitas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApiReceitasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiReceitasApplication.class, args);
	}

}
