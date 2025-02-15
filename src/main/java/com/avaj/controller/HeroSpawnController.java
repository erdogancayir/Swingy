package com.avaj.controller;

import com.avaj.database.HeroManager;
import com.avaj.view.console.SpawnHeroConsole;
import com.avaj.view.gui.SpawnHeroGuiView;
import com.avaj.model.hero.Hero;

public class HeroSpawnController
{
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
            while (spawnHeroGuiView.getHeroSelectedOnRow() == null) {
                try {
                    Thread.sleep(500); // CPU'yu boşa yormamak için 500ms bekle
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Hero selectedHero = spawnHeroGuiView.getHeroSelectedOnRow();
            System.out.println("✅ Selected Hero: " + selectedHero.getName());

            return selectedHero;
        }
        else
        {
            SpawnHeroConsole spawnHeroConsole = new SpawnHeroConsole(heroManager.GetAllHeroesArrayList(), heroManager);
            Hero hero = spawnHeroConsole.spawnHero();

            return hero;
        }
    }
}
