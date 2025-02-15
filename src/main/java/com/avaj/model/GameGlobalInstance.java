package com.avaj.model;
import com.avaj.model.hero.Hero;
import com.avaj.model.hero.Warrior;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

public class GameGlobalInstance
{
    public static final char EMPTY = '.';
    public static final char HERO = 'H';
    public static final char GhostEnemy = 'l';
    public static final char ZombieGirlEnemy = 'm';
    public static final char BlueEnemy = 'n';
    public static final char GreenEnemy = 'o';
    public static final char PurpleVILLAIN = 'p';
    public static final char ZombieGuyVILLAIN = 'q';
    public static final char ARTIFACT = 'A';

    public static final String HERO_ICON_PATH = "./src/main/resources/assets/hero3.png";
    public static final String HERO2_ICON_PATH = "./src/main/resources/assets/hero1.png";
    public static final String HERO3_ICON_PATH = "./src/main/resources/assets/hero2.png";
    public static final String EMPTY_ICON_PATH = "./src/main/resources/assets/Plant.png";
    public static final String GhostEnemyICON_PATH = "./src/main/resources/assets/GhostEnemy.png";
    public static final String ZombieGirlEnemy_ICON_PATH = "./src/main/resources/assets/enemy2.png";
    public static final String BlueEnemy_Icon_Path = "./src/main/resources/assets/enemy3.png";
    public static final String GreenEnemy_ICON_PATH = "./src/main/resources/assets/enemy4.png";
    public static final String PurpleVILLAIN5_ICON_PATH = "./src/main/resources/assets/enemy5.png";
    public static final String ZombieVILLAIN6_ICON_Path = "./src/main/resources/assets/enemy6.png";
    public static final String ARTIFACT_ICON_PATH = "./src/main/resources/assets/Arti.png";
    public static final String UNVISIBLE_ICON_PATH = "./src/main/resources/assets/Ghost.png";

    public static final String GUI_MODE = "gui";
    public static final String CONSOLE_MODE = "console";

    private static final HashMap<String, ImageIcon> cache = new HashMap<>();

    public static void preloadImages() {
        getImage(HERO_ICON_PATH);
        getImage(EMPTY_ICON_PATH);
    }

    public static ImageIcon getImage(String path) {
        if (!cache.containsKey(path)) {
            try {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                cache.put(path, new ImageIcon(img));
            } catch (Exception e) {
                System.err.println("Error loading image: " + path);
                e.printStackTrace();
                return null;
            }
        }
        return cache.get(path);
    }

    public static JPanel getGameLogPanel() {
        JTextArea gameLog = new JTextArea();
        gameLog.setEditable(false);
        gameLog.setFont(new Font("Arial", Font.PLAIN, 12)); // 📌 Küçük font boyutu
        gameLog.setMargin(new Insets(5, 5, 5, 5)); // Daha dar kenar boşluğu
        gameLog.setLineWrap(true);
        gameLog.setWrapStyleWord(true);

        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setPreferredSize(new Dimension(1000, 120)); // 📌 Haritanın altına geniş bir panel
        logPanel.setBorder(BorderFactory.createTitledBorder("Game Log")); // 📌 Başlık ekleme

        JScrollPane scrollPane = new JScrollPane(gameLog);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        logPanel.add(scrollPane, BorderLayout.CENTER);

        return logPanel;
    }
}
