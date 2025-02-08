package com.avaj.mapTest;

import com.avaj.database.HeroManager;
import com.avaj.model.hero.Warrior;
import com.avaj.model.map.Direction;
import org.junit.jupiter.api.*;
import com.avaj.model.map.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameMapTest {
    private Map gameMap;
    private Warrior hero;
    private HeroManager HeroManager = new HeroManager();

    @BeforeAll
    void setup() {
        HeroManager.createHeroesTable();
        hero = new Warrior("TestWarrior");
        gameMap = new Map(hero);
    }

    @Test
    @Order(1)
    void testMapInitialization() {
        assertEquals((hero.getLevel() - 1) * 5 + 10 - (hero.getLevel() % 2), gameMap.getSize(),
                "Map size calculation should be correct.");
    }

    @Test
    @Order(2)
    void testHeroStartsAtCenter() {
        int center = gameMap.getSize() / 2;
        assertEquals(center, hero.getX(), "Hero should start at the center (X).");
        assertEquals(center, hero.getY(), "Hero should start at the center (Y).");
    }

    @Test
    @Order(3)
    void testHeroMoveValid() {
        int initialX = hero.getX();
        int initialY = hero.getY();

        gameMap.moveHero(Direction.NORTH, HeroManager);
        assertEquals(initialX - 1, hero.getX(), "Hero should move up (North).");
        assertEquals(initialY, hero.getY(), "Y coordinate should remain unchanged.");
    }

    @Test
    @Order(4)
    void testHeroMoveInvalid() {
        gameMap.moveHero(Direction.NORTH, HeroManager); // Kahramanı yukarı
        gameMap.moveHero(Direction.NORTH, HeroManager); // Kahramanı yukarı taşı
        gameMap.moveHero(Direction.NORTH, HeroManager); // Kahramanı yukarı taşı
        gameMap.moveHero(Direction.NORTH, HeroManager); // Kahramanı yukarı taşı

        int minX = hero.getX(); // X minimum değer

        gameMap.moveHero(Direction.NORTH, HeroManager); // X zaten sıfıra yaklaştıysa sınır dışına çıkamaz
        assertEquals(minX, hero.getX(), "Hero should not move outside map boundaries.");
    }

    @Test
    @Order(5)
    void testHeroAtBorder() {
        while (!gameMap.isHeroAtBorder()) {
            gameMap.moveHero(Direction.NORTH, HeroManager);
        }
        assertTrue(gameMap.isHeroAtBorder(), "Hero should be at the border.");
    }
}