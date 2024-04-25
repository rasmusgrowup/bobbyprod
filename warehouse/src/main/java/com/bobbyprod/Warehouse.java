package com.bobbyprod;

public class Warehouse {
    private int id;
    private String[] inventory;
    private boolean itemPlaced;
    private SoapClient client;

    public Warehouse() {
        client = new SoapClient();
        clearInventory();
        fillInventory();
        setInventoryArray(client.getInventory());
    }

    public String[] getInventoryArray(){
        return inventory;
    }

    public void clearInventory() {
        for (int i = 1; i <= 10; i++) {
            client.pickItem(i);
        }
    }

    public void fillInventory() {
        for (int i = 1; i <= 10; i++) {
            client.insertItem("Drone Parts", i);
        }
    }

    public void setInventoryArray(String in) {
        String xmlString = in;

        String[] array = new String[10];
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\"Content\":\"(.*?)\"");
        java.util.regex.Matcher matcher = pattern.matcher(xmlString);
        int index = 0;
        while (matcher.find() && index < 10) {
            array[index++] = matcher.group(1);
        }
        inventory = array;
    }

    public void checkState(){
        String xmlString = client.getInventory();

        // Extracting "State" value using regular expressions
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\"State\":(\\d+)");
        java.util.regex.Matcher matcher = pattern.matcher(xmlString);
        if (matcher.find()) {
            String stateValue = matcher.group(1);
            System.out.println("State: " + stateValue);
        } else {
            System.out.println("State value not found.");
        }
    }
}
