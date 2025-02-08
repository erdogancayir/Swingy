package com.avaj.controller;

import com.avaj.database.HeroManager;
import com.avaj.view.gui.SpawnHeroGuiView;
import com.avaj.model.map.Map;

public class HeroSpawnController
{
    private SpawnHeroGuiView spawnHeroGuiView;
    private final boolean isGuiMode;
    private final HeroManager heroManager;
    public HeroSpawnController(HeroManager heroManager, boolean isGuiMode)
    {
        this.isGuiMode = isGuiMode;
        this.heroManager = heroManager;

        OpenSpawnHeroGui();
    }

    public void OpenSpawnHeroGui() {
        if (isGuiMode) {
            spawnHeroGuiView = new SpawnHeroGuiView(heroManager.GetAllHeroesArrayList());
        }
    }
}
