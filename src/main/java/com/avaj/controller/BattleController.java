package com.avaj.controller;

import com.avaj.model.enemy.Enemy;
import com.avaj.model.hero.Hero;
import com.avaj.view.gui.BattleGuiView;

import javax.swing.*;
import java.util.List;
import java.util.Random;

public class BattleController {
    private final Hero hero;
    private final Enemy enemy;
    private final BattleGuiView battleView;
    private final GameController gameController;

    public BattleController(Hero hero, Enemy enemy, BattleGuiView battleView, GameController gameController) {
        this.hero = hero;
        this.enemy = enemy;
        this.battleView = battleView;
        this.gameController = gameController;
    }

    public void startBattle() {
        battleView.logMessage("⚔️ Battle started! " + hero.getName() + " vs " + enemy.getClas());
        battleView.updateStats(hero, enemy);

        SwingWorker<Void, String> battleWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                while (hero.getHitPoints() > 0 && enemy.getHitPoint() > 0) {
                    int diceRoll = new Random().nextInt(6) + 1;
                    publish("🎲 Rolled: " + diceRoll);

                    int heroDamage = Math.max(0, (hero.getAttack() + diceRoll) - enemy.getDefence());
                    enemy.takeDamage(heroDamage);
                    publish("🗡️ " + hero.getName() + " attacks for " + heroDamage + " damage.");

                    if (enemy.getHitPoint() <= 0) {
                        publish("🏆 " + hero.getName() + " won the battle!");
                        return null;
                    }

                    int enemyDamage = Math.max(0, enemy.getAttack() - hero.getDefence());
                    hero.takeDamage(enemyDamage);
                    publish("💥 " + enemy.getClas() + " attacks for " + enemyDamage + " damage.");

                    if (hero.getHitPoints() <= 0) {
                        publish("💀 " + hero.getName() + " died...");
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void process(List<String> messages) { //Amacı: publish(...) tarafından gönderilen mesajları alır ve GUI'yi günceller
                for (String message : messages) {
                    battleView.logMessage(message);
                }
                battleView.updateStats(hero, enemy);
            }

            @Override
            protected void done() { //Amacı: doInBackground() metodu tamamlandığında çağrılır. (Dövüş tamamlandığında oyuncunun çıkış yapmasına izin verir)
                if (hero.getHitPoints() <= 0) {
                    JOptionPane.showMessageDialog(battleView.getFrame(),
                            hero.getName() + " has been defeated! Game Over.",
                            "Defeat",
                            JOptionPane.ERROR_MESSAGE);

                    battleView.getFrame().dispose(); // Battle GUI'yi kapat
                    gameController.getGameGuiView().closeGame(); // Ana oyun GUI'yi kapat
                } else
                {
                    JOptionPane.showMessageDialog(battleView.getFrame(),
                            hero.getName() + "Your hero won the battle!",
                            "Victory",
                            JOptionPane.INFORMATION_MESSAGE);
                    battleView.enableExit();
                }
            }

        };

        battleWorker.execute(); //SwingWorker'ı başlatır ve arka planda çalıştırır.
    }
}
