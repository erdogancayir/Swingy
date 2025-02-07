package com.avaj.hero;

import com.avaj.database.HeroManager;
import com.avaj.dbConnectionTest.H2DbTest;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HeroTest
{
    public HeroManager heroManager;

    @BeforeAll
    void setupDatabase() throws SQLException {
        heroManager = new HeroManager();
        System.out.println("✅ setupDatabase() - Is connection open? " + !heroManager.getConnection().isClosed());

        heroManager.createHeroesTable();
    }

    @BeforeEach
    void cleanDatabase() throws SQLException {
        System.out.println("🧹 Cleaning database - Is connection open? " + !heroManager.getConnection().isClosed());

        try {
            Connection conn = heroManager.getConnection();  // ✅ get connection
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM Heroes");
            System.out.println("🧹 All records deleted.");
        } catch (SQLException e) {
            System.err.println("⚠️ WARNING: 'Heroes' table not found or could not be cleared.");
        }
    }

    @AfterAll
    void tearDown() {
        System.out.println("✅ Closing database connection after all tests.");
        heroManager.closeConnection(); // ✅ Only close after all tests finish
    }

    @Test
    @Order(1)
    void CreateHero()
    {
        heroManager.insertHero("Arthur", "Warrior", 1, 0, 15, 10, 100);
        heroManager.insertHero("Merlin", "Mage", 1, 0, 20, 5, 80);
        heroManager.insertHero("Shadow", "Rogue", 1, 0, 18, 7, 90);

        heroManager.printAllHeroes();
    }
}
