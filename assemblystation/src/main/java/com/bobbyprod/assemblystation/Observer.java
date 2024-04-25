package com.bobbyprod.assemblystation;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface Observer {
    void update(String message);
}
