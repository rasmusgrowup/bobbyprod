package com.bobbyprod.agv;

import com.bobbyprod.common.Tasks.ActionType;
import com.bobbyprod.common.Tasks.Task;
import org.springframework.web.client.RestTemplate;

public class AgvServiceRunner {
    public static void main(String[] args) {
        // Create an instance of AgvService
        AgvService agvService = new AgvService(new RestTemplate());

        // Create a new task
        Task task = new Task();
        task.setActionType(ActionType.MOVE_TO_WAREHOUSE);

        // Call the handleTask method
        agvService.handleTask(task);

        // Print out a message to indicate that the task has been handled
        System.out.println("Task has been handled.");
    }
}
