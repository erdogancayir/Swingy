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
    private JPanel mapPanel;     // Merkez: Oyun haritası
    private JTextArea gameLog;   // Sağ panel: Oyun olayları
    private JPanel controlPanel; // 📌 Butonları eklemek için

    public GameGuiView(Hero hero, Map map, HeroManager heroManager) {
        this.hero = hero;
        this.map = map;
        this.heroManger = heroManager;

        frame = new JFrame("Swingy - RPG Game");
        initializeUI();
    }

    private void initializeUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800); // Daha geniş ve büyük pencere
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // 📌 PENCEREYİ ORTAYA AL


        // 📌 Sol Panel: Kahraman Bilgileri
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(250, frame.getHeight()));
        leftPanel.setBackground(new Color(240, 240, 240));

        avatarLabel = new JLabel();
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateAvatar(hero.getAvatarPath());

        heroStats = new JTextArea();
        heroStats.setEditable(false);
        heroStats.setFont(new Font("Arial", Font.BOLD, 18)); // 📌 Büyük ve kalın yazı
        heroStats.setOpaque(false);
        heroStats.setMargin(new Insets(10, 10, 10, 10)); // Kenar boşluğu
        updateHeroStats();

        leftPanel.add(avatarLabel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(heroStats), BorderLayout.CENTER);

        // 📌 Orta Panel: Harita (Şimdilik Grid olarak JLabel ile gösteriyoruz)
        mapPanel = new JPanel();
        mapPanel.setLayout(new GridLayout(map.getSize(), map.getSize(), 2, 2)); // Aralarında boşluk olacak
        updateMap();  // Harita güncellensin

        // 📌 Sağ Panel: Oyun Günlüğü ve Kontroller
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(250, frame.getHeight()));
        rightPanel.setBackground(new Color(240, 240, 240));

        gameLog = new JTextArea();
        gameLog.setEditable(false);
        gameLog.setFont(new Font("Arial", Font.PLAIN, 16)); // 📌 Yazı büyütüldü
        gameLog.setMargin(new Insets(10, 10, 10, 10)); // Kenar boşluğu

        var gameStartLog = "";
        gameStartLog += "Game started...\n";
        gameStartLog += "🔮Artifacts: " + "Take any artifact.\n";
        gameStartLog += "🤺Hero: " + "It is your hero.\n";
        gameStartLog += "👹Enemy: " + "Start a battle.\n";

        gameLog.setText(gameStartLog);

        rightPanel.add(new JScrollPane(gameLog), BorderLayout.CENTER);

        // 📌 Hareket Butonları
        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3, 3, 5, 5)); // 3x3 buton yerleşimi
        addControlButtons();
        rightPanel.add(controlPanel, BorderLayout.SOUTH);

        // 📌 Frame'e panelleri ekle
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(mapPanel, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }

    // 📌 Hareket butonlarını ekle
    private void addControlButtons() {
        JButton northButton = createMovementButton("\u2B06 North", Direction.NORTH);
        JButton southButton = createMovementButton("\u2B07  South", Direction.SOUTH);
        JButton eastButton = createMovementButton("\u27A1 East", Direction.EAST);
        JButton westButton = createMovementButton("\u2B05 West", Direction.WEST);

        // 📌 Boş butonlar (merkez hariç her köşeye)
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

    // 📌 Butonları oluşturan metod
    private JButton createMovementButton(String text, Direction direction) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBackground(new Color(100, 150, 255));
        button.setForeground(Color.BLUE);
        button.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));

        // 📌 Butona tıklanınca Direction enum ile kahraman hareket edecek
        button.addActionListener(e -> moveHero(direction));

        return button;
    }

    // 📌 Kahramanı hareket ettir
    private void moveHero(Direction direction) {
        int[] newPos = hero.getNewPosition(direction);
        int newX = newPos[0];
        int newY = newPos[1];

        if (map.isValidMove(newX, newY)) {
            gameLog.append("➡ Hero moved to (" + newX + ", " + newY + ")\n");

            map.moveHero(direction, heroManger); // Kahramanı yeni konuma taşı
            updateMap();

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

    // 📌 Haritayı güncelle
    private void updateMap() {
        mapPanel.removeAll(); // Mevcut içeriği temizle
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                mapPanel.add(getMapSymbol(i, j));
            }
        }
        mapPanel.revalidate();
        mapPanel.repaint();
    }

    private void updateEnemyInfo(int x, int y) {
        int[] enemyPosition = map.getNearbyEnemyPosition(x, y);
        if (enemyPosition != null)
        {

        }
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