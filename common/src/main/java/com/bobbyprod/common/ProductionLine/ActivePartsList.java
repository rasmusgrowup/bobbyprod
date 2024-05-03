package com.bobbyprod.common.ProductionLine;


import com.bobbyprod.common.Products.Part;

import java.util.LinkedList;

public class ActivePartsList {
    private LinkedList<Part> activePartsList = new LinkedList<Part>();

    public void addToActiveProductionList(Part part) {
        activePartsList.add(part);
    }

    public void removeFromActiveProductionList(Part part) {
        activePartsList.remove(part);
    }
}
