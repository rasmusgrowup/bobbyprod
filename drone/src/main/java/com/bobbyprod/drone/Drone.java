package com.bobbyprod.drone;

import com.bobbyprod.common.Products.DronePart;
import com.bobbyprod.common.Products.Part;
import com.bobbyprod.common.Products.Product;
import com.bobbyprod.common.Products.ProductStatus;

import java.util.ArrayList;

public class Drone extends Product {
    private ProductStatus status;

    @Override
    public void setPartsList(ArrayList<Part> partsList) {
        partsList = new ArrayList<>();
        // Add DronePart objects to the partsList
        partsList.add(new DronePart("Drone Part", "ID1"));
    }
}