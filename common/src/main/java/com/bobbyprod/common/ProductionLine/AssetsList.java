package com.bobbyprod.common.ProductionLine;

import com.bobbyprod.common.Assets.Asset;
import org.springframework.web.server.WebFilter;

import java.util.ArrayList;
import java.util.List;

public class AssetsList {
    private static AssetsList instance = null;
    private List<Asset> assets = new ArrayList<>();

    private AssetsList() {
    }

    public static synchronized AssetsList getInstance() {
        if (instance == null) {
            instance = new AssetsList();
        }
        return instance;
    }

    public void addAsset(Asset asset) {
        assets.add(asset);
    }

    public void removeAsset(Asset asset) {
        assets.remove(asset);
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public boolean contains(Asset asset) {
        return assets.contains(asset);
    }

    public boolean isEmpty() {
        return assets.isEmpty();
    }
}
