package com.bobbyprod.warehouse;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.Interfaces.IMediator;
import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;

public class Warehouse extends Asset {
    private AssetState state;
    private WarehouseService wService;

    public Warehouse(String id, String name, IMediator mediator){
        super(id, name, AssetType.WAREHOUSE, mediator);
        wService.clearInventory();
        wService.fillInventory();
        this.state = wService.checkState();
        this.wService = new WarehouseService();
    }

    @Override
    public boolean processTask(Task task) {
        return false;
    }

    @Override
    public AssetState getState() {
        return null;
    }

    @Override
    public void setState(AssetState state) {

    }

    @Override
    public void updateState(AssetState state) {

    }
}
