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
    private WarehouseController wController;
    private String[] invArray;
    private IMediator mediator;

    public Warehouse(){
        super("id - 1", "warehouse 1", AssetType.WAREHOUSE);
        this.wController = new WarehouseController();
        this.wService = new WarehouseService();
        this.state = wController.pollWarehouseStatus();
        this.mediator = Mediator.getInstance();
    }

    @Override
    public boolean processTask(Task task) {
        task.setStatus(TaskStatus.TASK_ACCEPTED);
        mediator.notify(this,task);
        if(wService.handleTask(task)){
            do{
                wController.pollWarehouseStatus();
            }while(state == AssetState.BUSY);
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
        setState(wController.pollWarehouseStatus());
    }

    public WarehouseService getwService() {
        return this.wService;
    }
}
