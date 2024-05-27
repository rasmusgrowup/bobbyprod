package com.bobbyprod.common.Interfaces;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Products.Product;
import com.bobbyprod.common.Tasks.Task;

import java.util.List;

// Defines the mediator interface. This is used by assets (components) to notify the mediator of events or tasks,
// which allows centralized handling of inter-component communication and task delegation.
public interface IMediator {
    void notify(Asset asset, Task task);
    void registerAsset(Asset asset);
    void startProduction();
}