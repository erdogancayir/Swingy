package com.avaj.controller;

import com.avaj.database.HeroManager;
import com.avaj.model.enemy.Enemy;
import com.avaj.model.map.Map;
import com.avaj.model.hero.*;
import com.avaj.view.console.GameConsoleView;
import com.avaj.view.gui.BattleGuiView;
import com.avaj.view.gui.GameGuiView;

import javax.swing.*;

public class GameController
{
    private HeroManager heroManager;
    private Map map;
    private Hero hero;
    private boolean isGuiMode;
    private ValidationController validationController;
    private GameGuiView gameGuiView;
    private GameConsoleView gameConsoleView;

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
        else
        {
            OpenConsole();
        }
    }

    public void OpenUi()
    {
        gameGuiView = new GameGuiView(hero, map, heroManager, this);
    }

    public void OpenConsole()
    {
        gameConsoleView = new GameConsoleView(hero, map, heroManager, this);
    }

    public void startBattleForUi(Enemy enemy) {
        SwingUtilities.invokeLater(() -> {
            BattleGuiView battleView = new BattleGuiView(hero, enemy);
            BattleController battleController = new BattleController(hero, enemy, battleView, this);
            battleController.startBattle();
        });
    }

    public void startBattleForConsole(Enemy enemy) {
        BattleController battleController = new BattleController(hero, enemy, null, this);
        boolean isVictory = battleController.startBattle();
        if (isVictory)
        {
            heroManager.updateHeroStatus(
                    hero.getName(),
                    hero.getLevel(),
                    hero.getExperience(),
                    hero.getAttack(),
                    hero.getDefense(),
                    hero.getHitPoints(),
                    hero.getX(),
                    hero.getY());
        }
    }

    public GameGuiView getGameGuiView() {
        return gameGuiView;
    }

    public GameConsoleView getGameConsoleView() {
        return gameConsoleView;
    }

    public boolean isGuiMode()
    {
        return isGuiMode;
    }
}
