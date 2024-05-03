package com.bobbyprod.common.ProductionLine;

import com.bobbyprod.common.Products.Product;

import java.util.LinkedList;

public class ProductionQueue {
    private LinkedList<Product> queue= new LinkedList<Product>();

    public void addToQueue (Product product){
        queue.add(product);

    }
    public void removeFromQueue (Product product){
        queue.remove(product);
    }

    public Product getFirstInQueue(){
        return queue.getFirst();
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }

}
