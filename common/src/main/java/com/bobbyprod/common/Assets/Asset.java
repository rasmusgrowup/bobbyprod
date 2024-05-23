package com.bobbyprod.common.Assets;

import com.bobbyprod.common.States.AssetState;
import com.bobbyprod.common.Tasks.Task;
import com.bobbyprod.common.Interfaces.IMediator;

public abstract class Asset {
    private String id;
    private String name;
    private final AssetType type;
    private AssetState state;
    //protected IMediator mediator;

    public Asset(String id, String name, AssetType type/*, IMediator mediator*/) {
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
        this.state = AssetState.BUSY;
        if (processTask(task)) {
            this.state = AssetState.IDLE;
        } else {
            this.state = AssetState.ERROR;
        }
    }

    public abstract boolean processTask(Task task);

    public AssetState getState() {
        return state;
    };

    public void setState(AssetState state) {
        this.state = AssetState.valueOf(state.name());
    };
}
