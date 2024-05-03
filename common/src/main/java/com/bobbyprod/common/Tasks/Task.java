package com.bobbyprod.common.Tasks;

import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.Products.Product;

public class Task {
    private ActionType actionType;
    private AssetType compatibleAssetType;
    private Product product;

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
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
