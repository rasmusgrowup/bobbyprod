package com.bobbyprod.agv;

import com.bobbyprod.agv.controller.AgvController;
import com.bobbyprod.agv.service.AgvService;
import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.Communication.Mediator;
import com.bobbyprod.common.Interfaces.IMediator;
import com.bobbyprod.common.Interfaces.Observer;
import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;
import com.bobbyprod.common.Tasks.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Agv extends Asset implements Observer {
    private AgvService agvService;
    private AgvController agvController;
    private AssetState state;
    private int batteryLevel;
    protected Mediator mediator;

    public Agv() {
        super("AGV", "AGV-1", AssetType.AGV);
        this.batteryLevel = 100;
        this.mediator = Mediator.getInstance();
        this.agvController = new AgvController(new RestTemplate());
        this.agvService = new AgvService(agvController);
    }

    @Autowired
    public void setAgvService(AgvService agvService) {
        this.agvService = agvService;
    }

    @Autowired
    public void setAgvController(AgvController agvController) {
        this.agvController = agvController;
        this.agvController.addObserver(this);
        this.state = agvController.getState();
    }

    @Override
    public boolean processTask(Task task) {
        this.state = AssetState.BUSY;
        task.setStatus(TaskStatus.TASK_ACCEPTED);
        mediator.notify(this, task);
        if (!agvService.handleTask(task)) {
            task.setStatus(TaskStatus.TASK_FAILED);
            mediator.notify(this, task);
            this.state = AssetState.ERROR;
            return false;
        } else {
            while (this.state == AssetState.BUSY) {
            }
            task.setStatus(TaskStatus.TASK_COMPLETED);
            mediator.notify(this, task);
            return true;
        }
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    @Override
    public void update() {
        this.state = agvController.getState();
        setBatteryLevel(batteryLevel = agvController.getBatteryLevel());
        //System.out.println("AGV state: " + state + ", battery level: " + batteryLevel + "%");
    }
}
