package com.bobbyprod.common.Communication;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Interfaces.IMediator;
import com.bobbyprod.common.Tasks.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// The Mediator class acts as a mediator by coordinating interactions between various components such as AGVs,
// assembly stations, and warehouses. It handles tasks based on events triggered by these assets.
@Component
public class Mediator implements IMediator {
    private List<Asset> assets; // List of all registered assets

    public Mediator() {
        assets = new ArrayList<>();
    }

    // Registers assets with the mediator
    public void registerAsset(Asset asset) {
        assets.add(asset);
    }

    // Handles notifications from assets. Decides action based on event type and asset type.
    @Override
    public void notify(Asset asset, String event, Task task) {
        // Ensure task is compatible with the asset's type before assigning
        if (task.getCompatibleAssetType() == asset.getType()) {
            switch (event) {
                case "TaskAssigned":
                    System.out.println("Task " + task.getActionType() + " assigned to " + asset.getName());
                    asset.handleTask(task);
                    break;
                // Additional handling for other events can be added here
            }
        } else {
            System.out.println("Task " + task.getActionType() + " not compatible with " + asset.getName() + ", type: " + asset.getType());
        }
    }
}
