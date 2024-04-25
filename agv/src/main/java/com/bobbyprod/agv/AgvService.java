package com.bobbyprod.agv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
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

    public void updateStatus(String newStatus) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(newStatus, headers);
        restTemplate.put("http://localhost:8082/v1/status", entity);
    }
}