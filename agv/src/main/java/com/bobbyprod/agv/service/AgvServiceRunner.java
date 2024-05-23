package com.bobbyprod.agv.service;

import com.bobbyprod.agv.Agv;
import com.bobbyprod.agv.controller.AgvController;
import com.bobbyprod.common.Tasks.ActionType;
import com.bobbyprod.common.Tasks.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@SpringBootApplication
//@ComponentScan(basePackages = {"com.bobbyprod.agv"})
public class AgvServiceRunner /*implements CommandLineRunner*/ {
//    private final AgvController agvController;
//    private final AgvService agvService;
//
//    @Autowired
//    public AgvServiceRunner(AgvController agvController, AgvService agvService) {
//        this.agvController = agvController;
//        this.agvService = agvService;
//    }
//
//    @Override
//    public void run(String... args) {
//        Agv agv = new Agv(agvService, agvController);
//        Task task = new Task();
//        task.setActionType(ActionType.MOVE_TO_WAREHOUSE);
//        agv.processTask(task);
//        System.out.println("Task has been handled.");
//    }
//
//    public static void main(String[] args) {
//        SpringApplication.run(AgvServiceRunner.class, args);
//    }
}
