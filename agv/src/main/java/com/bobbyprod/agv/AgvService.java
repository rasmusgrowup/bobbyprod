package com.bobbyprod.agv;

import com.bobbyprod.common.Tasks.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AgvService {
    private final RestTemplate restTemplate;

    @Autowired
    public AgvService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getStatus() {
        return restTemplate.getForObject("http://localhost:8082/v1/status", String.class);
    }

    public boolean handleTask(Task task) {
        boolean result = false;
        switch (task.getActionType()) {
            case MOVE_TO_WAREHOUSE:
                result = loadProgram("MoveToStorageOperation", 1);
                if (result) {
                    result = changeState(2);
                }
                break;
            case MOVE_TO_ASSEMBLY_STATION:
                // Logic to move AGV to the assembly station
                break;
            case MOVE_TO_CHARGER:
                // Logic to move AGV to the charger
                break;
            // Handle other action types
        }
        return result;
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