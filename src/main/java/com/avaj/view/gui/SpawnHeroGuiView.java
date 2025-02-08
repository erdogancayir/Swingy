package com.avaj.view.gui;

import javax.swing.*;

public class SpawnHeroGuiView {
    private final JFrame frame;

    public SpawnHeroGuiView() {
        frame = new JFrame("Spawn New Hero");

        // Boyut ayarla
        frame.setSize(400, 300);

        // Kapatma işlemi tanımla
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Buton ekleyelim
        JButton spawnButton = new JButton("Spawn Hero");
        frame.add(spawnButton);

        // Layout yöneticisini belirle
        frame.setLayout(new java.awt.FlowLayout());

        // Görünür hale getir
        frame.setVisible(true);
    }

    public void showWindow() {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
}
