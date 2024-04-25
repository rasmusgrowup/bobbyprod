package com.bobbyprod;

import org.eclipse.paho.client.mqttv3.MqttException;

public class ASMain {

    public static void main(String[] args) {
        try {
            ASClient client = new ASClient();
            client.connect();
            client.subscribe("emulator/status");

            AssemblyCommand startCommand = new AssemblyCommand(12345);
            client.publish("emulator/operation", startCommand);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
