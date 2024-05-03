package com.bobbyprod.common.Products;

import com.bobbyprod.common.Assets.Asset;

import java.util.ArrayList;

public abstract class Product {
    private ProductStatus status;
    private int trayId;
    private String name;
    private String id;
    private Asset location;
    private boolean isAssembled;
    private ArrayList<Part> partsList = new ArrayList<Part>();
    public Product(String name, String id, Asset location, ArrayList<Part> partsList) {
        this.name = name;
        this.id = id;
        this.location = location;
        this.partsList = partsList;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
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

    public Asset getLocation() {
        return location;
    }

    public void setLocation(Asset location) {
        this.location = location;
    }

    public ArrayList<Part> getPartsList() {
        return partsList;
    }
}
