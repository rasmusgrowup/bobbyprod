package com.bobbyprod.common.Assets;

import com.bobbyprod.common.ProductionLine.AssetsList;
import com.bobbyprod.common.States.AssetState;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class AssetManager {
    private static final Logger LOGGER = Logger.getLogger(AssetManager.class.getName());
    /**
     * Finds an available asset of a specific type that is currently idle.
     *
     * @param assets List of all assets.
     * @param type The type of asset to find.
     * @return An available asset if found, otherwise null.
     */
    public static Asset findAvailableAsset(AssetsList assets, AssetType type) {
        if (assets == null || assets.isEmpty()) {
            //LOGGER.severe("Asset list is null or empty.");
            return null;
        }

        Asset availableAsset = assets.getAssets().stream()
                .filter(asset -> asset.getType() == type && asset.getState() == AssetState.IDLE)
                .findFirst()
                .orElse(null);

        if (availableAsset == null) {
            //LOGGER.warning("No available idle asset of type " + type + " found.");
        }

        return availableAsset;
    }

}