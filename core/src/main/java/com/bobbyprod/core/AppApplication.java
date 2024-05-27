package com.bobbyprod.core;

import com.bobbyprod.agv.Agv;
import com.bobbyprod.assemblystation.AssemblyStation;
import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Communication.Mediator;
import com.bobbyprod.common.Interfaces.IMediator;
import com.bobbyprod.common.ProductionLine.ActiveProductsList;
import com.bobbyprod.common.ProductionLine.AssetsList;
import com.bobbyprod.common.ProductionLine.FinishedProducts;
import com.bobbyprod.common.ProductionLine.ProductionQueue;
import com.bobbyprod.common.Products.Product;
import com.bobbyprod.drone.Drone;
import com.bobbyprod.warehouse.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@Controller
@SpringBootApplication(scanBasePackages = "com.bobbyprod")
@EnableScheduling
public class AppApplication implements CommandLineRunner {
	private static IMediator mediator;
	private AssemblyStation assemblyStation;
	private Agv agv;
	private Warehouse warehouse;
	private AssetsList assets;
	private ProductionQueue productionQueue;
	private ActiveProductsList activeProductsList;
	private FinishedProducts finishedProducts;

	@Autowired
	public AppApplication(AssemblyStation assemblyStation, Agv agv, Warehouse warehouse) {
		this.mediator = Mediator.getInstance();
		this.assemblyStation = assemblyStation;
		this.agv = agv;
		this.warehouse = warehouse;
		this.assets = AssetsList.getInstance();
		this.productionQueue = ProductionQueue.getInstance();
		this.activeProductsList = ActiveProductsList.getInstance();
		this.finishedProducts = FinishedProducts.getInstance();
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AppApplication.class, args);
		AppApplication app = context.getBean(AppApplication.class);
		app.startApplication();
	}

	public void startApplication() {
	}

	@GetMapping("/api/assets")
	@ResponseBody
	public List<Asset> getAssets() {
		return assets.getAssets();
	}

	@GetMapping("/api/production-queue")
	@ResponseBody
	public List<Product> getProductionQueue() {
		return ProductionQueue.getInstance().getQueue();
	}

	@GetMapping("/api/finished-products")
	@ResponseBody
	public List<Product> getFinishedProducts() {
		return FinishedProducts.getInstance().getFinishedProducts();
	}

	@GetMapping("/api/active-products")
	@ResponseBody
	public List<Product> getActiveProducts() {
		return ActiveProductsList.getInstance().getActiveProductionList();
	}

	@PostMapping("/api/add-product")
	@ResponseBody
	public void addProduct() {
		String droneName = "KillerDrone";
		String droneID = "ID: D00" + (ProductionQueue.getInstance().getQueue().size() + 1);
		Product product = new Drone(droneName, droneID, null);
		warehouse.getwService().insertItem(product);
		ProductionQueue.getInstance().addToQueue(product);
	}

	@PostMapping("/api/start-production")
	@ResponseBody
	public void startProduction() {
		mediator.startProduction();
	}

	@Override
	public void run(String... args) throws Exception {
		assets.addAsset(warehouse);
		assets.addAsset(assemblyStation);
		assets.addAsset(agv);
		mediator.registerAsset(warehouse);
		mediator.registerAsset(assemblyStation);
		mediator.registerAsset(agv);
		for (Asset asset : assets.getAssets()) {
			System.out.println(asset.getName() + ", type: " + asset.getType() + ", state: " + asset.getState());
		}
		System.out.println();
		warehouse.getwService().clearInventory();
	}
}
