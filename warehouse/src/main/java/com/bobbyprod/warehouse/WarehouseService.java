package com.bobbyprod.warehouse;

import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;
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

    public boolean fillInventory(String name) {
        while(findEmptyShelfId() != -1) {
            if(!client.insertItem(name, findEmptyShelfId())){
                return false;
            }
        }
        return true;
    }

    public boolean pickItem(String name){
        return client.pickItem(findDronePartId(name));
    }

    public boolean insertItem(String name){
        return client.insertItem(name, findEmptyShelfId());
    }

    public int findDronePartId(String name){
        String inv = client.getInventory();
        JSONObject jsonResponse = new JSONObject(inv);
        JSONArray inventoryItems = jsonResponse.getJSONArray("Inventory");

        for (int i = 0; i < inventoryItems.length(); i++) {
            JSONObject item = inventoryItems.getJSONObject(i);
            String content = item.optString("Content", null);
            if (name.equals(content)) {
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

    public boolean handleTask(Task task){
        boolean result = false;
        switch (task.getActionType()){
            case INSERT_ITEM:
                if(task.getPart() == null){
                    result = insertItem(task.getProduct().getName());
                }
                else if(task.getProduct() == null){
                    result = insertItem(task.getPart().getName());
                }
                break;
            case PICK_ITEM:
                if(task.getProduct() == null){
                    result = pickItem(task.getPart().getName());
                }
                else if(task.getPart() == null){
                    result = pickItem(task.getProduct().getName());
                }
                break;
            case FILL_PARTS:
                clearInventory();
                result = fillInventory(task.getPart().getName());
                break;
            default:
                System.out.println("Unknown action type" + task.getActionType());
        }
        return result;
    }
}
