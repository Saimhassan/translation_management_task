package com.translation.managment.service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@OpenAPIDefinition(info = @Info(
		title = "Translation Management API",
		version = "1.0",
		description = "API for managing translations and locales"
))
public class TranslationManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TranslationManagementServiceApplication.class, args);
	}

}
