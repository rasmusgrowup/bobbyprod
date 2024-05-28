package com.bobbyprod.assemblystation;

import com.google.gson.Gson;
import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqttService {
    private final MqttClient mqttClient;
    private final Gson gson;

    @Autowired
    public MqttService(MqttClient mqttClient, Gson gson) {
        this.mqttClient = mqttClient;
        this.gson = gson;
    }

    public void publish(String topic, Object payload) throws MqttException {
        if (!mqttClient.isConnected()) {
            try {
                System.out.println("Client is not connected. Attempting to reconnect...");
                mqttClient.reconnect();
            } catch (MqttException e) {
                System.out.println("Failed to reconnect: " + e.getMessage());
                throw e;
            }
        }

        String json = gson.toJson(payload);
        System.out.println("Publishing message to topic \"" + topic + "\": " + json);
        MqttMessage message = new MqttMessage(json.getBytes());
        message.setQos(2);
        mqttClient.publish(topic, message);
        System.out.println("Message published");
    }

    public void subscribe(String topic, IMqttMessageListener messageListener) {
        System.out.println("Subscribing to topic: " + topic);
        try {
            mqttClient.subscribe(topic, messageListener);
            System.out.println("Subscribed to topic: " + topic);
        } catch (MqttException e) {
            e.printStackTrace();
            // Handle subscribe failure
        }
    }

    @PreDestroy
    public void cleanup() {
        try {
            if (mqttClient != null) {
                mqttClient.disconnect();
                mqttClient.close();
            }
        } catch (MqttException e) {
            e.printStackTrace();
            // Handle disconnection failure
        }
    }
}
