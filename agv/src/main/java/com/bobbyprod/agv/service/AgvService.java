package com.bobbyprod.agv.service;

import com.bobbyprod.agv.controller.AgvController;
import com.bobbyprod.common.Products.ProductStatus;
import com.bobbyprod.common.Tasks.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgvService {
    private final AgvController agvController;

    @Autowired
    public AgvService(AgvController agvController) {
        this.agvController = agvController;
    }

    public boolean handleTask(Task task) {
        boolean result = false;
        switch (task.getActionType()) {
            case MOVE_TO_WAREHOUSE:
                //Drains 5% power
                //task.getProduct().setStatus(ProductStatus.EN_ROUTE_TO_WAREHOUSE);
                result = agvController.loadProgram("MoveToStorageOperation", 1);
                if (result) {
                    result = agvController.changeState(2);
                }
                if (task.getProduct().isAssembled()) {
                    task.getProduct().setStatus(ProductStatus.EN_ROUTE_TO_WAREHOUSE);
                }
                break;
            case MOVE_TO_ASSEMBLY_STATION:
                //Drains 5% power
                task.getProduct().setStatus(ProductStatus.EN_ROUTE_TO_ASSEMBLY_STATION);
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
                task.getProduct().setStatus(ProductStatus.TRANSFERRING_TO_AGV);
                result = agvController.loadProgram("PickWarehouseOperation", 1);
                if (result) {
                    result = agvController.changeState(2);
                }
                break;
            case PUT_ITEM_TO_WAREHOUSE:
                //Drains 1% power
                task.getProduct().setStatus(ProductStatus.TRANSFERRING_TO_WAREHOUSE);
                result = agvController.loadProgram("PutWarehouseOperation", 1);
                if (result) {
                    result = agvController.changeState(2);
                }
                break;
            case PICK_ITEM_FROM_ASSEMBLY_STATION:
                //Drains 1% power
                task.getProduct().setStatus(ProductStatus.TRANSFERRING_TO_AGV);
                result = agvController.loadProgram("PickAssemblyOperation", 1);
                if (result) {
                    result = agvController.changeState(2);
                }
                break;
            case PUT_ITEM_TO_ASSEMBLY_STATION:
                //Drains 1% power
                task.getProduct().setStatus(ProductStatus.TRANSFERRING_TO_ASSEMBLY_STATION);
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

    public AgvController getAgvController() {
        return agvController;
    }
}