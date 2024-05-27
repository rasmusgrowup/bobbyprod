package com.bobbyprod.assemblystation;

import com.bobbyprod.common.Tasks.Task;
import com.bobbyprod.common.Tasks.TaskStatus;
import com.bobbyprod.common.Tasks.ActionType;
import com.bobbyprod.common.Products.Product;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.drone.Drone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class AssemblyStationServiceRunner /*implements CommandLineRunner*/ {
//    private AssemblyStationService assemblyStationService;
//
//    @Autowired
//    public AssemblyStationServiceRunner(AssemblyStationService assemblyStationService) {
//        this.assemblyStationService = assemblyStationService;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        AssemblyStation assemblyStation = new AssemblyStation(assemblyStationService);
//        Product product = new Drone("Drone", "D001", assemblyStation);
//        Task task = new Task(ActionType.ASSEMBLE_ITEM, AssetType.ASSEMBLY_STATION, product);
//        System.out.println("Assembly Station State before task: " + assemblyStation.getState());
//        assemblyStation.processTask(task);
//        System.out.println("Assembly Station State after task: " + assemblyStation.getState());
//        System.out.println("Task Status: " + task.getStatus());
//        if (task.getStatus() == TaskStatus.TASK_COMPLETED) {
//            System.out.println("Task completed successfully.");
//        } else {
//            System.out.println("Task failed.");
//        }
//    }
//
//    public static void main(String[] args) {
//        SpringApplication.run(AssemblyStationServiceRunner.class, args);
//    }
}
