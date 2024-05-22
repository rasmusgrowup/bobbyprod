package com.bobbyprod.common.Assets;

import com.bobbyprod.common.States.AssetState;

import java.util.List;
import java.util.logging.Logger;

public class AssetManager {
    private static final Logger LOGGER = Logger.getLogger(AssetManager.class.getName());
    /**
     * Finds an available asset of a specific type that is currently idle.
     *
     * @param assets List of all assets.
     * @param type The type of asset to find.
     * @return An available asset if found, otherwise null.
     */
    public static Asset findAvailableAsset(List<Asset> assets, AssetType type) {
        if (assets == null || assets.isEmpty()) {
            LOGGER.severe("Asset list is null or empty.");
            return null;
        }

        Asset availableAsset = assets.stream()
                .filter(asset -> asset.getType() == type)
                .findFirst()
                .orElse(null);

        if (availableAsset == null) {
            LOGGER.warning("No available asset of type " + type + " found.");
        }

        return availableAsset;
    }
}