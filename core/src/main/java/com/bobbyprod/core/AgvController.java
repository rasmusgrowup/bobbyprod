package com.bobbyprod.core;

import com.bobbyprod.agv.AgvChangeState;
import com.bobbyprod.agv.AgvLoadProgram;
import com.bobbyprod.agv.AgvService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agv")
public class AgvController {
    private final AgvService agvService;

    public AgvController(AgvService agvService) {
        this.agvService = agvService;
    }

    @GetMapping("/status")
    public String getStatus() {
        return agvService.getStatus();
    }

    @PutMapping("/command")
    public ResponseEntity<String> loadCommand(@RequestBody AgvLoadProgram command) {
        try {
            String programName = command.getProgramName();
            int state = command.getState();
            agvService.loadProgram(programName, state);
            return ResponseEntity.ok("Program loaded successfully: " + programName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading program: " + e.getMessage());
        }
    }

    @PutMapping("/state")
    public ResponseEntity<String> changeState(@RequestBody AgvChangeState command) {
        try {
            // Process the new state and update the AGV system
            agvService.changeState(command.getState());
            return ResponseEntity.ok("State changed successfully to: " + command.getState());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error changing state: " + e.getMessage());
        }
    }
}
