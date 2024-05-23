package com.bobbyprod.core;

import com.bobbyprod.agv.Agv;
import com.bobbyprod.assemblystation.AssemblyStation;
import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Communication.Mediator;
import com.bobbyprod.common.Products.Part;
import com.bobbyprod.common.Products.Product;
import com.bobbyprod.drone.Drone;
import com.bobbyprod.warehouse.Warehouse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@Controller
@SpringBootApplication(scanBasePackages = "com.bobbyprod")
@EnableScheduling
public class AppApplication {
	private static Mediator mediator;
	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
		mediator = Mediator.getInstance();
		//Product product = new Drone("Drone1", "Drone1", null);
//		Asset warehouse = new Warehouse();
//		Asset assemblyStation = new AssemblyStation();
//		Asset agv = new Agv();
//		mediator.registerAsset(warehouse);
//		mediator.registerAsset(assemblyStation);
//		mediator.registerAsset(agv);
		for (Asset asset : mediator.getAssets()) {
			System.out.println(asset.getName() + ", type: " + asset.getType());
		}
//		System.out.println();
		//mediator.getProductionQueue().addToQueue(product);
		//mediator.startProduction();
	}
}