package com.avaj.controller;

import com.avaj.view.gui.SpawnHeroGuiView;
import com.avaj.model.map.Map;

public class HeroSpawnController
{
    private SpawnHeroGuiView spawnHeroGuiView;
    private final boolean isGuiMode;
    public HeroSpawnController(boolean isGuiMode)
    {
        this.isGuiMode = isGuiMode;
    }

    public void SpawnHero() {
        if (isGuiMode) {
            spawnHeroGuiView = new SpawnHeroGuiView();
            spawnHeroGuiView.showWindow(); // GUI'nin görünür olmasını garanti et

        }
    }
}
