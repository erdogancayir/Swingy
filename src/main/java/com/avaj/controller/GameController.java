package com.avaj.controller;

import com.avaj.database.HeroManager;
import com.avaj.model.map.Map;
import com.avaj.model.hero.*;
import com.avaj.view.gui.GameGuiView;

public class GameController
{
    private HeroManager heroManager;
    private Map map;
    private Hero hero;
    private boolean isGuiMode;
    private ValidationController validationController;
    private GameGuiView gameGuiView;

    public GameController(Map map, Hero hero, HeroManager heroManager, ValidationController validationController, boolean isGuiMode) {
        this.heroManager = heroManager;
        this.map = map;
        this.isGuiMode = isGuiMode;
        this.hero = hero;
        this.validationController = validationController;

        this.validationController.validate(this.hero);
    }


    public void StartGame()
    {
        //this.hero.Heal();
        if (isGuiMode)
        {
            OpenUi();
        }
    }

    public void OpenUi()
    {
        gameGuiView = new GameGuiView(hero, map, heroManager);
    }
}
