package com.bobbyprod.common.Products;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Tasks.Task;

import java.util.ArrayList;

public class Product {
    private int trayId;
    private String name;
    private String id;
    private boolean isAssembled;
    private ProductStatus status;
    private ArrayList<Part> partsList;

    public Product(String name, String id, ArrayList<Part> partsList) {
        this.name = name;
        this.id = id;
        this.isAssembled = false;
        this.status = ProductStatus.IN_STORAGE;
        this.partsList = partsList;
    }

    public int getTrayId() {
        return trayId;
    }

    public void setTrayId(int trayId) {
        this.trayId = trayId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Part> getPartsList() {
        return partsList;
    }

    public boolean isAssembled() {
        return isAssembled;
    }

    public void setAssembled(boolean assembled) {
        isAssembled = assembled;
    }

    private void setPartsList(ArrayList<Part> partsList) {
        this.partsList = partsList;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public ProductStatus getStatus() {
        return status;
    }
}
