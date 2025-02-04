package com.avaj.database;

import java.sql.*;

public abstract class DbInterface {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./src/main/resources/HeroesDB/HeroesH2";
    private static final String USER = "user";
    private static final String PASS = "";

    private Connection connection;

    protected DbInterface() {
        try {
            Class.forName(JDBC_DRIVER);
            this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("H2 Database connected successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to H2 database", e);
        }
    }

    protected Connection getConnection() {
        return connection;
    }

    public ResultSet executeQuery(String query) {
        try (Statement statement = connection.createStatement()) {
            return statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException("Query execution failed: " + query, e);
        }
    }

    public int executeUpdate(String query) {
        try (Statement statement = connection.createStatement()) {
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
