package com.bobbyprod.assemblystation;

import com.bobbyprod.common.States.AssetState;
import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssemblyStationConfig {
    @Bean
    public AssetState assetState() {
        return AssetState.IDLE;
    }

    @Bean
    public AssemblyCommand assemblyCommand() {
        int processID = 1;
        return new AssemblyCommand(processID);
    }

    @Bean
    public MqttConfig mqttClientConfig() {
        return new MqttConfig();
    }

    @Bean
    public MqttService mqttService(MqttConfig mqttConfig, Gson gson) {
        try {
            return new MqttService(mqttConfig.mqttClient(), gson);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public AssemblyStationService assemblyStationService(MqttService mqttService) {
        return new AssemblyStationService(mqttService);
    }

    @Bean
    public AssemblyStation assemblyStation(AssemblyStationService assemblyStationService) {
        return new AssemblyStation(assemblyStationService);
    }
}
