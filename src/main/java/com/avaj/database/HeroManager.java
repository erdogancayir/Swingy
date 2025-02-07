package com.avaj.database;
import java.sql.*;

public class HeroManager extends DbInterface {

    public HeroManager() {
        super();
    }

    public void createHeroesTable() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS Heroes ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "name VARCHAR(255) NOT NULL, "
                + "\"HERO_CLASS\" VARCHAR(50) NOT NULL, "  // Büyük harf için düzeltildi
                + "level INT NOT NULL, "
                + "experience INT NOT NULL, "
                + "attack INT NOT NULL, "
                + "defense INT NOT NULL, "
                + "hit_points INT NOT NULL"
                + ")";

        executeUpdate(createTableQuery);
        System.out.println("✅ Heroes table created or already exists.");
    }

    public void insertHero(String name, String heroClass, int level, int experience, int attack, int defense, int hitPoints) {
       // String insertQuery = "INSERT INTO Heroes (name, hero_class, level, experience, attack, defense, hit_points) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertQuery = "INSERT INTO Heroes (name, \"HERO_CLASS\", level, experience, attack, defense, hit_points) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, name);
            stmt.setString(2, heroClass);
            stmt.setInt(3, level);
            stmt.setInt(4, experience);
            stmt.setInt(5, attack);
            stmt.setInt(6, defense);
            stmt.setInt(7, hitPoints);

            stmt.executeUpdate();
            System.out.println("✅ Hero inserted successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error inserting hero", e);
        }
    }

    public void printAllHeroes() {
        String selectQuery = "SELECT * FROM Heroes";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(selectQuery); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("name") +
                        ", Class: " + rs.getString("hero_class") +
                        ", Level: " + rs.getInt("level") +
                        ", XP: " + rs.getInt("experience") +
                        ", Attack: " + rs.getInt("attack") +
                        ", Defense: " + rs.getInt("defense") +
                        ", HP: " + rs.getInt("hit_points"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error retrieving heroes", e);
        }
    }

    public Connection getConnection() {
        return super.getConnection();
    }
}
