package com.bobbyprod.assemblystation;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.ActionType;
import com.bobbyprod.common.Tasks.Task;
import com.bobbyprod.common.Interface.IMediator;
import org.eclipse.paho.client.mqttv3.MqttException;

public class AssemblyStation extends Asset {

    private ASClient mqttClient;
    public AssemblyStation(String id, String name, AssetState state, AssetType type, IMediator mediator) {
        super(id, name, AssetState.IDLE, AssetType.ASSEMBLY_STATION, mediator);
        this.mqttClient = new ASClient();
        try {
            mqttClient.connect();  // Attempt to connect to the MQTT broker
            mqttClient.subscribe("emulator/status");// Subscribe to the status topic
        } catch (MqttException e) {
            System.out.println("Failed to connect to MQTT broker: " + e.getMessage());
        }
    }


    public void acceptTask(Task task) {
        System.out.println("AssemblyStation " + getName() + " is processing the task: " + task.getActionType());
        if (processTask(task)) {
            System.out.println("Task successfully completed by " + getName());
            this.setState(AssetState.IDLE);  // Update state to IDLE on success
        } else {
            System.out.println("Task failed by " + getName());
            this.setState(AssetState.ERROR); // Update state to ERROR on failure
        }
    }
    @Override
    public boolean processTask(Task task) {
        System.out.println("Assembly " + getName() + " is processing the task: " + task.getActionType());
        try {
            switch (task.getActionType()) {
                case ASSEMBLE_ITEM:
                    AssemblyCommand startCommand = new AssemblyCommand(1);
                    mqttClient.publish("emulator/operation", startCommand);
                    System.out.println("Published assembly start command.");
                    return true;
                default:
                    System.out.println("Unhandled task type: " + task.getActionType());
                    return false;
            }
        } catch (MqttException e) {
            System.out.println("Failed to publish MQTT message: " + e.getMessage());
            return false;
        }
    }
}

