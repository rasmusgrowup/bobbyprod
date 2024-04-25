package com.bobbyprod.core;

import java.util.ArrayList;
import java.util.List;

import com.bobbyprod.assemblystation.ASClient;
import com.bobbyprod.assemblystation.ASObserver;
import com.bobbyprod.assemblystation.AssemblyCommand;
import com.bobbyprod.assemblystation.Observer;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Publisher {
    private List<Observer> observers = new ArrayList<>();

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void publishMessage(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public static void main(String[] args) throws MqttException {
        ASClient client = new ASClient();
        Publisher publisher = new Publisher();
        AssemblyCommand startCommand = new AssemblyCommand(12345);
        ASObserver asObserver = new ASObserver("1");
        publisher.registerObserver(asObserver);
        //client.publish("emulator/operation", startCommand);
        publisher.publishMessage("Start assemble");
        System.out.println("Message published");
    }
}
