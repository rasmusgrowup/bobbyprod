package com.bobbyprod.core;

import com.bobbyprod.agv.Agv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

@RestController
@SpringBootApplication(scanBasePackages = "com.bobbyprod")
@EnableScheduling
public class AppApplication {
	private final Agv agv;

	@Autowired
	public AppApplication(Agv agv) {
		this.agv = agv;
	}


	@GetMapping("/")
	public String home() {
		return agv.getState().toString();
	}

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}
}
