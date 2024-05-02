package com.bobbyprod.warehouse;

import com.bobbyprod.common.States.AssetState;
import org.json.JSONArray;
import org.json.JSONObject;

public class WarehouseService {
    private WarehouseController client;

    public WarehouseService(){
     client = new WarehouseController();
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

    public void pickItem(int id){
        client.pickItem(id);
    }

    public void insertItem(String name, int id){
        client.insertItem(name,id);
    }

//    public void setInventoryArray(String inv) {
//        String xmlString = inv;
//
//        String[] array = new String[10];
//        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\"Content\":\"(.*?)\"");
//        java.util.regex.Matcher matcher = pattern.matcher(xmlString);
//        int index = 0;
//        while (matcher.find() && index < 10) {
//            array[index++] = matcher.group(1);
//        }
//        inventory = array;
//    }

    public int findEmptyShelfId(String inv) {
        // Assuming 'in' is the JSON string embedded in the XML.
        JSONObject jsonResponse = new JSONObject(inv);
        JSONArray inventoryItems = jsonResponse.getJSONArray("Inventory");

        for (int i = 0; i < inventoryItems.length(); i++) {
            JSONObject item = inventoryItems.getJSONObject(i);
            String content = item.optString("Content", null); // Use null to explicitly check for empty
            if (content == null || content.isEmpty()) { // Checks if Content is null or empty
                return item.getInt("Id"); // Returns the ID of the first empty shelf
            }
        }
        return -1; // Return -1 if no empty shelf is found
    }

//    public AssetState checkState(){
//        String xmlString = client.getInventory();
//
//        // Extracting "State" value using regular expressions
//        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\"State\":(\\d+)");
//        java.util.regex.Matcher matcher = pattern.matcher(xmlString);
//        if (matcher.find()) {
//            String stringStateValue = matcher.group(1);
//            int stateValue = Integer.parseInt(stringStateValue);
//            switch (stateValue){
//                case 0:
//                    System.out.println("State is idle");
//                    return AssetState.IDLE;
//                case 1:
//                    System.out.println("State is Executing");
//                    return AssetState.BUSY;
//                case 2:
//                    System.out.println("State is Error");
//                    return AssetState.ERROR;
//
//            }
//            System.out.println("State: " + stateValue);
//        } else {
//            System.out.println("State value not found.");
//            return AssetState.ERROR;
//        }
//        return AssetState.ERROR;
//    }

//    public AssetState checkState() {
//        String xmlString = client.getInventory();  // Assume this retrieves the XML containing JSON.
//
//        // First, extract the JSON string from the XML.
//        // Assuming you know the path to the JSON, here we assume it's directly the text content of a specific element.
//        // For real implementation, you might need to use an XML parser to safely extract this JSON string.
//        // Here, I directly use the xmlString as JSON for demonstration.
//
//        JSONObject jsonResponse = new JSONObject(xmlString);  // Parse the JSON string.
//        JSONObject inventoryResult = jsonResponse.getJSONObject("GetInventoryResult");
//
//        if (inventoryResult.has("State")) {
//            int stateValue = inventoryResult.getInt("State");  // Get the state value.
//            switch (stateValue) {
//                case 0:
//                    System.out.println("State is idle");
//                    return AssetState.IDLE;
//                case 1:
//                    System.out.println("State is Executing");
//                    return AssetState.BUSY;
//                case 2:
//                    System.out.println("State is Error");
//                    return AssetState.ERROR;
//            }
//        } else {
//            System.out.println("State value not found.");
//            return AssetState.ERROR;  // Default or error state if 'State' key is not found.
//        }
//        return AssetState.ERROR;
//    }

    public AssetState checkState() {
        String jsonPart = client.getInventory();  // This returns the XML containing JSON.

        if (jsonPart == null) {
            System.out.println("JSON part not found in the XML.");
            return AssetState.ERROR;
        }

        JSONObject jsonResponse = new JSONObject(jsonPart);  // Parse the extracted JSON string.
        int stateValue = jsonResponse.getInt("State");  // Get the state value.

        switch (stateValue) {
            case 0:
                System.out.println("State is idle");
                return AssetState.IDLE;
            case 1:
                System.out.println("State is Executing");
                return AssetState.BUSY;
            case 2:
                System.out.println("State is Error");
                return AssetState.ERROR;
        }
        return AssetState.ERROR;
    }

}
