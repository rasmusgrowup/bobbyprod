package com.bobbyprod.assemblystation;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Arrays;

public class ASClient {

    private static final String broker_URL = "tcp://localhost:1883";
    private static final String client_ID = java.util.UUID.randomUUID().toString();
    private MqttClient mqttClient;

    public void connect() throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        mqttClient = new MqttClient(broker_URL, client_ID, persistence);
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(true);
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println("Connection lost: " + throwable.getMessage());
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println("Message arrived: [" + s + "] " + new String(mqttMessage.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("Delivery complete for token " + iMqttDeliveryToken.getResponse());
            }
        });

        System.out.println("Connecting to broker: " + broker_URL);
        mqttClient.connect();
        System.out.println("Connected");
    }

    public void subscribe(String topic) throws MqttException {
        System.out.println("Subscribing to topic: " + topic);
        mqttClient.subscribe(topic, 1);
    }

    public void publish(String topic, Object payload) throws MqttException {
        Gson gson = new Gson();
        String json = gson.toJson(payload);
        System.out.println("Publishing message to topic \"" + topic + "\": " + json);
        MqttMessage message = new MqttMessage(json.getBytes());
        message.setQos(2);
        mqttClient.publish(topic, message);
        System.out.println("Message published");
    }
}
