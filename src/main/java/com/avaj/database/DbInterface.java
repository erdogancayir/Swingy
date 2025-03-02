package com.avaj.database;

import java.sql.*;

public abstract class DbInterface {
    private static final String DB_URL = "jdbc:h2:./src/main/resources/HeroesDB/HeroesH2";
    private static final String USER = "sa";
    private static final String PASS = "";

    private Connection connection;

    protected DbInterface() {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("✅ H2 Database connected successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to H2 database", e);
        }
    }

    protected Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {  // ✅ Check if connection is closed
                System.out.println("⚠️ Connection was closed, reopening...");
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("❌ Connection could not be retrieved!", e);
        }
    }

    public int executeUpdate(String query) {

        try (Connection conn = getConnection();  // Her seferinde yeni bağlantı al
             Statement statement = conn.createStatement()) {
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException("Update execution failed: " + query, e);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("H2 Database connection closed.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close H2 database connection", e);
        }
    }
}