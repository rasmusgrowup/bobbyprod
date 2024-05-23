package com.bobbyprod.common.Communication;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetManager;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.Interfaces.IMediator;
import com.bobbyprod.common.ProductionLine.ActiveProductsList;
import com.bobbyprod.common.ProductionLine.ProductionQueue;
import com.bobbyprod.common.Products.Product;
import com.bobbyprod.common.Tasks.ActionType;
import com.bobbyprod.common.Tasks.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Mediator implements IMediator {
    private static Mediator instance = null;
    private List<Asset> assets;// List of all registered assets
    //private Map<Asset, Task> assetTasks; // Map to keep track of current tasks for each asset
    private ProductionQueue productionQueue;// Queue to manage production of products
    private ActiveProductsList activeProductsList; // List of products currently in production

    public Mediator() {
        this.assets = new ArrayList<>();
        this.productionQueue = new ProductionQueue();
        this.activeProductsList = new ActiveProductsList();
    }

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
    @Autowired
    public Mediator(List<Asset> assets, /*Map<Asset, Task> assetTasks,*/ ProductionQueue productionQueue, ActiveProductsList activeProductsList) {
        this.assets = assets;
        //this.assetTasks = assetTasks;
        this.productionQueue = productionQueue;
        this.activeProductsList = activeProductsList;
    }

    /**
     * Registers an asset with the mediator
     * @param asset Asset to be registered
     */
    public void registerAsset(Asset asset) {
        if (!assets.contains(asset)) {
            assets.add(asset);
            //assetTasks.put(asset, null);
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
        Asset availableAsset = null;
        Task newTask = null;

        switch (task.getActionType()) {
            case PICK_ITEM:
                newTask = new Task(ActionType.MOVE_TO_WAREHOUSE, AssetType.AGV, task.getProduct());
                availableAsset = waitForIdleAsset(AssetType.AGV);
                break;
            case MOVE_TO_WAREHOUSE:
                availableAsset = waitForIdleAsset(AssetType.AGV);
                if (task.getProduct().isAssembled()) {
                    newTask = new Task(ActionType.PUT_ITEM_TO_WAREHOUSE, AssetType.AGV, task.getProduct());
                } else {
                    newTask = new Task(ActionType.PICK_ITEM_FROM_WAREHOUSE, AssetType.AGV, task.getProduct());
                }
                break;
            case PICK_ITEM_FROM_WAREHOUSE:
                availableAsset = waitForIdleAsset(AssetType.AGV);
                newTask = new Task(ActionType.MOVE_TO_ASSEMBLY_STATION, AssetType.AGV, task.getProduct());
                break;
            case MOVE_TO_ASSEMBLY_STATION:
                availableAsset = waitForIdleAsset(AssetType.AGV);
                if (task.getProduct().isAssembled()) {
                    newTask = new Task(ActionType.PICK_ITEM_FROM_ASSEMBLY_STATION, AssetType.AGV, task.getProduct());
                } else {
                    newTask = new Task(ActionType.PUT_ITEM_TO_ASSEMBLY_STATION, AssetType.AGV, task.getProduct());
                }
                break;
            case PUT_ITEM_TO_ASSEMBLY_STATION:
                availableAsset = waitForIdleAsset(AssetType.ASSEMBLY_STATION);
                newTask = new Task(ActionType.ASSEMBLE_ITEM, AssetType.ASSEMBLY_STATION, task.getProduct());
                break;
            case ASSEMBLE_ITEM:
                availableAsset = waitForIdleAsset(AssetType.ASSEMBLY_STATION);
                newTask = new Task(ActionType.PICK_ITEM_FROM_ASSEMBLY_STATION, AssetType.AGV, task.getProduct());
                break;
            case PICK_ITEM_FROM_ASSEMBLY_STATION:
                availableAsset = waitForIdleAsset(AssetType.AGV);
                newTask = new Task(ActionType.MOVE_TO_WAREHOUSE, AssetType.AGV, task.getProduct());
                break;
            case PUT_ITEM_TO_WAREHOUSE:
                availableAsset = waitForIdleAsset(AssetType.WAREHOUSE);
                newTask = new Task(ActionType.INSERT_ITEM, AssetType.WAREHOUSE, task.getProduct());
                activeProductsList.removeFromActiveProductionList(task.getProduct());
                break;
            case INSERT_ITEM:
                System.out.println("Product " + task.getProduct().getName() + " inserted into warehouse");
                return;
        }

        if (availableAsset != null && newTask != null) {
            assignTask(availableAsset, newTask);
        } else {
            System.out.println("No available asset for action: " + task.getActionType());
        }
    }

    private Asset waitForIdleAsset(AssetType type) {
        Asset availableAsset = null;
        while (availableAsset == null) {
            availableAsset = AssetManager.findAvailableAsset(assets, type);
            if (availableAsset == null) {
                try {
                    Thread.sleep(1000); // Wait for 1 second before retrying
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Waiting for idle asset interrupted");
                    break;
                }
            }
        }
        return availableAsset;
    }


    private void assignTask(Asset asset, Task task) {
        System.out.println("Assigning task " + task.getActionType() + " to " + asset.getName());
        //assetTasks.put(asset, task);
        asset.processTask(task);
    }

    public List<Asset> getAssets() {
        return assets;
    }

//    public Map<Asset, Task> getAssetTasks() {
//        return assetTasks;
//    }

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

//    public void setAssetTasks(Map<Asset, Task> assetTasks) {
//        this.assetTasks = assetTasks;
//    }

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

//    public void addTask(Asset asset, Task task) {
//        assetTasks.put(asset, task);
//    }
//
//    public void removeTask(Asset asset) {
//        assetTasks.remove(asset);
//    }

    public void addProduct(Product product) {
        productionQueue.addToQueue(product);
    }

    public void removeProduct(Product product) {
        productionQueue.removeFromQueue(product);
    }
}
