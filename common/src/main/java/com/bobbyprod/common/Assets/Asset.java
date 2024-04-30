package com.bobbyprod.common.Assets;

import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;

public abstract class Asset {
    private String id;
    private String name;
    private final AssetType type;
    //private String location;
    private AssetState state;

    public Asset(String id, String name, /*String location,*/ AssetState state, AssetType type) {
        this.id = id;
        this.name = name;
        this.type = type;
        //this.location = location;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /* public String getLocation() {
        return location;
    } */

    public AssetState getState() {
        return state;
    }

    public void setState(AssetState state) {
        this.state = state;
    }

    public void acceptTask(Task task) {
        this.state = AssetState.BUSY;
        if (processTask(task)) {
            this.state = AssetState.IDLE;
        } else {
            this.state = AssetState.ERROR;
        }
    }

    public abstract boolean processTask(Task task);
}
