package com.bobbyprod.assemblystation;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.Communication.Mediator;
import com.bobbyprod.common.Products.ProductStatus;
import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;
import com.bobbyprod.common.Tasks.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class AssemblyStation extends Asset {
    private AssetState state;
    private final Mediator mediator;
    private final AssemblyStationService assemblyStationService;

    public AssemblyStation(AssemblyStationService assemblyStationService) {
        super("id", "AssemblyStation", AssetType.ASSEMBLY_STATION);
        this.state = AssetState.IDLE;
        this.mediator = Mediator.getInstance();
        this.assemblyStationService = assemblyStationService;
    }

    @Override
    public boolean processTask(Task task) {
        setState(AssetState.BUSY);
        task.setStatus(TaskStatus.TASK_ACCEPTED);
        task.getProduct().setStatus(ProductStatus.BEING_ASSEMBLED);
        mediator.notify(this, task);
        boolean assemblyStarted = assemblyStationService.startAssemblyProcess(new AssemblyCommand(1));

        if (assemblyStarted) {
            System.out.println(this.getName() + "Processing task...");

            try {
                Thread.sleep(2000); // Sleep for 2 seconds to give the machine time to change its state
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (fetchMachineStatus() == AssetState.BUSY) {
                System.out.println("Machine is busy, waiting for it to finish...");
                try {
                    Thread.sleep(100); // Sleep for 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            setState(fetchMachineStatus());
            task.getProduct().setAssembled(true);
            task.setStatus(TaskStatus.TASK_COMPLETED);
            mediator.notify(this, task);
            return true;
        } else {
            task.setStatus(TaskStatus.TASK_FAILED);
            mediator.notify(this, task);
            return false;
        }
    }

    private AssetState fetchMachineStatus() {
        return assemblyStationService.getState();
    }

    @Override
    public AssetState getState() {
        return state;
    }

    @Override
    public void setState(AssetState state) {
        this.state = state;
    }
}

