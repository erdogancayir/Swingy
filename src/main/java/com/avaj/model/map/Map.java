package com.avaj.model.map;
import com.avaj.database.HeroManager;
import com.avaj.model.hero.Hero;
import com.avaj.model.GameGlobalInstance;

import java.util.Random;

import static com.avaj.model.GameGlobalInstance.*;

public class Map
{
    private final int size;
    private final char[][] grid;
    private final Hero hero;
    private final Random random = new Random();

    public Map(Hero hero) {
        this.hero = hero;
        this.size = calculateMapSize(hero.getLevel());
        this.grid = new char[size][size];
        initializeMap();
    }

    private void initializeMap() {
        fillGridWithEmpty();
        placeHeroAtCenter();
        placeRandomEntities(VILLAIN, size / 5);
        placeRandomEntities(ARTIFACT, size / 10);
    }

    private int calculateMapSize(int level) {
        return (level - 1) * 5 + 10 - (level % 2);
    }

    private void fillGridWithEmpty() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = EMPTY;
            }
        }
    }

    private void placeHeroAtCenter() {
        int center = size / 2;
        hero.setPosition(center, center);
        grid[center][center] = HERO;
    }

    private void placeRandomEntities(char entity, int count) {
        for (int i = 0; i < count; i++) {
            int x, y;
            do {
                x = random.nextInt(size);
                y = random.nextInt(size);
            } while (grid[x][y] != GameGlobalInstance.EMPTY);
            grid[x][y] = entity;
        }
    }

    public void moveHero(Direction direction, HeroManager heroManager) {
        int[] newPos = hero.getNewPosition(direction);
        int newX = newPos[0];
        int newY = newPos[1];

        if (!isValidMove(newX, newY)) {
            System.out.println("🚫 Invalid move! Stay within the boundaries.");
            return;
        }

        updateHeroPosition(newX, newY); // ✅ Haritada pozisyonu güncelle
        heroManager.updateHeroPosition(hero.getName(), newX, newY); // ✅ DB'de pozisyonu güncelle
        handleEncounter(newX, newY);
    }

    private void updateHeroPosition(int newX, int newY) {
        grid[hero.getX()][hero.getY()] = EMPTY;
        hero.setPosition(newX, newY);
        grid[newX][newY] = HERO;
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    private void handleEncounter(int x, int y) {
        switch (grid[x][y]) {
            case VILLAIN -> System.out.println("⚔️ You encountered a villain! Prepare for battle!");
            case ARTIFACT -> System.out.println("✨ You found an artifact! Do you want to keep it?");
            default -> {}
        }
    }

    public void printMap() {
        for (char[] row : grid) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    public boolean isHeroAtBorder() {
        return hero.getX() == 0 || hero.getX() == size - 1 || hero.getY() == 0 || hero.getY() == size - 1;
    }

    public int getSize() {
        return size;
    }
}
