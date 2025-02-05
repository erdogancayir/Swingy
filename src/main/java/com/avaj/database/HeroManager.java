package com.avaj.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HeroManager extends DbInterface {

    public HeroManager() {
        super();
    }

    public void createHeroesTable() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS Heroes ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "name VARCHAR(255) NOT NULL, "
                + "powerLevel INT NOT NULL"
                + ")";
        executeUpdate(createTableQuery);
        System.out.println("Heroes table created or already exists.");
    }

    public void insertHero(String name, int powerLevel) {
        String insertQuery = String.format("INSERT INTO Heroes (name, powerLevel) VALUES ('%s', %d)", name, powerLevel);
        executeUpdate(insertQuery);
        System.out.println("Hero inserted successfully.");
    }

    public void printAllHeroes() {
        String selectQuery = "SELECT * FROM Heroes";
        try (ResultSet rs = executeQuery(selectQuery)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") +
                        ", Power Level: " + rs.getInt("powerLevel"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
