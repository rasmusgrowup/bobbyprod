package com.bobbyprod.agv.controller;

import com.bobbyprod.agv.model.AgvChangeState;
import com.bobbyprod.agv.model.AgvLoadProgram;
import com.bobbyprod.common.Interfaces.Observable;
import com.bobbyprod.common.Interfaces.Observer;
import com.bobbyprod.common.States.AssetState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
public class AgvController implements Observable {
    private final RestTemplate restTemplate;
    private List<Observer> observers;
    private int batteryLevel;
    private AssetState state;
    private final ScheduledExecutorService executorService;

    @Autowired
    public AgvController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.observers = new ArrayList<>();
        this.batteryLevel = 100;
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    public String getStatus() {
        return restTemplate.getForObject("http://localhost:8082/v1/status", String.class);
    }

    public boolean loadProgram(String programName, int state) {
        System.out.println("Loading program: " + programName);
        try {
            //Thread.sleep(2000);
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

    @Override
    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void pollAgvStatus() {
        try {
            //System.out.println("About to make a request to AGV");
            ResponseEntity<Map> responseEntity = restTemplate.getForEntity("http://localhost:8082/v1/status", Map.class);
            Map<String, Object> responseMap = responseEntity.getBody();
            //System.out.println("Received response from AGV: " + responseMap);
            this.batteryLevel = (int) responseMap.get("battery");
            int stateInt = (int) responseMap.get("state");
            this.state = stateInt == 1 ? AssetState.IDLE : stateInt == 2 ? AssetState.BUSY : AssetState.CHARGING;
            //System.out.println("Updated battery level and state");
            notifyObservers();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.out.println("Error polling AGV status");
            System.out.println(e.getMessage());
        }
    }

    @Scheduled(fixedRate = 1000)
    public void schedulePolling() {
        //System.out.println("Polling task scheduled.");
        executorService.scheduleAtFixedRate(this::pollAgvStatus, 0, 1, TimeUnit.SECONDS);
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public AssetState getState() {
        return state;
    }
}
