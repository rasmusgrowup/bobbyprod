package com.bobbyprod.common.Tasks;

import com.bobbyprod.common.Assets.AssetType;

public class Task {
    private ActionType actionType;
    private AssetType compatibleAssetType;
    //private String targetLocation;

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public AssetType getCompatibleAssetType() {
        return compatibleAssetType;
    }

    public void setCompatibleAssetType(AssetType compatibleAssetType) {
        this.compatibleAssetType = compatibleAssetType;
    }
}
