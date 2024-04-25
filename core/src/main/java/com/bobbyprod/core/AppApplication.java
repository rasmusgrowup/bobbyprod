package com.bobbyprod.core;

import com.bobbyprod.agv.AgvService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "com.bobbyprod")
@RestController
public class AppApplication {

	private final AgvService agvService;

	public AppApplication(AgvService agvService) {
		this.agvService = agvService;
	}

	@GetMapping("/")
	public String home() {
		return agvService.message();
	}

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

}
