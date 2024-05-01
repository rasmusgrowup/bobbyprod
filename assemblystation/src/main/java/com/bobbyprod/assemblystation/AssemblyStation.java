package com.bobbyprod.assemblystation;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;
import com.bobbyprod.common.Interface.IMediator;

public class AssemblyStation extends Asset {

    public AssemblyStation(String id, String name, AssetState state, AssetType type, IMediator mediator) {
        super(id, name, AssetState.IDLE, AssetType.ASSEMBLY_STATION, mediator);
    }

    
    public void acceptTask(Task task) {
        System.out.println("AssemblyStation " + getName() + " is processing the task: " + task.getActionType());
        if (processTask(task)) {
            System.out.println("Task successfully completed by " + getName());
            this.setState(AssetState.IDLE);  // Update state to IDLE on success
        } else {
            System.out.println("Task failed by " + getName());
            this.setState(AssetState.ERROR); // Update state to ERROR on failure
        }
    }
    @Override
    public boolean processTask(Task task) {
        System.out.println("Assembly " + getName() + " is processing the task: " + task.getActionType());
        // Placeholder for task processing logic. Real implementation would depend on the task specifics.
        switch (task.getActionType()) {
            case ASSEMBLE_ITEM:
                // Logic to handle assembling an item
                System.out.println("Assembling item...");
                return true;
            default:
                // Handle other possible actions
                System.out.println("Unhandled task type: " + task.getActionType());
                return false;
        }
    }
}

