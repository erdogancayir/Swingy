package com.avaj.controller;

import com.avaj.database.HeroManager;
import com.avaj.view.gui.SpawnHeroGuiView;
import com.avaj.model.hero.Hero;

public class HeroSpawnController
{
    private SpawnHeroGuiView spawnHeroGuiView;
    private final boolean isGuiMode;
    private final HeroManager heroManager;
    public HeroSpawnController(HeroManager heroManager, boolean isGuiMode)
    {
        this.isGuiMode = isGuiMode;
        this.heroManager = heroManager;
    }

    public Hero OpenSpawnHeroGui() {
        if (isGuiMode) {
            SpawnHeroGuiView spawnHeroGuiView = new SpawnHeroGuiView(heroManager.GetAllHeroesArrayList());

            // 📌 Kullanıcı seçim yapana kadar pencerenin kapanmasını bekle
            while (spawnHeroGuiView.getSelectedHero() == null) {
                try {
                    Thread.sleep(500); // CPU'yu boşa yormamak için 500ms bekle
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Hero selectedHero = spawnHeroGuiView.getSelectedHero();
            System.out.println("✅ Selected Hero: " + selectedHero.getName());

            return selectedHero;
        }
        return null;
    }
}
