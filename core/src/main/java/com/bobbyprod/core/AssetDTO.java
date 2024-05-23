package com.bobbyprod.core;

import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.States.AssetState;

public class AssetDTO {
    private String id;
    private String name;
    private AssetType type;
    private AssetState state;

    public AssetDTO(String id, String name, AssetType type, AssetState state) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.state = state;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public AssetType getType() { return type; }
    public void setType(AssetType type) { this.type = type; }
    public AssetState getState() { return state; }
    public void setState(AssetState state) { this.state = state; }
}