package com.bobbyprod.assemblystation;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.States.AssetState;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ASClient {

    private static final String broker_URL = "tcp://localhost:1883";
    private static final String client_ID = java.util.UUID.randomUUID().toString();
    private MqttClient mqttClient;
    private AssemblyStation assemblyStation;

    public ASClient(AssemblyStation assemblyStation) {
        this.assemblyStation = assemblyStation;
    }

    public void connect() throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        mqttClient = new MqttClient(broker_URL, client_ID, persistence);
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(true);
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println("Connection lost: " + throwable.getMessage());
                throwable.printStackTrace();
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                String payload = new String(mqttMessage.getPayload());
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(payload, JsonObject.class);
                int status = jsonObject.get("State").getAsInt();
                switch (status) {
                    case 0:
                        assemblyStation.setState(AssetState.IDLE);
                        break;
                    case 1:
                        assemblyStation.setState(AssetState.BUSY);
                        break;
                    case 2:
                        assemblyStation.setState(AssetState.ERROR);
                        break;
                    default:
                        System.out.println("Unknown status: " + status);
                }
                System.out.println("Message arrived: [" + s + "] " + payload);
                System.out.println("Assembly Station State: " + assemblyStation.getState());
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
