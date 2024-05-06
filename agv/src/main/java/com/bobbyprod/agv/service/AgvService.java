package com.bobbyprod.agv.service;

import com.bobbyprod.agv.Agv;
import com.bobbyprod.agv.controller.AgvController;
import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Communication.Mediator;
import com.bobbyprod.common.Interfaces.IMediator;
import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgvService {
    private final AgvController agvController;
    private AssetState state;

    @Autowired
    public AgvService(AgvController agvController) {
        this.agvController = agvController;
    }

    public boolean handleTask(Task task) {
        boolean result = false;
        switch (task.getActionType()) {
            case MOVE_TO_WAREHOUSE:
                //Drains 5% power
                result = agvController.loadProgram("MoveToStorageOperation", 1);
                if (result) {
                    result = agvController.changeState(2);
                }
                break;
            case MOVE_TO_ASSEMBLY_STATION:
                //Drains 5% power
                result = agvController.loadProgram("MoveToAssemblyOperation", 1);
                if (result) {
                    result = agvController.changeState(2);
                }
                break;
            case MOVE_TO_CHARGER:
                //Recharges the battery to full
                result = agvController.loadProgram("MoveToChargerOperation", 1);
                if (result) {
                    result = agvController.changeState(2);
                }
                break;
            case PICK_ITEM_FROM_WAREHOUSE:
                //Drains 1% power
                result = agvController.loadProgram("PickWarehouseOperation", 1);
                if (result) {
                    result = agvController.changeState(2);
                }
                break;
            case PUT_ITEM_TO_WAREHOUSE:
                //Drains 1% power
                result = agvController.loadProgram("PutWarehouseOperation", 1);
                if (result) {
                    result = agvController.changeState(2);
                }
                break;
            case PICK_ITEM_FROM_ASSEMBLY_STATION:
                //Drains 1% power
                result = agvController.loadProgram("PickAssemblyOperation", 1);
                if (result) {
                    result = agvController.changeState(2);
                }
                break;
            case PUT_ITEM_TO_ASSEMBLY_STATION:
                //Drains 1% power
                result = agvController.loadProgram("PutAssemblyOperation", 1);
                if (result) {
                    result = agvController.changeState(2);
                }
                break;
            default:
                System.out.println("Unknown action type: " + task.getActionType());
        }

        if (agvController.getBatteryLevel() <= 5) {
            agvController.loadProgram("MoveToChargerOperation", 1);
            agvController.changeState(2);
        }

        return result;
    }
}