package com.bobbyprod.agv;

import com.bobbyprod.agv.service.AgvService;
import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Agv extends Asset {
    private AgvService agvService;
    private AssetState state;

    public Agv() {
        super("AGV", "AGV-1", AssetType.AGV);
        this.state = AssetState.IDLE;
    }

    @Autowired
    public void setAgvService(AgvService agvService) {
        this.agvService = agvService;
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

    @Override
    public void updateState(AssetState state) {
        setState(state);
    }
}
