package com.avaj.dbConnectionTest;

import com.avaj.database.HeroManager;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class H2DbTest {

    private HeroManager heroManager;

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
            Connection conn = heroManager.getConnection();  // ✅ Bağlantıyı al
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM Heroes");
            System.out.println("🧹 All records deleted.");
        } catch (SQLException e) {
            System.err.println("⚠️ WARNING: 'Heroes' table not found or could not be cleared.");
        }
    }

    @Test
    @Order(1)
    void testCreateHeroesTable() {
        try (Connection conn = heroManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_NAME) = 'HEROES'")) {

            assertTrue(rs.next(), "Table query failed");
            assertEquals(1, rs.getInt(1), "Heroes table was not created");

        } catch (SQLException e) {
            fail("❌ Database query failed: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    void testInsertHero() {
        heroManager.insertHero("Superman", 100);

        try (Connection conn = heroManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Heroes WHERE name = 'Superman'")) {

            assertTrue(rs.next(), "Superman hero could not be inserted");
            assertEquals("Superman", rs.getString("name"), "Inserted hero name is incorrect");
            assertEquals(100, rs.getInt("powerLevel"), "Power level is incorrect");
        } catch (SQLException e) {
            fail("❌ Database query failed: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    void testPrintAllHeroes() {
        heroManager.insertHero("Batman", 90);
        heroManager.insertHero("Wonder Woman", 95);

        try (Connection conn = heroManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Heroes")) {

            assertTrue(rs.next(), "Heroes table query failed");
            assertEquals(2, rs.getInt(1), "Incorrect number of registered heroes");
        } catch (SQLException e) {
            fail("❌ Database query failed: " + e.getMessage());
        }
    }

    @AfterAll
    void tearDown() {
        System.out.println("✅ Closing database connection after all tests.");
        heroManager.closeConnection(); // ✅ Only close after all tests finish
    }
}