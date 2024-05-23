package com.bobbyprod.agv;

import com.bobbyprod.agv.controller.AgvController;
import com.bobbyprod.agv.service.AgvService;
import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.Communication.Mediator;
import com.bobbyprod.common.Interfaces.IMediator;
import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;
import com.bobbyprod.common.Tasks.TaskStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Agv extends Asset {
    private AgvService agvService;
    private AgvController agvController;
    private AssetState state;
    private int batteryLevel;
    protected IMediator mediator;

    public Agv() {
        super("AGV", "AGV-1", AssetType.AGV);
        this.agvController = new AgvController(new RestTemplate());
        this.agvService = new AgvService(agvController);
        this.mediator = Mediator.getInstance();
        this.batteryLevel = agvController.getBatteryLevel();
        this.state = AssetState.IDLE;
    }

    @Override
    public boolean processTask(Task task) {

        this.batteryLevel = getBatteryLevel();
        task.setStatus(TaskStatus.TASK_ACCEPTED);
        this.state = AssetState.BUSY;
        mediator.notify(this, task);
        if (!agvService.handleTask(task)) {
            task.setStatus(TaskStatus.TASK_FAILED);
            mediator.notify(this, task);
            this.state = AssetState.ERROR;
            return false;
        } else {
            System.out.println("Processing task ...");
            while (this.state == AssetState.BUSY) {
                setState(agvController.getState());
            }
            task.setStatus(TaskStatus.TASK_COMPLETED);
            mediator.notify(this, task);
            return true;
        }
    }

    public int getBatteryLevel() {
        return agvController.getBatteryLevel();
    }

    public AssetState getState() {
        return state;
    }

    public void setState(AssetState state) {
        this.state = state;
    }
}
