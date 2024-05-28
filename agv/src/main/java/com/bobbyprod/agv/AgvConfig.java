package com.bobbyprod.agv;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AgvConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public AgvController agvController(RestTemplate restTemplate) {
        return new AgvController(restTemplate);
    }

    @Bean
    public AgvService agvService(AgvController agvController) {
        return new AgvService(agvController);
    }
}
