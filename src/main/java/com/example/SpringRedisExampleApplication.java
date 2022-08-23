package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringRedisExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRedisExampleApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello() {
		return "HELLO WORLD";
	}
}
