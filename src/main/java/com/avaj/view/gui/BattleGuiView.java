package com.avaj.view.gui;

import com.avaj.model.enemy.Enemy;
import com.avaj.model.hero.Hero;

import javax.swing.*;
import java.awt.*;

public class BattleGuiView {
    private final JFrame frame;
    private final JTextArea battleLog;
    private final JProgressBar heroHP;
    private final JProgressBar enemyHP;

    public BattleGuiView(Hero hero, Enemy enemy) {
        frame = new JFrame("Battle");
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // 📌 Savaş Log Alanı
        battleLog = new JTextArea();
        battleLog.setEditable(false);
        battleLog.setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane logScroll = new JScrollPane(battleLog);
        mainPanel.add(logScroll, BorderLayout.CENTER);

        // 📌 Sağlık Barları
        JPanel healthPanel = new JPanel(new GridLayout(2, 1));

        heroHP = new JProgressBar(0, hero.getMaxHp());
        heroHP.setStringPainted(true);
        heroHP.setValue(hero.getHitPoints());
        healthPanel.add(new JLabel(hero.getName() + " HP"));
        healthPanel.add(heroHP);

        enemyHP = new JProgressBar(0, enemy.getMaxHp());
        enemyHP.setStringPainted(true);
        enemyHP.setValue(enemy.getHitPoint());
        healthPanel.add(new JLabel(enemy.getClas() + " HP"));
        healthPanel.add(enemyHP);

        mainPanel.add(healthPanel, BorderLayout.NORTH);

        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
    }

    public void logMessage(String message) {
        battleLog.append(message + "\n");
    }

    public void updateStats(Hero hero, Enemy enemy) {
        heroHP.setValue(hero.getHitPoints());
        enemyHP.setValue(enemy.getHitPoint());
        heroHP.setString(hero.getHitPoints() + " / " + hero.getMaxHp());
        enemyHP.setString(enemy.getHitPoint() + " / " + enemy.getMaxHp());
    }

    public void enableExit() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public JFrame getFrame() {
        return frame;
    }
}
