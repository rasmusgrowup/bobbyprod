package com.bobbyprod.common.Assets;

import com.bobbyprod.common.States.AssetState;

import java.util.List;

public class AssetManager {
    /**
     * Finds an available asset of a specific type that is currently idle.
     *
     * @param assets List of all assets.
     * @param type The type of asset to find.
     * @return An available asset if found, otherwise null.
     */
    public static Asset findAvailableAsset(List<Asset> assets, AssetType type) {
        return assets.stream()
                .filter(asset -> asset.getState() == AssetState.IDLE && asset.getType() == type)
                .findFirst()
                .orElse(null);
    }
}