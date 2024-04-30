package com.bobbyprod.agv;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;

public class Agv extends Asset {
    private final AgvService agvService;

    public Agv(AgvService agvService) {
        super("AGV", "AGV-1", AssetState.IDLE, AssetType.AGV);
        this.agvService = agvService;
    }

    @Override
    public boolean processTask(Task task) {
        return agvService.handleTask(task);
    }

    @Override
    public void updateState(AssetState state) {
        setState(state);
    }
}
