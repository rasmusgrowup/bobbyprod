package com.bobbyprod.common.ProductionLine;

import com.bobbyprod.common.Products.Product;

import java.util.LinkedList;

public class ActiveProductionList {
    private LinkedList<Product> activeProductionList = new LinkedList<Product>();

    public void addToActiveProductionList(Product product){
        activeProductionList.add(product);
    }

    public void removeFromActiveProductionList(Product product){
        activeProductionList.remove(product);
    }

    public void updateP
}
