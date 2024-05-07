package com.bobbyprod.common.Products;

import com.bobbyprod.common.Assets.Asset;

public class Part {
    private String name;
    private String id;
    private int trayId;
    private Asset location;

    public Part(String name, String id, int trayId) {
        this.name = name;
        this.id = id;
        this.trayId = trayId;
    }

    public int getTrayId() {
        return trayId;
    }

    public void setTrayId(int trayId) {
        this.trayId = trayId;
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

    public void setLocation(Asset location) {
        this.location = location;
    }
}
