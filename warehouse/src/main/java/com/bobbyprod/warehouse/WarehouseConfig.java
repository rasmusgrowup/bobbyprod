package com.bobbyprod.warehouse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WarehouseConfig {
    @Bean
    public WarehouseController warehouseController() {
        return new WarehouseController();
    }

    @Bean
    public WarehouseService warehouseService() {
        return new WarehouseService(warehouseController());
    }
}
