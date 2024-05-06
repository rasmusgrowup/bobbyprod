package com.bobbyprod.warehouse;

import com.bobbyprod.common.States.AssetState;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
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
        while(findEmptyShelfId() != -1) {
            client.insertItem("Drone Parts", findEmptyShelfId());
        }
    }

    public void pickItem(){
        client.pickItem(findDronePartId());
    }

    public void insertItem(){
        client.insertItem("Drone",findEmptyShelfId());
    }

    public int findDronePartId(){
        String inv = client.getInventory();
        JSONObject jsonResponse = new JSONObject(inv);
        JSONArray inventoryItems = jsonResponse.getJSONArray("Inventory");

        for (int i = 0; i < inventoryItems.length(); i++) {
            JSONObject item = inventoryItems.getJSONObject(i);
            String content = item.optString("Content", null);
            if ("Drone Parts".equals(content)) {
                return item.getInt("Id");
            }
        }
        return -1;
    }


    public int findEmptyShelfId() {
        // Assuming 'in' is the JSON string embedded in the XML.
        String inv = client.getInventory();
        JSONObject jsonResponse = new JSONObject(inv);
        JSONArray inventoryItems = jsonResponse.getJSONArray("Inventory");
        for (int i = 0; i < inventoryItems.length(); i++) {
            JSONObject item = inventoryItems.getJSONObject(i);
            String content = item.optString("Content", null);
            if (content == null || content.isEmpty()) {
                return item.getInt("Id");
            }
        }
        return -1;
    }

    public AssetState checkState() {
        String jsonPart = client.getInventory();

        if (jsonPart == null) {
            System.out.println("JSON part not found in the XML.");
            return AssetState.ERROR;
        }

        JSONObject jsonResponse = new JSONObject(jsonPart);
        int stateValue = jsonResponse.getInt("State");

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
