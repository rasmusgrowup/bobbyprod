package com.bobbyprod.drone;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Products.DronePart;
import com.bobbyprod.common.Products.Part;
import com.bobbyprod.common.Products.Product;
import com.bobbyprod.common.Products.ProductStatus;
import com.bobbyprod.common.Tasks.Task;

import java.util.ArrayList;

public class Drone extends Product {
    private ProductStatus status;

    public Drone(String name, String id, Asset location) {
        super(name, id, location);
        Part propellor = new Part("Propellors", "111");
        ArrayList<Part> partsList = new ArrayList<>();
        partsList.add(propellor);
        setPartsList(partsList);
    }

    public void setPartsList(ArrayList<Part> partsList) {
        partsList = new ArrayList<>();
        partsList.add(new DronePart("Drone Part", "ID1"));
    }
}