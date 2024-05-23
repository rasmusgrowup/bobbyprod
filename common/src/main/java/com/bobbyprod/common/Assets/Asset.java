package com.bobbyprod.common.Assets;

import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;

public abstract class Asset {
    private String id;
    private final String name;
    private final AssetType type;
    private AssetState state;

    public Asset(String id, String name, AssetType type) {
        this.id = id;
        this.name = name;
        this.type = type;
        //this.mediator = mediator;
    }

    public AssetType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void handleTask(Task task) {
    }

    public abstract boolean processTask(Task task);

    public AssetState getState() {
        return state;
    };

    public void setState(AssetState state) {
        this.state = state;
    };
}
