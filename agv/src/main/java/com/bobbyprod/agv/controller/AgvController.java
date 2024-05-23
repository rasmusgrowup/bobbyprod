package com.bobbyprod.agv.controller;

import com.bobbyprod.agv.model.AgvChangeState;
import com.bobbyprod.agv.model.AgvLoadProgram;
import com.bobbyprod.common.States.AssetState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class AgvController {
    private final RestTemplate restTemplate;

    @Autowired
    public AgvController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getStatus() {
        return restTemplate.getForObject("http://localhost:8082/v1/status", String.class);
    }

    public boolean loadProgram(String programName, int state) {
        System.out.println("Loading program: " + programName);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            AgvLoadProgram agvLoadProgram = new AgvLoadProgram();
            agvLoadProgram.setProgramName(programName);
            agvLoadProgram.setState(state);

            HttpEntity<AgvLoadProgram> request = new HttpEntity<>(agvLoadProgram, headers);
            restTemplate.exchange("http://localhost:8082/v1/status", HttpMethod.PUT, request, Void.class);
            System.out.println("Program loaded: " + programName);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.out.println("Error loading program: " + programName);
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean changeState(int state) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            AgvChangeState agvChangeState = new AgvChangeState();
            agvChangeState.setState(state);

            HttpEntity<AgvChangeState> request = new HttpEntity<>(agvChangeState, headers);
            restTemplate.exchange("http://localhost:8082/v1/status", HttpMethod.PUT, request, Void.class);
            System.out.println("State changed successfully to: " + state);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.out.println("Error changing state to: " + state);
            System.out.println(e.getMessage());
            return false;
        }
    }

    public AssetState getState() {
        try {
            ResponseEntity<Map> responseEntity = restTemplate.getForEntity("http://localhost:8082/v1/status", Map.class);
            Map<String, Object> responseMap = responseEntity.getBody();
            int stateInt = (int) responseMap.get("state");
            return stateInt == 1 ? AssetState.IDLE : stateInt == 2 ? AssetState.BUSY : AssetState.CHARGING;
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

    public int getBatteryLevel() {
        try {
            ResponseEntity<Map> responseEntity = restTemplate.getForEntity("http://localhost:8082/v1/status", Map.class);
            Map<String, Object> responseMap = responseEntity.getBody();
            return (int) responseMap.get("battery");
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }
}
