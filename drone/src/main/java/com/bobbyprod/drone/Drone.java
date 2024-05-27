package com.bobbyprod.drone;

import com.bobbyprod.common.Products.Part;
import com.bobbyprod.common.Products.Product;

import java.util.ArrayList;

public class Drone extends Product {
    private ArrayList<Part> partsList;

    public Drone(String name, String id) {
        super(name, id, new ArrayList<>());
        this.setParts();
    }

    /**
     * Set the parts of the drone
     */
    public void setParts() {
        Part propeller = new Part("Propellors", "111");
        this.partsList = new ArrayList<>();
        this.partsList.add(propeller);
    }

    @Override
    public ArrayList<Part> getPartsList() {
        return partsList;
    }
}