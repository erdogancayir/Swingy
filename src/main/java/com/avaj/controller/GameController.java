package com.avaj.controller;

import com.avaj.database.HeroManager;
import com.avaj.model.map.Map;
import com.avaj.model.hero.*;

public class GameController
{
    private HeroManager heroManager;
    private Map map;
    private Hero hero;
    private boolean isGuiMode;
    private ValidationController validationController;

    public GameController(Map map, Hero hero, HeroManager heroManager, ValidationController validationController, boolean isGuiMode) {
        this.heroManager = heroManager;
        this.map = map;
        this.isGuiMode = isGuiMode;
        this.hero = hero;
        this.validationController = validationController;

        this.validationController.validate(this.hero);
    }

    
}
