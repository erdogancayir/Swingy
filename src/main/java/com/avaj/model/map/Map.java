package com.avaj.model.map;
import com.avaj.database.HeroManager;
import com.avaj.model.enemy.Enemy;
import com.avaj.model.hero.Hero;

import java.util.Random;

import static com.avaj.model.GameGlobalInstance.*;
import static com.avaj.model.enemy.EnemyFactory.createEnemyForHero;

public class Map
{
    private final int size;
    private final char[][] grid;
    private final boolean[][] visibility;
    private final Hero hero;
    private final Random random = new Random();
    private final int visionRange = 1; // 🔥 Kahramanın görüş mesafesi

    public Map(Hero hero) {
        this.hero = hero;
        this.size = calculateMapSize(hero.getLevel());
        this.grid = new char[size][size];
        this.visibility = new boolean[size][size];
        initializeMap();
    }

    private void initializeMap() {
        int center = size / 2;

        fillGridWithEmpty();

        placeRandomEntities(GhostEnemy, size / 5);
        placeRandomEntities(ZombieGirlEnemy, size / 5);
        placeRandomEntities(ZombieGirlEnemy, size / 5);
        placeRandomEntities(ZombieGirlEnemy, size / 5);
        placeRandomEntities(BlueEnemy, size / 5);
        placeRandomEntities(BlueEnemy, size / 5);
        placeRandomEntities(GreenEnemy, size / 5);
        placeRandomEntities(GreenEnemy, size / 5);
        placeRandomEntities(PurpleVILLAIN, size / 5);
        placeRandomEntities(ZombieGuyVILLAIN, size / 5);
        placeRandomEntities(ZombieGuyVILLAIN, size / 5);
        placeRandomEntities(ARTIFACT, size / 3);

        placeHeroAtCenter(hero.getX(), hero.getY());

        updateVisibility(); // Kahramanın etrafını açığa çıkar
    }

    private int calculateMapSize(int level) {
        return (level - 1) * 5 + 10 - (level % 2);
    }

    private void fillGridWithEmpty() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = EMPTY;
                visibility[i][j] = false; // 🔥 Her şey başlangıçta görünmez olacak
            }
        }
    }

    private void updateVisibility() {
        // 📌 Önce tüm haritayı kapat
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                visibility[i][j] = false;
            }
        }

        int heroX = hero.getX();
        int heroY = hero.getY();

        // 📌 Yeni görüş alanını aç
        for (int dx = -visionRange; dx <= visionRange; dx++) {
            for (int dy = -visionRange; dy <= visionRange; dy++) {
                int nx = heroX + dx;
                int ny = heroY + dy;
                if (isValidMove(nx, ny)) {
                    // Rastgele bir düzen oluştur, örneğin 2 üstten, 1 alttan, çapraz açık kalsın
                    if (random.nextBoolean() || (dx == -2 || dy == -1 || (dx == -1 && dy == -1))) {
                        visibility[nx][ny] = true;
                    }
                }
            }
        }

        // 📌 Kahramanın olduğu hücreyi her zaman görünür yap
        visibility[heroX][heroY] = true;
    }


    private void placeHeroAtCenter(int x, int y) {
        hero.setPosition(x, y);
        grid[x][y] = HERO;
        visibility[x][y] = true; // Kahramanın başladığı nokta görülebilir
    }

    private void placeRandomEntities(char entity, int count) {
        for (int i = 0; i < count; i++) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);

            grid[x][y] = entity;
        }
    }

    public boolean isVisible(int x, int y) {
        return visibility[x][y];
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
            case GhostEnemy -> System.out.println("⚔️ You encountered a villain! Prepare for battle!");
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

    public int[] getNearbyEnemyPosition(int x, int y) {
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},  // Sol üst, üst, sağ üst
                {0, -1},          {0, 1},   // Sol, sağ
                {1, -1}, {1, 0}, {1, 1}     // Sol alt, alt, sağ alt
        };

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            if (isWithinBounds(newX, newY) && isEnemy(grid[newX][newY])) {
                return new int[]{newX, newY};  // Düşmanın koordinatlarını döndür
            }
        }
        return null; // Eğer düşman yoksa null döndür
    }

    public String getEnemyPath(int x, int y)
    {
        if (grid[x][y] == GhostEnemy)
            return GhostEnemyICON_PATH;
        else if (grid[x][y] == ZombieGirlEnemy)
            return ZombieGirlEnemy_ICON_PATH;
        else if (grid[x][y] == BlueEnemy)
            return BlueEnemy_Icon_Path;
        else if (grid[x][y] == GreenEnemy)
            return GreenEnemy_ICON_PATH;
        else if (grid[x][y] == PurpleVILLAIN)
            return PurpleVILLAIN5_ICON_PATH;
        else
            return ZombieVILLAIN6_ICON_Path;
    }

    public boolean isEnemy(char cell) {
        return cell == GhostEnemy || cell == ZombieGirlEnemy || cell == BlueEnemy ||
                cell == GreenEnemy || cell == PurpleVILLAIN || cell == ZombieGuyVILLAIN;
    }

    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }

    public Enemy getEnemyAt(int x, int y)
    {
        if (grid[x][y] == GhostEnemy)
            return createEnemyForHero(hero, GhostEnemyICON_PATH, 1, "Ghost Villain");
        else if (grid[x][y] == ZombieGirlEnemy)
            return createEnemyForHero(hero, ZombieGirlEnemy_ICON_PATH, 2, "Zombie Girl");
        else if (grid[x][y] == BlueEnemy)
            return createEnemyForHero(hero, BlueEnemy_Icon_Path, 3, "Blue Villain");
        else if (grid[x][y] == GreenEnemy)
            return createEnemyForHero(hero, GreenEnemy_ICON_PATH, 4, "Green Villain");
        else if (grid[x][y] == PurpleVILLAIN)
            return createEnemyForHero(hero, PurpleVILLAIN5_ICON_PATH,5, "Purple Villain");
        else
            return createEnemyForHero(hero, ZombieVILLAIN6_ICON_Path, 6, "Zombie Boy");
    }
}

