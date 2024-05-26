package com.bobbyprod.common.ProductionLine;

import com.bobbyprod.common.Products.Product;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
public class ActiveProductsList {
    private LinkedList<Product> activeProductionList = new LinkedList<Product>();

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
