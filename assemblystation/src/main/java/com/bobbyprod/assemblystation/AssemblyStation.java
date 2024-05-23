package com.bobbyprod.assemblystation;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.Communication.Mediator;
import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;
import com.bobbyprod.common.Tasks.TaskStatus;
import org.eclipse.paho.client.mqttv3.MqttException;

public class AssemblyStation extends Asset {
    private AssetState state;

    private ASClient mqttClient;
    private Mediator mediator;
    public AssemblyStation() {
        super("1", "Main Assembly Station", AssetType.ASSEMBLY_STATION);
        this.state = AssetState.IDLE;
        this.mediator = Mediator.getInstance();
//        this.mqttClient = new ASClient(this);
//        this.mediator = Mediator.getInstance();
//        try {
//            mqttClient.connect();  // Attempt to connect to the MQTT broker
//            mqttClient.subscribe("emulator/status");// Subscribe to the status topic
//        } catch (MqttException e) {
//            System.out.println("Failed to connect to MQTT broker: " + e.getMessage());
//        }
    }


    public void acceptTask(Task task) {
        System.out.println("AssemblyStation " + getName() + " is processing the task: " + task.getActionType());
        if (processTask(task)) {
            System.out.println("Task successfully completed by " + getName());
            this.setState(AssetState.BUSY);  // Update state to IDLE on success
        } else {
            System.out.println("Task failed by " + getName());
            this.setState(AssetState.ERROR); // Update state to ERROR on failure
        }
    }
    @Override
    public boolean processTask(Task task) {
//        try {
//            task.setStatus(TaskStatus.TASK_ACCEPTED);
//            mediator.notify(this, task);
//            switch (task.getActionType()) {
//                case ASSEMBLE_ITEM:
//                    AssemblyCommand command = new AssemblyCommand(1);
//                    mqttClient.publish("emulator/operation", command);
//                    task.setStatus(TaskStatus.TASK_COMPLETED);
//                    mediator.notify(this, task);
//                    return true;
//                default:
//                    task.setStatus(TaskStatus.TASK_FAILED);
//                    mediator.notify(this, task);
//                    return false;
//            }
//        } catch (MqttException e) {
//            System.out.println("Failed to publish MQTT message: " + e.getMessage());
//            task.setStatus(TaskStatus.TASK_FAILED);
//            mediator.notify(this, task);
//            return false;
//        }
        task.setStatus(TaskStatus.TASK_ACCEPTED);
        mediator.notify(this, task);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        task.setStatus(TaskStatus.TASK_COMPLETED);
        task.getProduct().setAssembled(true);
        mediator.notify(this, task);
        return true;
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

