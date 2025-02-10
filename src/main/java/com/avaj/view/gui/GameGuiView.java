package com.avaj.view.gui;

import com.avaj.database.HeroManager;
import com.avaj.model.GameGlobalInstance;
import com.avaj.model.hero.Hero;
import com.avaj.model.map.Direction;
import com.avaj.model.map.Map;

import javax.swing.*;
import java.awt.*;

import static com.avaj.model.GameGlobalInstance.*;

public class GameGuiView {
    private final JFrame frame;
    private final Hero hero;
    private final Map map;
    private final HeroManager heroManger;

    private JLabel avatarLabel;  // Sol panel: Avatar
    private JTextArea heroStats; // Sol panel: Kahraman bilgileri
    private JTextArea enemyStats; // Sol panel: Kahraman bilgileri
    private JPanel mapPanel;     // Merkez: Oyun haritası
    private JTextArea gameLog;   // Sağ panel: Oyun olayları
    private JPanel controlPanel; // 📌 Butonları eklemek için
    private JPanel enemyInfoPanel; // 📌 Düşman Bilgileri Paneli (Şimdilik boş)
    private JLabel enemyAvatarLabel;

    public GameGuiView(Hero hero, Map map, HeroManager heroManager) {
        this.hero = hero;
        this.map = map;
        this.heroManger = heroManager;

        frame = new JFrame("Swingy - RPG Game");
        initializeUI();
    }

