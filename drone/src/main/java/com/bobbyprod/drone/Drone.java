package com.bobbyprod.drone;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Products.Part;
import com.bobbyprod.common.Products.Product;
import com.bobbyprod.common.Products.ProductStatus;

import java.util.ArrayList;

public class Drone extends Product {
    private ProductStatus status;
    public Drone(String name, String id, Asset location) {
        super(name, id, location, initializePartsList());
        this.status = ProductStatus.NOT_ASSEMBLED_STORED;
    }
    private static ArrayList<Part> initializePartsList() {
        ArrayList<Part> partsList = new ArrayList<>();
        // Add DronePart objects to the partsList
        partsList.add(new DronePart("Drone Part", "ID1", 0));
        return partsList;
    }
}