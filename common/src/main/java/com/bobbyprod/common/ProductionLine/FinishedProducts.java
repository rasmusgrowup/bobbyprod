package com.bobbyprod.common.ProductionLine;

import com.bobbyprod.common.Products.Product;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
public class FinishedProducts {
    private LinkedList<Product> finishedProducts = new LinkedList<Product>();

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
