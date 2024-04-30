package com.bobbyprod.agv;

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
        return result;
    }
}