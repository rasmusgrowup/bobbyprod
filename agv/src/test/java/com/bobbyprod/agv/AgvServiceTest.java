package com.bobbyprod.agv;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest("service.message=Hello from the AGV service")
public class AgvServiceTest {
    @Autowired
    private AgvService agvService;

    @Test
    public void contextLoads() {
        assertThat(agvService.message()).isNotNull();
    }

    @SpringBootApplication
    static class TestConfiguration {
    }
}