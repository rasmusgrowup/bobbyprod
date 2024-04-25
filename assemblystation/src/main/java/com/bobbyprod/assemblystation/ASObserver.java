package com.bobbyprod.assemblystation;

public class ASObserver implements Observer {

    private final String stationId;

    public ASObserver(String stationId) {
        this.stationId = stationId;
    }

    @Override
    public void update(String message) {
        System.out.println("Assembly station " + stationId + " received message: " + message);
    }

    private void executeCommand(String command) {
        System.out.println("Executing command: " + command);
    }
}
