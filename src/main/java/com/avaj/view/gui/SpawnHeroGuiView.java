package com.avaj.view.gui;

import com.avaj.model.hero.Hero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.JTableHeader;

public class SpawnHeroGuiView {
    private final JFrame frame;
    private final ArrayList<Hero> heroesList;
    private JTable table;
    private Hero selectedHero;
    private JLabel avatarLabel;

    private Hero hero;

    public SpawnHeroGuiView(ArrayList<Hero> heroesList) {
        this.heroesList = heroesList;
        this.frame = new JFrame("Spawn New Hero");

        initializeUI();
        updateTable();
    }

    public Hero getSelectedHero() {
        return selectedHero;
    }

    // 📌 Arayüzü başlatma metodu
    private void initializeUI() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 900);
        frame.setLocationRelativeTo(null); // Pencereyi ekran ortasına getir
        frame.setLayout(new BorderLayout());

        // 📌 Tablo oluşturuluyor
        table = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        table.setFont(new Font("Arial", Font.BOLD, 18)); // Tablo yazı boyutunu 18 yapar
        table.setRowHeight(30); // Satır yüksekliğini artırarak daha okunaklı hale getirir
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
        JButton selectButton = new JButton("🎯 Select Hero"); // Butona emoji ekleyerek daha dikkat çekici hale getiriyoruz
        selectButton.setFont(new Font("Arial", Font.BOLD, 18)); // Yazı fontunu büyüt
        selectButton.setPreferredSize(new Dimension(200, 50)); // Butonun boyutunu ayarla
        selectButton.setFocusPainted(false); // Tıklanınca kenar çizgisi olmaması için

        // 📌 Buton rengi ayarları
        selectButton.setBackground(new Color(50, 150, 250)); // Mavi renk tonu
        selectButton.setForeground(Color.BLUE); // Yazı rengini beyaz yap
        selectButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Siyah çerçeve

        // 📌 Üzerine gelince renk değiştirme efekti
        selectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                selectButton.setBackground(new Color(30, 130, 230));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                selectButton.setBackground(new Color(50, 150, 250));
            }
        });

        // 📌 Butona tıklanınca çalışacak işlem
        selectButton.addActionListener(e -> {
            if (selectedHero != null) {
                JOptionPane.showMessageDialog(frame, "🎉 Selected Hero: " + selectedHero.getName(),
                        "Hero Selected", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose(); // Pencereyi kapat
            } else {
                JOptionPane.showMessageDialog(frame, "⚠️ Please select a hero first!",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        // 📌 Butonu ortalamak için bir JPanel içine ekleyelim
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(selectButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
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

        // 📌 Başlık fontunu büyütme
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 18)); // Büyük ve kalın font
        header.setForeground(Color.BLUE); // Beyaz başlık yazısı
        header.setBackground(Color.ORANGE); // Koyu gri arka plan
        header.setReorderingAllowed(false); // Sütun sıralamasını kapat

        table.setModel(tableModel);
    }

    // 📌 Kahramanın istatistiklerini gösteren metot
    private void displayHeroStats(Hero hero) {
        ImageIcon icon = null;
        if (hero.getAvatarPath() != null && !hero.getAvatarPath().isEmpty()) {
            ImageIcon originalIcon = new ImageIcon(hero.getAvatarPath());
            Image img = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
        }

        // 📌 Metin alanı oluştur
        JTextArea textArea = new JTextArea(
                "Hero Stats:\n" +
                        "Name: " + hero.getName() + "\n" +
                        "Class: " + hero.getHeroClass() + "\n" +
                        "Level: " + hero.getLevel() + "\n" +
                        "Attack: " + hero.getAttack() + "\n" +
                        "Defense: " + hero.getDefense() + "\n" +
                        "HP: " + hero.getHitPoints() + "\n" +
                        "Experience: " + hero.getExperience()
        );
        textArea.setFont(new Font("Arial", Font.BOLD, 14));
        textArea.setEditable(false);
        textArea.setOpaque(false);

        // 📌 Panel oluştur
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JLabel(icon), BorderLayout.WEST); // Avatar sol tarafta
        panel.add(textArea, BorderLayout.CENTER); // Metin sağ tarafta

        // 📌 JOptionPane ile göster
        JOptionPane.showMessageDialog(frame, panel, "Hero Stats", JOptionPane.INFORMATION_MESSAGE);
    }
}
