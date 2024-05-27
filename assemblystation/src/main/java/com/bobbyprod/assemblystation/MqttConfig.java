package com.bobbyprod.assemblystation;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;

public class MqttConfig {
    private static final String broker_URL = "tcp://localhost:1883";
    private static final String client_ID = java.util.UUID.randomUUID().toString();

    @Bean
    public MqttClient mqttClient() throws MqttException {
        MqttClient client = new MqttClient(broker_URL, client_ID);
        client.connect();
        if (client.isConnected()) {
            System.out.println("Connected to MQTT broker at " + broker_URL);
        } else {
            System.out.println("Failed to connect to MQTT broker at " + broker_URL);
        }
        return client;
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
