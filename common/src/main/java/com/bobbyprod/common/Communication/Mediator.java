package com.bobbyprod.common.Communication;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetManager;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.Interfaces.IMediator;
import com.bobbyprod.common.ProductionLine.ActivePartsList;
import com.bobbyprod.common.ProductionLine.ActiveProductsList;
import com.bobbyprod.common.ProductionLine.ProductionQueue;
import com.bobbyprod.common.Products.Product;
import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.ActionType;
import com.bobbyprod.common.Tasks.Task;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Mediator implements IMediator {
    private static Mediator instance = null;
    private List<Asset> assets; // List of all registered assets
    private Map<Asset, Task> assetTasks; // Map to keep track of current tasks for each asset
    private ProductionQueue productionQueue;// Queue to manage production of products
    private ActiveProductsList activeProductsList; // List of products currently in production

    /**
     * Mediator instance
     */
    public static Mediator getInstance() {
        if (instance == null) {
            instance = new Mediator();
        }
        return instance;
    }

    /**
     * Constructor
     */
    public Mediator() {
        assets = new ArrayList<>();
        assetTasks = new HashMap<>();
        productionQueue = new ProductionQueue();
        activeProductsList = new ActiveProductsList();
    }

    /**
     * Registers an asset with the mediator
     * @param asset Asset to be registered
     */
    public void registerAsset(Asset asset) {
        if (!assets.contains(asset)) {
            assets.add(asset);
            assetTasks.put(asset, null);
        }
    }

    public void startProduction() {
        System.out.println("Starting production");
        while (!productionQueue.isEmpty()) {
            Product product = productionQueue.getFirstInQueue();
            activeProductsList.addToActiveProductionList(product);
            Asset warehouseAsset = AssetManager.findAvailableAsset(assets, AssetType.WAREHOUSE);
            Task task = new Task(ActionType.PICK_ITEM, warehouseAsset);
            task.setProcessedBy(warehouseAsset);
            task.setDestination(warehouseAsset);
            task.setProduct(product);
            if (warehouseAsset != null) {
                assignTask(warehouseAsset, task);
            } else {
                System.out.println("No warehouse asset available");
            }
//            //System.out.println("Product added to active production list");
//            for (Part part : product.getPartsList()) {
//                //System.out.println("Processing part: " + part.getName());
//                activePartsList.addToActiveProductionList(part);
//                //System.out.println("Part added to active production list");
//                Task task = new Task(ActionType.PICK_ITEM, AssetType.WAREHOUSE, part);
//                //System.out.println("Creating task: " + task.getActionType());
//                Asset warehouseAsset = AssetManager.findAvailableAsset(assets,AssetType.WAREHOUSE);
//                //System.out.println("Finding available warehouse asset");
//                if (warehouseAsset != null) {
////                    System.out.println("Assigning task to warehouse asset");
//                    assignTask(warehouseAsset, task);
//                    //System.out.println("Task assigned to warehouse asset");
//                } else {
//                    //System.out.println("No warehouse asset available");
//                }
//            }
            productionQueue.removeFromQueue(product);
            //System.out.println("Product removed from production queue");
        }
        System.out.println("Production queue empty");
    }

    // Handles notifications from assets. Decides action based on event type and asset type.
    @Override
    public void notify(Asset asset, Task task) {
        if (!assets.contains(asset)) {
            System.out.println("Asset not registered: " + asset.getName());
            return;
        }
        switch (task.getStatus()) {
            case TASK_ACCEPTED:
                System.out.println(asset.getName() + " accepted task: " + task.getActionType());
                //assetTasks.put(asset, task);
                break;
            case TASK_COMPLETED:
                System.out.println(asset.getName() + " completed task: " + task.getActionType());
                //assetTasks.put(asset, null); // Clear the task as it is completed
                fleetManagement(asset, task);
                break;
            case TASK_FAILED:
                System.out.println(asset.getName() + " failed task: " + task.getActionType());
                //assetTasks.put(asset, null); // Clear the task on failure
                break;
        }
    }

    private void fleetManagement(Asset asset, Task task) {
        //System.out.println(asset.getName() + " completed task: " + task.getActionType());
        switch (task.getActionType()) {
            case PICK_ITEM:
                Task moveToWarehouse = new Task(ActionType.MOVE_TO_WAREHOUSE, AssetType.AGV, task.getProduct());
                asset = AssetManager.findAvailableAsset(assets, AssetType.AGV);
                assignTask(asset, moveToWarehouse);
                break;
            case MOVE_TO_WAREHOUSE:
                asset = AssetManager.findAvailableAsset(assets, AssetType.AGV);
                if (task.getProduct().isAssembled()) {
                    Task deliverItemToWarehouse = new Task(ActionType.PUT_ITEM_TO_WAREHOUSE, AssetType.AGV, task.getProduct());
                    assignTask(asset, deliverItemToWarehouse);
                } else {
                    Task pickItemFromWarehouse = new Task(ActionType.PICK_ITEM_FROM_WAREHOUSE, AssetType.AGV, task.getProduct());
                    assignTask(asset, pickItemFromWarehouse);
                }
                break;
            case PICK_ITEM_FROM_WAREHOUSE:
                asset = AssetManager.findAvailableAsset(assets, AssetType.AGV);
                Task moveToAssemblyTask = new Task(ActionType.MOVE_TO_ASSEMBLY_STATION, AssetType.AGV, task.getProduct());
                assignTask(asset, moveToAssemblyTask);
                break;
            case MOVE_TO_ASSEMBLY_STATION:
                asset = AssetManager.findAvailableAsset(assets, AssetType.AGV);
                if (task.getProduct().isAssembled()) {
                    Task pickItemFromAssemblyStation = new Task(ActionType.PICK_ITEM_FROM_ASSEMBLY_STATION, AssetType.AGV, task.getProduct());
                    assignTask(asset, pickItemFromAssemblyStation);
                } else {
                    Task deliverItemToAssemblyStation = new Task(ActionType.PUT_ITEM_TO_ASSEMBLY_STATION, AssetType.AGV, task.getProduct());
                    assignTask(asset, deliverItemToAssemblyStation);
                }
                break;
            case PUT_ITEM_TO_ASSEMBLY_STATION:
                asset = AssetManager.findAvailableAsset(assets, AssetType.ASSEMBLY_STATION);
                Task assembleItem = new Task(ActionType.ASSEMBLE_ITEM, AssetType.ASSEMBLY_STATION, task.getProduct());
                assignTask(asset, assembleItem);
                break;
            case ASSEMBLE_ITEM:
                asset = AssetManager.findAvailableAsset(assets, AssetType.ASSEMBLY_STATION);
                Task pickupFromAssemblyStation = new Task(ActionType.PICK_ITEM_FROM_ASSEMBLY_STATION, AssetType.AGV, task.getProduct());
                assignTask(asset, pickupFromAssemblyStation);
                break;
            case PICK_ITEM_FROM_ASSEMBLY_STATION:
                asset = AssetManager.findAvailableAsset(assets, AssetType.AGV);
                Task deliverToWarehouse = new Task(ActionType.MOVE_TO_WAREHOUSE, AssetType.AGV, task.getProduct());
                assignTask(asset, deliverToWarehouse);
                break;
            case PUT_ITEM_TO_WAREHOUSE:
                asset = AssetManager.findAvailableAsset(assets, AssetType.WAREHOUSE);
                Task insertItem = new Task(ActionType.INSERT_ITEM, AssetType.WAREHOUSE, task.getProduct());
                assignTask(asset, insertItem);
                activeProductsList.removeFromActiveProductionList(task.getProduct());
                break;
            case INSERT_ITEM:
                System.out.println("Product " + task.getProduct().getName() + " inserted into warehouse");
                System.out.println(task.getActionType());
                break;
        }
    }

    private void assignTask(Asset asset, Task task) {
        System.out.println("Assigning task " + task.getActionType() + " to " + asset.getName());
        //assetTasks.put(asset, task);
        asset.handleTask(task);
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public Map<Asset, Task> getAssetTasks() {
        return assetTasks;
    }

    public ProductionQueue getProductionQueue() {
        return productionQueue;
    }

//    public ActiveProductsList getActiveProductsList() {
//        return activeProductsList;
//    }
//
//    public ActivePartsList getActivePartsList() {
//        return activePartsList;
//    }

    public static void setInstance(Mediator instance) {
        Mediator.instance = instance;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public void setAssetTasks(Map<Asset, Task> assetTasks) {
        this.assetTasks = assetTasks;
    }

    public void setProductionQueue(ProductionQueue productionQueue) {
        this.productionQueue = productionQueue;
    }

//    public void setActiveProductsList(ActiveProductsList activeProductsList) {
//        this.activeProductsList = activeProductsList;
//    }
//
//    public void setActivePartsList(ActivePartsList activePartsList) {
//        this.activePartsList = activePartsList;
//    }

    public void addAsset(Asset asset) {
        assets.add(asset);
    }

    public void removeAsset(Asset asset) {
        assets.remove(asset);
    }

    public void addTask(Asset asset, Task task) {
        assetTasks.put(asset, task);
    }

    public void removeTask(Asset asset) {
        assetTasks.remove(asset);
    }

    public void addProduct(Product product) {
        productionQueue.addToQueue(product);
    }

    public void removeProduct(Product product) {
        productionQueue.removeFromQueue(product);
    }
}
