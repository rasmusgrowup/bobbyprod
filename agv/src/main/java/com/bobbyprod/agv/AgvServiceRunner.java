package com.bobbyprod.agv;

import com.bobbyprod.common.Tasks.ActionType;
import com.bobbyprod.common.Tasks.Task;
import org.springframework.web.client.RestTemplate;

public class AgvServiceRunner {
    public static void main(String[] args) {
        run();
    }

    public static void run() {
        // Create an instance of AgvService
        AgvController agvController = new AgvController(new RestTemplate());
        AgvService agvService = new AgvService(agvController);

        // Create a new task
        Task task = new Task();
        task.setActionType(ActionType.MOVE_TO_WAREHOUSE);

        // Call the handleTask method
        agvService.handleTask(task);

        // Print out a message to indicate that the task has been handled
        System.out.println("Task has been handled.");
    }
}
