package com.bobbyprod.common.ProductionLine;

import com.bobbyprod.common.Products.Product;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
public class FinishedProducts {
    private static FinishedProducts instance = null;
    private LinkedList<Product> finishedProducts = new LinkedList<Product>();

    private FinishedProducts() {
    }

    public static synchronized FinishedProducts getInstance() {
        if (instance == null) {
            instance = new FinishedProducts();
        }
        return instance;
    }

    public void addToFinishedProducts(Product product) {
        finishedProducts.add(product);
    }

    public void removeFromFinishedProducts(Product product) {
        finishedProducts.remove(product);
    }

    public LinkedList<Product> getFinishedProducts() {
        return finishedProducts;
    }
}
