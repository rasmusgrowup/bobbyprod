package com.bobbyprod.core;


import com.bobbyprod.common.Communication.Mediator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ProductionQueueController {

    private final Mediator mediator;

    public ProductionQueueController() {
        this.mediator = Mediator.getInstance();
    }

    @GetMapping("/api/queue-name")
    public String getProductionQueue() {
        return mediator.getProductionQueue().getFirstInQueue().getName();
    }

    @GetMapping("/api/queue-id")
    public String getProductionQueueId() {
        return mediator.getProductionQueue().getFirstInQueue().getId();
    }


}
