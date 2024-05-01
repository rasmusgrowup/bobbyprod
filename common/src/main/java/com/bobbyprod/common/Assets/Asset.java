package com.bobbyprod.common.Assets;

import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;

public abstract class Asset {
    private String id;
    private String name;
    private final AssetType type;

    public Asset(String id, String name, AssetType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract boolean processTask(Task task);
    public abstract AssetState getState();
    public abstract void setState(AssetState state);
    public abstract void updateState(AssetState state);
}
