package com.bobbyprod.core;

import com.bobbyprod.agv.Agv;
import com.bobbyprod.agv.controller.AgvController;
import com.bobbyprod.agv.service.AgvService;
import com.bobbyprod.assemblystation.AssemblyStation;
import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Communication.Mediator;
import com.bobbyprod.common.Products.Product;
import com.bobbyprod.drone.Drone;
import com.bobbyprod.warehouse.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
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
		ConfigurableApplicationContext context = SpringApplication.run(AppApplication.class, args);
		AppApplication app = context.getBean(AppApplication.class);
		app.startApplication();
	}

	public void startApplication() {
		mediator = Mediator.getInstance();
		Warehouse warehouse = new Warehouse();
		AssemblyStation assemblyStation = new AssemblyStation();
		Agv agv = new Agv();
		mediator.registerAsset(warehouse);
		mediator.registerAsset(assemblyStation);
		mediator.registerAsset(agv);
		for (Asset asset : mediator.getAssets()) {
			System.out.println(asset.getName() + ", type: " + asset.getType() + ", state: " + asset.getState());
		}
		System.out.println();

		Product product = new Drone("Drone1", "Drone1", null);
		warehouse.getwService().insertItem(product);

		Product product1 = new Drone("Drone2", "Drone2", null);
		warehouse.getwService().insertItem(product1);

		Product product2 = new Drone("Drone3", "Drone3", null);
		warehouse.getwService().insertItem(product2);

		mediator.getProductionQueue().addToQueue(product);
		mediator.getProductionQueue().addToQueue(product1);
		mediator.getProductionQueue().addToQueue(product2);

		mediator.startProduction();
	}
}
