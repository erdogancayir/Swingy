package com.avaj.view.gui;

import com.avaj.database.HeroManager;
import com.avaj.model.hero.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.JTableHeader;

public class SpawnHeroGuiView {
    private final JFrame frame;
    private final ArrayList<Hero> heroesList;
    private JTable table;
    private Hero heroSelectedOnRow;
    private Hero selectedHero;
    private final HeroManager heroManager;

    public SpawnHeroGuiView(ArrayList<Hero> heroesList, HeroManager heroManager) {
        this.heroesList = heroesList;
        this.heroManager = heroManager;
        this.frame = new JFrame("Spawn New Hero");

        initializeUI();
        updateTable();
    }

    public Hero getHeroSelectedOnRow() {
        return selectedHero;
    }

    // 📌 Arayüzü başlatma metodu
    private void initializeUI() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // 📌 Tablo oluşturuluyor
        table = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        table.setFont(new Font("Arial", Font.BOLD, 18));
        table.setRowHeight(30);
        frame.add(tableScrollPane, BorderLayout.CENTER);

        // 📌 Seçilen kahramanı yakalamak için event listener ekleniyor
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                int row = table.getSelectedRow();
                if (row >= 0 && row < heroesList.size()) {
                    heroSelectedOnRow = heroesList.get(row);
                    displayHeroStats(heroSelectedOnRow);
                }
            }
        });

        // 📌 Butonlar (Seçme ve Yeni Kahraman Yaratma)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton selectButton = new JButton("🎯 Select Hero");
        styleButton(selectButton);
        selectButton.addActionListener(e -> selectHero());

        JButton createButton = new JButton("➕ Create New Hero");
        styleButton(createButton);
        createButton.addActionListener(e -> createNewHero());

        buttonPanel.add(selectButton);
        buttonPanel.add(createButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // 📌 Tabloyu güncelleyen metot
    private void updateTable() {
        String[] columns = {"NAME", "CLASS", "LEVEL", "EXP"};
        Object[][] data = new Object[heroesList.size()][4];

        for (int i = 0; i < heroesList.size(); i++) {
            data[i][0] = heroesList.get(i).getName();
            data[i][1] = heroesList.get(i).getClass().getSimpleName();
            data[i][2] = heroesList.get(i).getLevel();
            data[i][3] = heroesList.get(i).getExperience();
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setForeground(Color.BLUE);
        header.setBackground(Color.ORANGE);
        header.setReorderingAllowed(false);

        table.setModel(tableModel);
    }

    // 📌 Seçili kahramanı belirleyen metot
    private void selectHero() {
        if (heroSelectedOnRow != null) {
            selectedHero = heroSelectedOnRow;
            JOptionPane.showMessageDialog(frame, "🎉 Selected Hero: " + heroSelectedOnRow.getName(),
                    "Hero Selected", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "⚠️ Please select a hero first!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // 📌 Yeni kahraman yaratma süreci
    private void createNewHero() {
        String name = JOptionPane.showInputDialog(frame, "Enter new hero's name:", "Create Hero", JOptionPane.PLAIN_MESSAGE);

        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "⚠️ Hero name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (name.length() > 20) {
            JOptionPane.showMessageDialog(frame, "⚠️ Hero name must be 20 characters or less!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] heroClasses = {"Rogue", "Warrior", "Mage"};
        int choice = JOptionPane.showOptionDialog(frame, "Choose a Hero Class:",
                "Hero Class Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, heroClasses, heroClasses[0]);

        if (choice == -1) {
            return; // Kullanıcı iptal etti
        }

        Hero newHero;
        switch (choice) {
            case 0 -> newHero = new Rogue(name);
            case 1 -> newHero = new Warrior(name);
            case 2 -> newHero = new Mage(name);
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        }

        heroManager.insertHero(
                newHero.getName(),
                newHero.getHeroClass(),
                newHero.getLevel(),
                newHero.getExperience(),
                newHero.getAttack(),
                newHero.getDefence(),
                newHero.getHitPoints(),
                newHero.getX(),
                newHero.getY()
        );

        heroesList.add(newHero);
        updateTable();
        JOptionPane.showMessageDialog(frame, "🎉 Hero Created: " + newHero.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // 📌 Kahramanın istatistiklerini gösteren metot
    private void displayHeroStats(Hero hero) {
        // 📌 Varsayılan avatar belirle
        ImageIcon icon = new ImageIcon("default_avatar.png"); // Eğer kahramanın avatarı yoksa bu kullanılacak

        // 📌 Kahramanın özel avatarı varsa onu yükle
        if (hero.getAvatarPath() != null && !hero.getAvatarPath().isEmpty()) {
            ImageIcon originalIcon = new ImageIcon(hero.getAvatarPath());
            Image img = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
        }

        // 📌 Avatarı bir JLabel içine koy
        JLabel avatarLabel = new JLabel(icon);

        // 📌 Metin alanı oluştur
        JTextArea textArea = new JTextArea(
                "Hero Stats:\n" +
                        "Name: " + hero.getName() + "\n" +
                        "Class: " + hero.getClass().getSimpleName() + "\n" +
                        "Level: " + hero.getLevel() + "\n" +
                        "Attack: " + hero.getAttack() + "\n" +
                        "Defense: " + hero.getDefense() + "\n" +
                        "HP: " + hero.getHitPoints() + "\n" +
                        "Experience: " + hero.getExperience()
        );
        textArea.setFont(new Font("Arial", Font.BOLD, 14));
        textArea.setEditable(false);
        textArea.setOpaque(false);

        // 📌 Panel oluştur (Avatar sol tarafa, metin sağ tarafa)
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(avatarLabel, BorderLayout.WEST); // Avatar sol tarafta
        panel.add(textArea, BorderLayout.CENTER);  // Metin sağ tarafta

        // 📌 JOptionPane ile göster
        JOptionPane.showMessageDialog(frame, panel, "Hero Stats", JOptionPane.INFORMATION_MESSAGE);
    }

    // 📌 Butonları stilize eden metot
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(200, 50));
        button.setFocusPainted(false);
        button.setBackground(new Color(50, 150, 250));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }
}
