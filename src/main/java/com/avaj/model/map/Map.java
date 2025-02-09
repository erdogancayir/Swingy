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
    private final boolean[][] visibility;
    private final Hero hero;
    private final Random random = new Random();
    private final int visionRange = 3; // 🔥 Kahramanın görüş mesafesi

    public Map(Hero hero) {
        this.hero = hero;
        this.size = calculateMapSize(hero.getLevel());
        this.grid = new char[size][size];
        this.visibility = new boolean[size][size];
        initializeMap();
    }

    private void initializeMap() {
        fillGridWithUnknown();
        placeHeroAtCenter();
        placeRandomEntities(VILLAIN, size / 5);
        placeRandomEntities(ARTIFACT, size / 10);
        updateVisibility(); // Kahramanın etrafını açığa çıkar
    }

    private int calculateMapSize(int level) {
        return (level - 1) * 5 + 10 - (level % 2);
    }

    private void fillGridWithUnknown() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = UNKNOWN;  // 🎭 Başlangıçta her yer bilinmez
                visibility[i][j] = false;
            }
        }
    }

    private void updateVisibility() {
        int heroX = hero.getX();
        int heroY = hero.getY();

        for (int dx = -visionRange; dx <= visionRange; dx++) {
            for (int dy = -visionRange; dy <= visionRange; dy++) {
                int nx = heroX + dx;
                int ny = heroY + dy;
                if (isValidMove(nx, ny)) {
                    visibility[nx][ny] = true;
                }
            }
        }
    }

    private void placeHeroAtCenter() {
        int center = size / 2;
        hero.setPosition(center, center);
        grid[center][center] = HERO;
        visibility[center][center] = true; // Kahramanın başladığı nokta görülebilir
    }

    private void placeRandomEntities(char entity, int count) {
        for (int i = 0; i < count; i++) {
            int x, y;
            do {
                x = random.nextInt(size);
                y = random.nextInt(size);
            } while (grid[x][y] != UNKNOWN); // 🎯 Sadece bilinmeyen yerlere yerleştir
            grid[x][y] = entity;
        }
    }

    public boolean isVisible(int x, int y) {
        return visibility[x][y] || grid[x][y] != UNKNOWN;
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
        updateVisibility(); // 🔥 Hareket sonrası görüş alanını güncelle
    }

    private void updateHeroPosition(int newX, int newY) {
        grid[hero.getX()][hero.getY()] = EMPTY;
        hero.setPosition(newX, newY);
        grid[newX][newY] = HERO;
    }

    public boolean isValidMove(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    private void handleEncounter(int x, int y) {
        switch (grid[x][y]) {
            case VILLAIN -> System.out.println("⚔️ You encountered a villain! Prepare for battle!");
            case ARTIFACT -> System.out.println("✨ You found an artifact! Do you want to keep it?");
            default -> {}
        }
    }

    public boolean isHeroAtBorder() {
        return hero.getX() == 0 || hero.getX() == size - 1 || hero.getY() == 0 || hero.getY() == size - 1;
    }

    public int getSize() {
        return size;
    }

    public char getGrid(int x, int y) {
        return grid[x][y];
    }
}
