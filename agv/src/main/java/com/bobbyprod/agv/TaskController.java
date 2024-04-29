package com.bobbyprod.agv;

import com.bobbyprod.common.Tasks.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/task")
public class TaskController {
    private final AgvService agvService;

    public TaskController(AgvService agvService) {
        this.agvService = agvService;
    }

    @PostMapping
    public ResponseEntity<Void> receiveTask(@RequestBody Task task) {
        agvService.handleTask(task);
        return ResponseEntity.ok().build();
    }
}
