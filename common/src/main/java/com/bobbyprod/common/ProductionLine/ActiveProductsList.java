package com.bobbyprod.common.ProductionLine;

import com.bobbyprod.common.Products.Product;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
public class ActiveProductsList {
    private static ActiveProductsList instance = null;
    private LinkedList<Product> activeProductionList = new LinkedList<Product>();

    private ActiveProductsList() {
    }

    public static synchronized ActiveProductsList getInstance() {
        if (instance == null) {
            instance = new ActiveProductsList();
        }
        return instance;
    }

    public void addToActiveProductionList(Product product) {
        activeProductionList.add(product);
    }

    public void removeFromActiveProductionList(Product product) {
        activeProductionList.remove(product);
    }

    public void clearActiveProductionList() {
        activeProductionList.clear();
    }

    public LinkedList<Product> getActiveProductionList() {
        return activeProductionList;
    }
}
