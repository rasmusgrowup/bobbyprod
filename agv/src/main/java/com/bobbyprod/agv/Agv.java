package com.bobbyprod.agv;

import com.bobbyprod.agv.controller.AgvController;
import com.bobbyprod.agv.service.AgvService;
import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.Interfaces.IMediator;
import com.bobbyprod.common.Interfaces.Observer;
import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Agv extends Asset implements Observer {
    private AgvService agvService;
    private AgvController agvController;
    private AssetState state;
    private int batteryLevel;

    public Agv(IMediator mediator) {
        super("AGV", "AGV-1", AssetType.AGV, mediator);
        this.batteryLevel = 100;
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
        return agvService.handleTask(task);
    }

    @Override
    public AssetState getState() {
        return state;
    }

    @Override
    public void setState(AssetState state) {
        this.state = state;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    @Override
    public void update() {
        setState(agvController.getState());
        setBatteryLevel(batteryLevel = agvController.getBatteryLevel());
        System.out.println("AGV state: " + state + ", battery level: " + batteryLevel + "%");
    }
}
