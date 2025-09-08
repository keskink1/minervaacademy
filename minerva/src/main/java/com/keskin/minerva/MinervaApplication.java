package com.keskin.minerva;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableCaching
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@OpenAPIDefinition(
		info = @Info(
				title = "This documentation provides an overview of the REST APIs developed for managing course schedules and lesson planning. The application is designed for use by academies and training centers, enabling students and teachers to view and manage their lesson timetables through structured RESTful endpoints.",
				version = "v1",
				contact = @Contact(
						name = "Kaan Keskin",
						email = "kaankeskin1@hotmail.com"
				)))
public class MinervaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinervaApplication.class, args);
	}

}
