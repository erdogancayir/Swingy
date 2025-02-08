package com.avaj.view.gui;

import com.avaj.model.hero.Hero;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class SpawnHeroGuiView {
    private final JFrame frame;
    private final ArrayList<Hero> heroesList;
    private JTable table;
    private Hero selectedHero;

    private Hero hero;

    public SpawnHeroGuiView(ArrayList<Hero> heroesList) {
        this.heroesList = heroesList;
        this.frame = new JFrame("Spawn New Hero");

        initializeUI();
        updateTable();
    }

    // 📌 Arayüzü başlatma metodu
    private void initializeUI() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null); // Pencereyi ekran ortasına getir
        frame.setLayout(new BorderLayout());

        // 📌 Tablo oluşturuluyor
        table = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        frame.add(tableScrollPane, BorderLayout.CENTER);

        // 📌 Seçilen kahramanı yakalamak için event listener ekleniyor
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                int row = table.getSelectedRow();
                if (row >= 0 && row < heroesList.size()) {
                    selectedHero = heroesList.get(row);
                    displayHeroStats(selectedHero);
                }
            }
        });

        // 📌 "Seç ve Devam Et" butonu
        JButton selectButton = new JButton("Select Hero");
        selectButton.addActionListener(e -> {
            if (selectedHero != null) {
                JOptionPane.showMessageDialog(frame, "Selected Hero: " + selectedHero.getName());
                // 📌 Kahraman seçildikten sonra yapılacak işlemler buraya eklenebilir
                frame.dispose(); // Pencereyi kapat
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a hero first!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        frame.add(selectButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }


    // 📌 Tabloyu güncelleyen metot
    private void updateTable() {
        String[] columns = {"NAME", "CLASS", "LEVEL", "EXP"};
        Object[][] data = new Object[heroesList.size()][4];

        for (int i = 0; i < heroesList.size(); i++) {
            data[i][0] = heroesList.get(i).getName();
            data[i][1] = heroesList.get(i).getHeroClass();
            data[i][2] = heroesList.get(i).getLevel();
            data[i][3] = heroesList.get(i).getExperience();
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(tableModel);
    }

    // 📌 Kahramanın istatistiklerini gösteren metot
    private void displayHeroStats(Hero hero) {
        JOptionPane.showMessageDialog(frame,
                "Hero Stats:\n"
                        + "Name: " + hero.getName() + "\n"
                        + "Class: " + hero.getHeroClass() + "\n"
                        + "Level: " + hero.getLevel() + "\n"
                        + "Exp: " + hero.getExperience(),
                "Hero Stats", JOptionPane.INFORMATION_MESSAGE);
    }

}
