package com.bobbyprod.core;

import com.bobbyprod.common.Communication.Mediator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final Mediator mediator;

    public AssetController() {
        this.mediator = Mediator.getInstance();
    }

    @GetMapping
    public List<AssetDTO> getAssets() {
        return mediator.getAssets().stream()
                .map(asset -> new AssetDTO(asset.getId(), asset.getName(), asset.getType(), asset.getState()))
                .collect(Collectors.toList());
    }
}