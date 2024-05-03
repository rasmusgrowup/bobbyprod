package com.bobbyprod.common.Communication;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetManager;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.Interfaces.IMediator;
import com.bobbyprod.common.ProductionLine.ActivePartsList;
import com.bobbyprod.common.ProductionLine.ActiveProductsList;
import com.bobbyprod.common.ProductionLine.ProductionQueue;
import com.bobbyprod.common.Products.Part;
import com.bobbyprod.common.Products.Product;
import com.bobbyprod.common.Tasks.ActionType;
import com.bobbyprod.common.Tasks.Task;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Mediator implements IMediator {
    private List<Asset> assets; // List of all registered assets
    private Map<Asset, Task> assetTasks; // Map to keep track of current tasks for each asset
    private ProductionQueue productionQueue;// Queue to manage production of products
    private ActiveProductsList activeProductsList; // List of products currently in production
    private ActivePartsList activePartsList; // List of parts currently in production

    public Mediator() {
        assets = new ArrayList<>();
        assetTasks = new HashMap<>();
        productionQueue = new ProductionQueue();
        activeProductsList = new ActiveProductsList();
        activePartsList = new ActivePartsList();
    }

    // Registers assets with the mediator
    public void registerAsset(Asset asset) {
        if (!assets.contains(asset)) {
            assets.add(asset);
            assetTasks.put(asset, null);
        }
    }

    public void startProduction() {
        while (!productionQueue.isEmpty()) {
            Product product = productionQueue.getFirstInQueue();
            activeProductsList.addToActiveProductionList(product);
            for (Part part : product.getPartsList()) {
                activePartsList.addToActiveProductionList(part);
                Task task = new Task(ActionType.PICK_ITEM, AssetType.WAREHOUSE, part);
                Asset warehouseAsset = AssetManager.findAvailableAsset(assets,AssetType.WAREHOUSE);
                if (warehouseAsset != null) {
                    assignTask(warehouseAsset, task);
                }
            }
            productionQueue.removeFromQueue(product);
        }
    }

    // Handles notifications from assets. Decides action based on event type and asset type.
    @Override
    public void notify(Asset asset, String event, Task task) {
        if (!assets.contains(asset)) {
            System.out.println("Asset not registered: " + asset.getName());
            return;
        }
        switch (event) {
            case "TaskAccepted":
                System.out.println(asset.getName() + " accepted task: " + task.getActionType());
                assetTasks.put(asset, task);
                break;
            case "TaskCompleted":
                System.out.println(asset.getName() + " completed task: " + task.getActionType());
                assetTasks.put(asset, null); // Clear the task as it is completed
                fleetManagement(asset, task);
                break;
            case "TaskFailed":
                System.out.println(asset.getName() + " failed task: " + task.getActionType());
                assetTasks.put(asset, null); // Clear the task on failure
                break;
        }
    }
    private void fleetManagement(Asset asset, Task task) {
        System.out.println(asset.getName() + " completed task: " + task.getActionType());
        switch (task.getActionType()) {
            case PICK_ITEM:
                if (task.getPart() != null) {
                    // Step 3: Part is picked up from warehouse by AGV
                    task.getPart().setLocation(asset);
                    Task moveToAssemblyTask = new Task(ActionType.MOVE_TO_WAREHOUSE, AssetType.AGV, task.getPart());
                    assignTask(asset, moveToAssemblyTask);
                }
                break;
            case MOVE_TO_WAREHOUSE:
                if (task.getPart() != null) {
                    // Step 3: Part is picked up from warehouse by AGV
                    task.getPart().setLocation(asset);
                    Task moveToAssemblyTask = new Task(ActionType.PICK_ITEM_FROM_WAREHOUSE, AssetType.AGV, task.getPart());
                    assignTask(asset, moveToAssemblyTask);
                } else if (task.getProduct() != null) {
                    // Step 9: AGV delivers product to warehouse
                    Asset warehouse = AssetManager.findAvailableAsset(assets, AssetType.WAREHOUSE);
                    if (warehouse != null) {
                        task.getProduct().setLocation(warehouse);
                        Task putItemToWarehouseTask = new Task(ActionType.PUT_ITEM_TO_WAREHOUSE, AssetType.WAREHOUSE, task.getProduct());
                        assignTask(warehouse, putItemToWarehouseTask);
                    }
                }
                break;
            case PICK_ITEM_FROM_WAREHOUSE:
                if (task.getPart() != null) {
                    // Step 2: Part is picked from warehouse
                    Asset agv = AssetManager.findAvailableAsset(assets, AssetType.AGV);
                    if (agv != null) {
                        task.getPart().setLocation(agv);
                        Task moveFromWarehouseTask = new Task(ActionType.MOVE_TO_ASSEMBLY_STATION, AssetType.AGV, task.getPart());
                        assignTask(agv, moveFromWarehouseTask);
                    }
                }
                break;
            case MOVE_TO_ASSEMBLY_STATION:
                if (task.getPart() != null) {
                    // Step 3: Part is picked up from warehouse by AGV
                    task.getPart().setLocation(asset);
                    Task moveToAssemblyTask = new Task(ActionType.PICK_ITEM_FROM_ASSEMBLY_STATION, AssetType.AGV, task.getPart());
                    assignTask(asset, moveToAssemblyTask);
                } else if (task.getProduct() != null) {
                    // Step 9: AGV delivers product to warehouse
                    Asset warehouse = AssetManager.findAvailableAsset(assets, AssetType.WAREHOUSE);
                    if (warehouse != null) {
                        task.getProduct().setLocation(warehouse);
                        Task putItemToWarehouseTask = new Task(ActionType.PUT_ITEM_TO_ASSEMBLY_STATION, AssetType.WAREHOUSE, task.getProduct());
                        assignTask(warehouse, putItemToWarehouseTask);
                    }
                }
                break;
            case PUT_ITEM_TO_ASSEMBLY_STATION:
                if (task.getPart() != null) {
                    // Step 4: AGV transports parts to assembly station
                    Asset assemblyStation = AssetManager.findAvailableAsset(assets, AssetType.ASSEMBLY_STATION);
                    if (assemblyStation != null) {
                        task.getPart().setLocation(assemblyStation);
                        Task assembleTask = new Task(ActionType.ASSEMBLE_ITEM, AssetType.ASSEMBLY_STATION, task.getPart());
                        assignTask(assemblyStation, assembleTask);
                    }
                }
            case ASSEMBLE_ITEM:
                if (task.getPart() != null) {
                    // Step 6: Assembly station assembles the parts into a product
                    Product product = new Product(task.getPart().getName(), task.getPart().getId(), asset, new ArrayList<>(Arrays.asList(task.getPart())));
                    product.setAssembled(true);
                    product.setLocation(asset);
                    activeProductsList.addToActiveProductionList(product);
                    Task moveToWarehouseTask = new Task(ActionType.MOVE_TO_ASSEMBLY_STATION, AssetType.AGV, product);
                    assignTask(asset, moveToWarehouseTask);
                }
                break;
            case PICK_ITEM_FROM_ASSEMBLY_STATION:
                if (task.getPart() != null) {
                    // Step 2: Part is picked from warehouse
                    Asset agv = AssetManager.findAvailableAsset(assets, AssetType.AGV);
                    if (agv != null) {
                        task.getPart().setLocation(agv);
                        Task moveFromWarehouseTask = new Task(ActionType.MOVE_TO_WAREHOUSE, AssetType.AGV, task.getPart());
                        assignTask(agv, moveFromWarehouseTask);
                    }
                }
                break;
            case PUT_ITEM_TO_WAREHOUSE:
                if (task.getProduct() != null) {
                    // Step 10: Warehouse stores the product in storage
                    task.getProduct().setLocation(asset);
                    System.out.println("Product stored in warehouse: " + task.getProduct().getName());
                }
                break;
        }
    }
    private void assignTask(Asset asset, Task task) {
        System.out.println("Assigning task " + task.getActionType() + " to " + asset.getName());
        assetTasks.put(asset, task);
        asset.handleTask(task);
    }
}
