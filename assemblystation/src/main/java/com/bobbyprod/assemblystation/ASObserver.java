package com.bobbyprod.assemblystation;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ASObserver implements Observer {

    private final String stationId;

    public ASObserver(String stationId) {
        this.stationId = stationId;
    }

    @Override
    public void update(String message) {
        System.out.println("Assembly station " + stationId + " received message: " + message);
        switch (message) {
            case "Start-assemble":
                try {
                    startAssemble();
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                System.out.println("Unknown message: " + message);
        }
    }

    private void startAssemble() throws MqttException {
        ASClient client = new ASClient();
        client.connect();
        AssemblyCommand startCommand = new AssemblyCommand(1);
        client.publish("emulator/operation", startCommand);

    }
}
