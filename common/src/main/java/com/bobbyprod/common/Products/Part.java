package com.bobbyprod.common.Products;

import com.bobbyprod.common.Assets.Asset;

public class Part {
    private String name;
    private String id;

    public Part(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
