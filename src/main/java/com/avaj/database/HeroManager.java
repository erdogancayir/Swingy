package com.avaj.database;
import com.avaj.model.hero.Hero;
import com.avaj.model.hero.Mage;
import com.avaj.model.hero.Rogue;
import com.avaj.model.hero.Warrior;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HeroManager extends DbInterface {

    public HeroManager() {
        super();

        createHeroesTable();

        Warrior warrior = new Warrior("Warrior");
        Mage mage = new Mage("Mage");
        Rogue rogue = new Rogue("Rogue");

        AddHeroToDb(warrior);
        AddHeroToDb(mage);
        AddHeroToDb(rogue);
    }

    public void createHeroesTable() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS Heroes ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "name VARCHAR(255) NOT NULL, "
                + "\"HERO_CLASS\" VARCHAR(50) NOT NULL, "  // ✅ HERO_CLASS büyük harf olduğu için tırnak içinde
                + "level INT NOT NULL, "
                + "experience INT NOT NULL, "
                + "attack INT NOT NULL, "
                + "defense INT NOT NULL, "
                + "hit_points INT NOT NULL, "  // ✅ Eksik olan virgül eklendi
                + "\"POSITION_X\" INT NOT NULL, "  // ✅ Büyük harf için çift tırnak eklendi
                + "\"POSITION_Y\" INT NOT NULL"    // ✅ Büyük harf için çift tırnak eklendi
                + ")";

        executeUpdate(createTableQuery);
        System.out.println("✅ Heroes table created or already exists.");
    }

    public void AddHeroToDb(Hero hero) {
        if (!isHeroExists(hero.getName())) {  // Eğer kahraman yoksa ekle
            insertHero(hero.getName(), hero.getHeroClass(), hero.getLevel(), hero.getExperience(), hero.getAttack(), hero.getDefense(), hero.getHitPoints(), 0, 0);
        } else {
            System.out.println("⚠️ Hero already exists: " + hero.getName());
        }
    }

    public boolean isHeroExists(String name) {
        String query = "SELECT COUNT(*) FROM Heroes WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error checking if hero exists", e);
        }
        return false;
    }


    public void insertHero(String name, String heroClass, int level, int experience, int attack, int defense, int hitPoints, int x, int y) {
        String insertQuery = "INSERT INTO Heroes (name, \"HERO_CLASS\", level, experience, attack, defense, hit_points, \"POSITION_X\", \"POSITION_Y\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, name);
            stmt.setString(2, heroClass);
            stmt.setInt(3, level);
            stmt.setInt(4, experience);
            stmt.setInt(5, attack);
            stmt.setInt(6, defense);
            stmt.setInt(7, hitPoints);
            stmt.setInt(8, x);  // ✅ X pozisyonu ekleme
            stmt.setInt(9, y);  // ✅ Y pozisyonu ekleme

            stmt.executeUpdate();
            System.out.println("✅ Hero inserted successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error inserting hero", e);
        }
    }

    public void updateHeroPosition(String name, int newX, int newY) {
        String updateQuery = "UPDATE Heroes SET \"POSITION_X\" = ?, \"POSITION_Y\" = ? WHERE name = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setInt(1, newX);
            stmt.setInt(2, newY);
            stmt.setString(3, name);

            stmt.executeUpdate();
            System.out.println("✅ Hero position updated in database.");
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error updating hero position", e);
        }
    }

    public void removeHero(String name) {
        String removeQuery = "DELETE FROM Heroes WHERE name = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(removeQuery)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            System.out.println("✅ Hero removed successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error removing hero", e);
        }
    }

    // get hero count
    public int getHeroCount() {
        String query = "SELECT COUNT(*) FROM Heroes";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error counting heroes", e);
        }
        return 0;
    }

    public List<String> getAllHeroes() {
        List<String> heroes = new ArrayList<>();
        String query = "SELECT name FROM Heroes";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                heroes.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error fetching hero list", e);
        }
        return heroes;
    }

    public ArrayList<Hero> GetAllHeroesArrayList()
    {
        var heroes = new ArrayList<Hero>();
        String query = "SELECT * FROM Heroes";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String name = rs.getString("name");
                String heroClass = rs.getString("hero_class");
                int level = rs.getInt("level");
                int experience = rs.getInt("experience");
                int attack = rs.getInt("attack");
                int defense = rs.getInt("defense");
                int hitPoints = rs.getInt("hit_points");
                int positionX = rs.getInt("POSITION_X");
                int positionY = rs.getInt("POSITION_Y");

                Hero hero = switch (heroClass.toLowerCase()) {
                    case "mage" ->
                            new Mage(name, heroClass, level, experience, attack, defense, hitPoints, positionX, positionY);
                    case "warrior" ->
                            new Warrior(name, level, experience, attack, defense, hitPoints, positionX, positionY);
                    case "rogue" ->
                            new Rogue(name, level, experience, attack, defense, hitPoints, positionX, positionY);
                    default -> throw new RuntimeException("❌ Unknown hero class: " + heroClass);
                };

                heroes.add(hero);
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error fetching hero list", e);
        }

        return heroes;
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
