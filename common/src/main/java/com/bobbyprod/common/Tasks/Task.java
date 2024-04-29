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

    /*public String getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(String targetLocation) {
        this.targetLocation = targetLocation;
    }*/

    public AssetType getCompatibleAssetType() {
        return compatibleAssetType;
    }

    public void setCompatibleAssetType(AssetType compatibleAssetType) {
        this.compatibleAssetType = compatibleAssetType;
    }
}
