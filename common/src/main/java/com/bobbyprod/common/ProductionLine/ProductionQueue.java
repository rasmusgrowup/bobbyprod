package com.bobbyprod.common.ProductionLine;

import com.bobbyprod.common.Products.Product;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
public class ProductionQueue {
    private static ProductionQueue instance = null;
    private LinkedList<Product> queue= new LinkedList<Product>();

    private ProductionQueue() {
    }

    public static synchronized ProductionQueue getInstance() {
        if (instance == null) {
            instance = new ProductionQueue();
        }
        return instance;
    }

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

    public LinkedList<Product> getQueue(){
        return queue;
    }
}
