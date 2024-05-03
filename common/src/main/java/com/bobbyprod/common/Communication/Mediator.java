package com.bobbyprod.common.Communication;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.Interfaces.IMediator;
import com.bobbyprod.common.ProductionLine.ProductionQueue;
import com.bobbyprod.common.Products.Product;
import com.bobbyprod.common.Products.ProductStatus;
import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.ActionType;
import com.bobbyprod.common.Tasks.Task;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Mediator implements IMediator {
    private List<Asset> assets; // List of all registered assets
    private Map<Asset, Task> assetTasks; // Map to keep track of current tasks for each asset
    private ProductionQueue productionQueue;// Queue to manage production of products

    public Mediator() {
        assets = new ArrayList<>();
        assetTasks = new HashMap<>();
        productionQueue = new ProductionQueue();
    }

    // Registers assets with the mediator
    public void registerAsset(Asset asset) {
        assets.add(asset);
        assetTasks.put(asset, null); // Initialize with no task assigned
    }

    public void startProduction() {
        // While the production queue is not empty
        while (!productionQueue.isEmpty()) {
            // Get the first product in the queue
            Product product = productionQueue.getFirstInQueue();

            // Create a new task to pick item from warehouse
            Task task = new Task();
            task.setActionType(ActionType.PICK_ITEM_FROM_WAREHOUSE);
            task.setCompatibleAssetType(AssetType.WAREHOUSE); // WAREHOUSE type assets are responsible for picking items

            // Find an available asset to assign the task
            for (Asset asset : assets) {
                if (asset.getState() == AssetState.IDLE && asset.getType() == AssetType.WAREHOUSE) {
                    assignTask(asset, task);
                    break;
                }
            }

            // Remove the product from the queue as it is now being processed
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
        // Determine next step based on the task completed
        //Combined tasks:
        switch (task.getActionType()) {
            case MOVE_TO_WAREHOUSE:
                if (asset.getType() == AssetType.AGV && asset.getState() == AssetState.IDLE) {
                    Task newTask = new Task();
                    newTask.setActionType(ActionType.MOVE_TO_ASSEMBLY_STATION_PARTS);
                    assignTask(asset, newTask); // Assign the task to the same AGV
                    break;
                }
            case MOVE_TO_ASSEMBLY_STATION_PARTS:
        }
    }

    private void assignTask(Asset asset, Task task) {
        System.out.println("Assigning task " + task.getActionType() + " to " + asset.getName());
        assetTasks.put(asset, task);
        asset.handleTask(task);
    }
}
