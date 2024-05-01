package com.bobbyprod.agv.service;

import com.bobbyprod.agv.Agv;
import com.bobbyprod.agv.controller.AgvController;
import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgvService {
    private final AgvController agvController;
    private final Agv agv;
    private AssetState state;

    @Autowired
    public AgvService(AgvController agvController, Agv agv) {
        this.agvController = agvController;
        this.agv = agv;
    }

    public boolean handleTask(Task task) {
        agv.setState(AssetState.BUSY);
        boolean result = false;
        switch (task.getActionType()) {
            case MOVE_TO_WAREHOUSE:
                result = agvController.loadProgram("MoveToStorageOperation", 1);
                if (result) {
                    result = agvController.changeState(2);
                }
                break;
            case MOVE_TO_ASSEMBLY_STATION:
                // Logic to move AGV to the assembly station
                break;
            case MOVE_TO_CHARGER:
                // Logic to move AGV to the charger
                break;
            // Handle other action types
        }
        if (result) {
            agv.setState(AssetState.IDLE);
        } else {
            agv.setState(AssetState.ERROR);
        }
        return result;
    }

//    public AssetState getState() {
//        // Logic to update the state of the AGV
//    }
}