    private void initializeUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // 📌 Sol Panel: Kahraman Bilgileri
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(250, frame.getHeight()));
        leftPanel.setBackground(new Color(240, 240, 240));

        avatarLabel = new JLabel();
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateAvatar(hero.getAvatarPath());

        heroStats = new JTextArea();
        heroStats.setEditable(false);
        heroStats.setFont(new Font("Arial", Font.BOLD, 18));
        heroStats.setOpaque(false);
        heroStats.setMargin(new Insets(10, 10, 10, 10));
        updateHeroStats();

        leftPanel.add(avatarLabel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(heroStats), BorderLayout.CENTER);

        // 📌 Orta Panel: Harita + Log Paneli
        JPanel centerPanel = new JPanel(new BorderLayout());

        mapPanel = new JPanel();
        mapPanel.setLayout(new GridLayout(map.getSize(), map.getSize(), 2, 2));
        updateMap();

        // 📌 Oyun Günlüğü: Haritanın altına eklenen log paneli
        gameLog = new JTextArea();
        gameLog.setEditable(false);
        gameLog.setFont(new Font("Arial", Font.PLAIN, 16));
        gameLog.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane gameLogScroll = new JScrollPane(gameLog);
        gameLogScroll.setPreferredSize(new Dimension(frame.getWidth(), 150));

        var gameStartLog = "Game started...\n";
        gameLog.setText(gameStartLog);

        centerPanel.add(mapPanel, BorderLayout.CENTER);
        centerPanel.add(gameLogScroll, BorderLayout.SOUTH);

        // 📌 Sağ Panel: Düşman Bilgileri + Yön Butonları
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(250, frame.getHeight()));
        rightPanel.setBackground(new Color(240, 240, 240));

        // 📌 Düşman Bilgileri Paneli (Yeni Avatar Dahil)
        enemyInfoPanel = new JPanel(new BorderLayout());
        enemyInfoPanel.setPreferredSize(new Dimension(250, 250));
        enemyInfoPanel.setBackground(new Color(255, 255, 255));
        enemyInfoPanel.setBorder(BorderFactory.createTitledBorder("Enemy Info"));

        // 📌 Düşman Avatarı
        enemyAvatarLabel = new JLabel();
        enemyAvatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateEnemyAvatar(null); // Başlangıçta boş

        // 📌 Düşman Bilgileri Alanı
        enemyStats = new JTextArea();
        enemyStats.setEditable(false);
        enemyStats.setFont(new Font("Arial", Font.BOLD, 16));
        enemyStats.setOpaque(false);
        enemyStats.setMargin(new Insets(10, 10, 10, 10));
        enemyStats.setText("No enemy nearby."); // Varsayılan metin

        enemyInfoPanel.add(enemyAvatarLabel, BorderLayout.NORTH);
        enemyInfoPanel.add(new JScrollPane(enemyStats), BorderLayout.CENTER);

        // 📌 Hareket Butonları Küçültülerek Alta Taşındı
        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3, 3, 3, 3));
        controlPanel.setPreferredSize(new Dimension(250, 120));
        addControlButtons();

        rightPanel.add(enemyInfoPanel, BorderLayout.CENTER);
        rightPanel.add(controlPanel, BorderLayout.SOUTH);

        // 📌 Frame'e panelleri ekle
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }

    private void addControlButtons() {
        JButton northButton = createMovementButton("\u2B06 North", Direction.NORTH);
        JButton southButton = createMovementButton("\u2B07 South", Direction.SOUTH);
        JButton eastButton = createMovementButton("\u27A1 East", Direction.EAST);
        JButton westButton = createMovementButton("\u2B05 West", Direction.WEST);

        northButton.setPreferredSize(new Dimension(70, 30));
        southButton.setPreferredSize(new Dimension(70, 30));
        eastButton.setPreferredSize(new Dimension(70, 30));
        westButton.setPreferredSize(new Dimension(70, 30));

        controlPanel.add(new JLabel(""));
        controlPanel.add(northButton);
        controlPanel.add(new JLabel(""));
        controlPanel.add(westButton);
        controlPanel.add(new JLabel(""));
        controlPanel.add(eastButton);
        controlPanel.add(new JLabel(""));
        controlPanel.add(southButton);
        controlPanel.add(new JLabel(""));
    }

    private JButton createMovementButton(String text, Direction direction) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12)); // 📌 Daha küçük font
        button.setFocusPainted(false);
        button.setBackground(new Color(100, 150, 255));
        button.setForeground(Color.BLUE);
        button.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));

        button.addActionListener(e -> moveHero(direction));

        return button;
    }

    private void moveHero(Direction direction) {
        int[] newPos = hero.getNewPosition(direction);
        int newX = newPos[0];
        int newY = newPos[1];

        if (map.isValidMove(newX, newY)) {
            gameLog.append("➡ Hero moved to (" + newX + ", " + newY + ")\n");

            map.moveHero(direction, heroManger);
            updateMap();
            updateEnemyInfo(newX, newY); // 📌 Hareket sonrası düşman bilgilerini güncelle

            char cellContent = map.getGrid(newX, newY);
            if (cellContent == GameGlobalInstance.VILLAIN) {
                gameLog.append("⚔️ Battle! A villain is here!\n");
            } else if (cellContent == GameGlobalInstance.ARTIFACT) {
                gameLog.append("✨ Found an artifact! Do you want to keep it?\n");
            }
        } else {
            gameLog.append("⛔ Cannot move there!\n");
        }
    }
    private void updateMap() {
        mapPanel.removeAll();
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                mapPanel.add(getMapSymbol(i, j));
            }
        }
        mapPanel.revalidate();
        mapPanel.repaint();
    }

    // 📌 Harita hücrelerini simgeyle göster
    private JLabel getMapSymbol(int x, int y) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));

        String imagePath; // Varsayılan resim

        if (!map.isVisible(x, y)) {
            imagePath = UNVISIBLE_ICON_PATH;
        } else {
            switch (map.getGrid(x, y)) {
                case GameGlobalInstance.HERO:
                    imagePath = hero.getAvatarPath();
                    break;
                case GameGlobalInstance.VILLAIN:
                    imagePath = VILLAIN_ICON_PATH;
                    break;
                case GameGlobalInstance.VILLAIN2:
                    imagePath = VILLAIN2_ICON_PATH;
                    break;
                case GameGlobalInstance.VILLAIN3:
                    imagePath = VILLAIN3_ICON_PATH;
                    break;
                case GameGlobalInstance.VILLAIN4:
                    imagePath = VILLAIN4_ICON_PATH;
                    break;
                case GameGlobalInstance.VILLAIN5:
                    imagePath = VILLAIN5_ICON_PATH;
                    break;
                case GameGlobalInstance.VILLAIN6:
                    imagePath = VILLAIN6_ICON_PATH;
                    break;
                case GameGlobalInstance.ARTIFACT:
                    imagePath = ARTIFACT_ICON_PATH;
                    break;
                default:
                    imagePath = EMPTY_ICON_PATH;
                    break;
            }
        }

        // Resmi önbellekten al
        label.setIcon(GameGlobalInstance.getImage(imagePath));

        return label;
    }

    // 📌 Avatarı Güncelle
    private void updateAvatar(String avatarPath) {
        if (avatarPath != null && !avatarPath.isEmpty()) {
            ImageIcon icon = new ImageIcon(avatarPath);
            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            avatarLabel.setIcon(new ImageIcon(img));
        } else {
            avatarLabel.setIcon(null);
        }
    }

    private void updateEnemyInfo(int x, int y) {
        int[] enemyPosition = map.getNearbyEnemyPosition(x, y);
        if (enemyPosition != null) {
            // 📌 Düşman Avatarını Güncelle
            updateEnemyAvatar(map.getEnemyPath(enemyPosition[0], enemyPosition[1]));

            // 📌 Varsayılan olarak düşman bilgilerini güncelle
            enemyStats.setText("Enemy Detected!\n");
            enemyStats.append("Position: (" + enemyPosition[0] + ", " + enemyPosition[1] + ")\n");
            enemyStats.append("Health: 100 HP\n");
            enemyStats.append("Strength: 10\n");
        } else {
            enemyStats.setText("No enemy nearby.");
            updateEnemyAvatar(null); // 📌 Düşman yoksa avatarı kaldır
        }
    }

    // 📌 Düşman Avatarını Güncelle
    private void updateEnemyAvatar(String avatarPath) {
        if (avatarPath != null && !avatarPath.isEmpty()) {
            ImageIcon icon = new ImageIcon(avatarPath);
            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            enemyAvatarLabel.setIcon(new ImageIcon(img));
        } else {
            enemyAvatarLabel.setIcon(null); // 📌 Düşman yoksa avatarı gösterme
        }
    }

    // 📌 Kahraman Bilgilerini Güncelle
    private void updateHeroStats() {
        heroStats.setText(
                "🛡️ Name: " + hero.getName() + "\n" +
                        "⚔️ Class: " + hero.getHeroClass() + "\n" +
                        "📈 Level: " + hero.getLevel() + "\n" +
                        "🏅 Exp: " + hero.getExperience() + "\n" +
                        "❤️ HP: " + hero.getHitPoints()
        );
    }
}