package com.bobbyprod.core;

import com.bobbyprod.agv.AgvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@RestController
@SpringBootApplication(scanBasePackages = "com.bobbyprod")
public class AppApplication {
	private final AgvService agvService;

	@Autowired
	public AppApplication(AgvService agvService) {
		this.agvService = agvService;
	}

	@GetMapping("/")
	public String home() {
		return agvService.getStatus();
	}

	@PutMapping("/status")
	public void updateStatus(@RequestBody String newStatus) {
		agvService.updateStatus(newStatus);
	}

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

}
