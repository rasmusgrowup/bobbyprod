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

@Component
public class Agv extends Asset {
    private AgvService agvService;
    private AssetState state;
    private int batteryLevel;
    protected IMediator mediator;

    public Agv(AgvService agvService) {
        super("AGV", "AGV-1", AssetType.AGV);
        this.agvService = agvService;
        this.mediator = Mediator.getInstance();
        this.batteryLevel = agvService.getAgvController().getBatteryLevel();
        this.state = AssetState.IDLE;
    }

    public void handleRecharge(){
        int battery = getBatteryLevel();
        if(battery <= 5){
            boolean result = agvService.getAgvController().loadProgram("MoveToChargerOperation", 1);
            state = AssetState.CHARGING;
            if (result) {
                result = agvService.getAgvController().changeState(2);
                System.out.println("Recharging");
                while (batteryLevel < 100){
                    batteryLevel = getBatteryLevel();
                    //System.out.println("Battery Level: " + batteryLevel);
                }
                //System.out.println("Fully Charged");
            }
        }
    }

    @Override
    public boolean processTask(Task task) {
        this.batteryLevel = getBatteryLevel();
        task.setStatus(TaskStatus.TASK_ACCEPTED);
        handleRecharge();
        this.state = AssetState.BUSY;
        mediator.notify(this, task);
        if (!agvService.handleTask(task)) {
            task.setStatus(TaskStatus.TASK_FAILED);
            mediator.notify(this, task);
            this.state = AssetState.ERROR;
            return false;
        } else {
            while (this.state == AssetState.BUSY) {
                setState(agvService.getAgvController().getState());
                //System.out.println(this.getName() + " is processing task " + task.getActionType());
            }
            task.setStatus(TaskStatus.TASK_COMPLETED);
            mediator.notify(this, task);
            return true;
        }
    }

    public int getBatteryLevel() {
        return agvService.getAgvController().getBatteryLevel();
    }

    public AssetState getState() {
        return state;
    }

    public void setState(AssetState state) {
        this.state = state;
    }
}
