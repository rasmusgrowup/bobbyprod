package com.bobbyprod.warehouse;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.Communication.Mediator;
import com.bobbyprod.common.Interfaces.IMediator;
import com.bobbyprod.common.Products.ProductStatus;
import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;
import com.bobbyprod.common.Tasks.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class Warehouse extends Asset {
    private AssetState state;
    private WarehouseService wService;
    private IMediator mediator;

    public Warehouse() {
        super("Warehouse1", "Main warehouse", AssetType.WAREHOUSE);
        //wService.clearInventory();
        //wService.fillInventory();
        //this.state = wService.checkState();
        this.state = AssetState.IDLE;
        this.wService = new WarehouseService();
        this.mediator = Mediator.getInstance();
    }

    @Override
    public boolean processTask(Task task) {
        task.setStatus(TaskStatus.TASK_ACCEPTED);
        mediator.notify(this, task);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        task.setStatus(TaskStatus.TASK_COMPLETED);
        mediator.notify(this, task);
        return true;
    }
}
