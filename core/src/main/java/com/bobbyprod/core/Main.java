package com.bobbyprod.core;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.assemblystation.AssemblyStation;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.Communication.Mediator;
import com.bobbyprod.common.Tasks.ActionType;
import com.bobbyprod.common.Tasks.Task;

public class Main {
    public static void main(String[] args) {
        // Create the mediator
        Mediator mediator = new Mediator();

        // Create asset
        Asset assemblyStation = new AssemblyStation("AS1", "Main Assembly Station");

        // Register asset with the mediator
        mediator.registerAsset(assemblyStation);

        // Create task
        Task assembleItem = new Task();
        assembleItem.setActionType(ActionType.ASSEMBLE_ITEM);
        assembleItem.setCompatibleAssetType(AssetType.ASSEMBLY_STATION);

        // Assign tasks via mediator
        System.out.println("\nAssigning ASSEMBLE_ITEM task to Assembly Station:");
    }
}
