package com.bobbyprod.core;

import com.bobbyprod.agv.Agv;
import com.bobbyprod.assemblystation.AssemblyStation;
import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Communication.Mediator;
import com.bobbyprod.common.Products.Product;
import com.bobbyprod.drone.Drone;
import com.bobbyprod.warehouse.Warehouse;
import org.springframework.stereotype.Component;

@Component
public class AssetInitializer {

    private final Mediator mediator;

    public AssetInitializer() {
        this.mediator = Mediator.getInstance();
        initializeAssets();
    }

    private void initializeAssets() {
        // Initial registration of assets
        Asset warehouse = new Warehouse();
        Asset assemblyStation = new AssemblyStation();
        Asset agv = new Agv();

        mediator.registerAsset(warehouse);
        mediator.registerAsset(assemblyStation);
        mediator.registerAsset(agv);

        Product product = new Drone("MIR Drone", "1", null);
        mediator.getProductionQueue().addToQueue(product);
    }
}
