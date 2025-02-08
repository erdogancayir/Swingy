package com.avaj.hero;

import com.avaj.database.HeroManager;
import com.avaj.model.artifact.Armor;
import com.avaj.model.artifact.Helm;
import com.avaj.model.artifact.Weapon;
import com.avaj.model.hero.Warrior;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void CreateAndInsertHero()
    {
        heroManager.insertHero("Arthur", "Warrior", 1, 0, 15, 10, 100);
        heroManager.insertHero("Merlin", "Mage", 1, 0, 20, 5, 80);
        heroManager.insertHero("Shadow", "Rogue", 1, 0, 18, 7, 90);

        heroManager.printAllHeroes();
    }

    @Test
    @Order(2)
    void testRemoveHeroAndValidate()
    {
        heroManager.insertHero("Arthur", "Warrior", 1, 0, 15, 10, 100);
        heroManager.insertHero("Merlin", "Mage", 1, 0, 20, 5, 80);
        heroManager.insertHero("Shadow", "Rogue", 1, 0, 18, 7, 90);

        heroManager.removeHero("Merlin");

        var totalHeroCount = heroManager.getHeroCount();
        assertEquals(2, totalHeroCount, "Total hero count of db is not expected value");

        heroManager.printAllHeroes();
    }

    @Test
    @Order(3)
    void testHeroLevelUp() {
        Warrior testHero = new Warrior("Warrior") {};
        testHero.gainExperience(1500);

        assertEquals(2, testHero.getLevel(), "Hero should level up to 2");
        assertEquals(20, testHero.getAttack(), "Attack should increase by 5");
        assertEquals(15, testHero.getDefense(), "Defense should increase by 5");
        assertEquals(110, testHero.getHitPoints(), "Hit points should increase by 10");
    }

    @Test
    @Order(4)
    void testEquipArtifact() {
        Warrior testHero = new Warrior("Warrior") {};

        Weapon sword = new Weapon("Excalibur", 10);
        Armor shield = new Armor("Dragon Shield", 5);
        Helm helmet = new Helm("Golden Helm", 7);

        testHero.equipArtifact(sword);
        testHero.equipArtifact(shield);
        testHero.equipArtifact(helmet);

        assertEquals(25, testHero.getAttack(), "Weapon should increase attack");
        assertEquals(15, testHero.getDefense(), "Armor should increase defense");
        assertEquals(107, testHero.getHitPoints(), "Helm should increase hit points");
    }
}
