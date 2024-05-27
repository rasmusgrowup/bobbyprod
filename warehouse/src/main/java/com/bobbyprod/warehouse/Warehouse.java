package com.bobbyprod.warehouse;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.Communication.Mediator;
import com.bobbyprod.common.Interfaces.IMediator;
import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;
import com.bobbyprod.common.Tasks.TaskStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Warehouse extends Asset{
    private AssetState state;
    private WarehouseService wService;
    private String[] invArray;
    private IMediator mediator;

    public Warehouse(WarehouseService wService){
        super("id - 1", "warehouse 1", AssetType.WAREHOUSE);
        this.wService = wService;
        this.state = wService.getWarehouseController().pollWarehouseStatus();
        this.mediator = Mediator.getInstance();
    }

    @Override
    public boolean processTask(Task task) {
        task.setStatus(TaskStatus.TASK_ACCEPTED);
        mediator.notify(this,task);
        if(wService.handleTask(task)){
            this.state = AssetState.BUSY;
            while (state == AssetState.BUSY) {
                this.state = wService.getWarehouseController().pollWarehouseStatus();
            }
            task.setStatus(TaskStatus.TASK_COMPLETED);
            mediator.notify(this,task);
            invArray = wService.setInventoryArray();
            return true;
        } else {
            task.setStatus(TaskStatus.TASK_FAILED);
            mediator.notify(this,task);
            return false;
        }
    }

    public String[] getInvArray(){
        return invArray;
    }

    @Override
    public AssetState getState() {
        return state;
    }

    @Override
    public void setState(AssetState state) {
        this.state = state;
    }

    @Scheduled(fixedRate = 1000)
    public void updateState() {
        setState(wService.getWarehouseController().pollWarehouseStatus());
    }

    public WarehouseService getwService() {
        return this.wService;
    }
}
