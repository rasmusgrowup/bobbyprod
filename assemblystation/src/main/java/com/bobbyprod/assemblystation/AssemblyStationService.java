package com.bobbyprod.assemblystation;

import com.bobbyprod.common.States.AssetState;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssemblyStationService {
    private final MqttService mqttService;
    private static final String OPERATION_TOPIC = "emulator/operation";
    private static final String STATUS_TOPIC = "emulator/status";
    private static final String HEALTH_TOPIC = "emulator/checkhealth";
    private AssetState state;

    @Autowired
    public AssemblyStationService(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    @PostConstruct
    public void init() {
        mqttService.subscribe(STATUS_TOPIC, new StatusMessageListener());
    }

    public boolean startAssemblyProcess(AssemblyCommand assembleCommand) {
        try {
            mqttService.publish(OPERATION_TOPIC, assembleCommand);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private class StatusMessageListener implements IMqttMessageListener {
        @Override
        public void messageArrived(String topic, org.eclipse.paho.client.mqttv3.MqttMessage message) throws Exception {
            String payload = new String(message.getPayload());
            //System.out.println("Message arrived on topic \"" + topic + "\": " + payload);

            // Parse JSON payload to extract status
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(payload, JsonObject.class);
            int status = jsonObject.get("State").getAsInt();

            // Update assembly station state based on the received status
            switch (status) {
                case 0:
                    setState(AssetState.IDLE);
                    break;
                case 1:
                    setState(AssetState.BUSY);
                    break;
                case 2:
                    setState(AssetState.ERROR);
                    break;
                default:
                    System.out.println("Unknown status: " + status);
            }

            //System.out.println("Assembly Station State: " + getState());
        }
    }

    public AssetState getState() {
        return this.state;
    }

    public void setState(AssetState state) {
        this.state = state;
    }
}
