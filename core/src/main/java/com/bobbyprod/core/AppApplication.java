package com.bobbyprod.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

//@RestController
@Controller
@SpringBootApplication(scanBasePackages = "com.bobbyprod")
@EnableScheduling
public class AppApplication {
//	private final Agv agv;

//	@Autowired
//	public AppApplication(Agv agv) {
//		this.agv = agv;
//	}
//
//
//	@GetMapping("/")
//	public String home() {
//		return agv.getState().toString();
//	}

//	@GetMapping("/")
//	public String home() {
//		return "index";
//	}
	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}
}
