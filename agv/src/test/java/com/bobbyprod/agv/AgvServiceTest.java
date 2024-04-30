
package com.bobbyprod.agv;

import com.bobbyprod.common.Tasks.ActionType;
import com.bobbyprod.common.Tasks.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AgvServiceTest {
    @Test
    public void testHandleTask() {
        // Arrange
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        AgvService agvService = new AgvService(restTemplate);
        Task task = new Task();
        task.setActionType(ActionType.MOVE_TO_WAREHOUSE);

        // Mock the behavior of restTemplate.exchange method
        when(restTemplate.exchange(
                eq("http://localhost:8080/v1/status"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(Void.class)))
                .thenAnswer(invocation -> {
                    HttpEntity<AgvLoadProgram> request = invocation.getArgument(2);
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = objectMapper.writeValueAsString(request.getBody());
                    System.out.println(json); // Print the JSON object to the console
                    return ResponseEntity.ok().build();
                });

        // Act
        agvService.handleTask(task);
    }

    @Test
    public void testHandleTaskWithDockerImage() {
    }
}

