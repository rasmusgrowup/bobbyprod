package com.bobbyprod.core;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Communication.Mediator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@SpringBootApplication(scanBasePackages = "com.bobbyprod")
@EnableScheduling
public class AppApplication {
	private static Mediator mediator;
	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
		mediator = Mediator.getInstance();
		for (Asset asset : mediator.getAssets()) {
			System.out.println(asset.getName() + ", type: " + asset.getType());
		}
	}
}