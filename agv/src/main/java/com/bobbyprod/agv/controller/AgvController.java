package com.bobbyprod.agv.controller;

import com.bobbyprod.agv.model.AgvChangeState;
import com.bobbyprod.agv.model.AgvLoadProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
public class AgvController {
    private final RestTemplate restTemplate;
    //private final AgvService agvService;

    @Autowired
    public AgvController(RestTemplate restTemplate) {
        //this.agvService = agvService;
        this.restTemplate = restTemplate;
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
}
