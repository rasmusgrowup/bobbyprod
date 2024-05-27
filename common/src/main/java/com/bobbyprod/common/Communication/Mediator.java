package com.bobbyprod.common.Communication;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetManager;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.Interfaces.IMediator;
import com.bobbyprod.common.ProductionLine.ActiveProductsList;
import com.bobbyprod.common.ProductionLine.AssetsList;
import com.bobbyprod.common.ProductionLine.FinishedProducts;
import com.bobbyprod.common.ProductionLine.ProductionQueue;
import com.bobbyprod.common.Products.Product;
import com.bobbyprod.common.Tasks.ActionType;
import com.bobbyprod.common.Tasks.Task;
import org.springframework.stereotype.Component;

@Component
public class Mediator implements IMediator {
    private static Mediator instance = null;
    private AssetsList assets;// List of all registered assets
    private ProductionQueue productionQueue;// Queue to manage production of products
    private ActiveProductsList activeProductsList; // List of products currently in production
    private FinishedProducts finishedProducts; // List of products currently in production

    public Mediator() {
        this.assets = AssetsList.getInstance();
        this.productionQueue = ProductionQueue.getInstance();
        this.activeProductsList = ActiveProductsList.getInstance();
        this.finishedProducts = FinishedProducts.getInstance();
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
     * Registers an asset with the mediator
     * @param asset Asset to be registered
     */
    @Override
    public void registerAsset(Asset asset) {
        if (!assets.contains(asset)) {
            assets.addAsset(asset);
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
                availableAsset = waitForIdleAsset(AssetType.AGV);
                newTask = new Task(ActionType.PICK_ITEM_FROM_ASSEMBLY_STATION, AssetType.AGV, task.getProduct());
                break;
            case PICK_ITEM_FROM_ASSEMBLY_STATION:
                availableAsset = waitForIdleAsset(AssetType.AGV);
                newTask = new Task(ActionType.MOVE_TO_WAREHOUSE, AssetType.AGV, task.getProduct());
                break;
            case PUT_ITEM_TO_WAREHOUSE:
                availableAsset = waitForIdleAsset(AssetType.WAREHOUSE);
                newTask = new Task(ActionType.INSERT_ITEM, AssetType.WAREHOUSE, task.getProduct());
                ProductionQueue.getInstance().removeFromQueue(task.getProduct());
                ActiveProductsList.getInstance().removeFromActiveProductionList(task.getProduct());
                FinishedProducts.getInstance().addToFinishedProducts(task.getProduct());
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

    public static void setInstance(Mediator instance) {
        Mediator.instance = instance;
    }
}
