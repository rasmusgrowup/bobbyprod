package com.bobbyprod.common.Products;

import java.util.ArrayList;

public abstract class Product {
    private ProductStatus status;
    private String name;
    private String id;
    private String location;
    private ArrayList<Part> partsList = new ArrayList<Part>();
    public Product(String name, String id, String location,  ArrayList<Part> partsList) {
        this.name = name;
        this.id = id;
        this.location = location;
        this.partsList = partsList;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<Part> getPartsList() {
        return partsList;
    }
}
