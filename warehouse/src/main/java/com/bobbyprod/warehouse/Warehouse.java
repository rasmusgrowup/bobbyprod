package com.bobbyprod.warehouse;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.Communication.Mediator;
import com.bobbyprod.common.Interfaces.Observer;
import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;
import com.bobbyprod.common.Tasks.TaskStatus;

public class Warehouse extends Asset implements Observer {
    private AssetState state;
    private WarehouseService wService = new WarehouseService();
    protected Mediator mediator = new Mediator();

    public Warehouse(String id, String name){
        super(id, name, AssetType.WAREHOUSE);
        this.wService = new WarehouseService();
    }

    @Override
    public boolean processTask(Task task) {
        task.setStatus(TaskStatus.TASK_ACCEPTED);
        mediator.notify(this,task);
        if(wService.handleTask(task)){
            task.setStatus(TaskStatus.TASK_COMPLETED);
            mediator.notify(this,task);
            return true;
        } else {
            task.setStatus(TaskStatus.TASK_FAILED);
            mediator.notify(this,task);
            return false;
        }
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
    public void update() {
        setState(wService.checkState());
    }
}
