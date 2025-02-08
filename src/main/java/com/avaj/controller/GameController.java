package com.avaj.controller;

import com.avaj.database.HeroManager;
import com.avaj.model.map.Map;
import com.avaj.model.hero.*;

public class GameController
{
    private HeroManager heroManager;
    private Map map;
    private Hero hero;
    private boolean isGuiMode = false;

    public GameController(Map map, HeroManager heroManager, boolean isGuiMode) {
        this.heroManager = heroManager;
        this.map = map;
        this.isGuiMode = isGuiMode;
    }



}
