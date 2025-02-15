package com.avaj.view.console;

import com.avaj.controller.GameController;
import com.avaj.database.HeroManager;
import com.avaj.model.artifact.*;
import com.avaj.model.enemy.Enemy;
import com.avaj.model.hero.Hero;
import com.avaj.model.map.Direction;
import com.avaj.model.map.Map;

import java.util.Random;
import java.util.Scanner;

public class GameConsoleView {
    private final Hero hero;
    private final Map map;
    private final HeroManager heroManager;
    private final GameController gameController;
    private final Scanner scanner;
    private final Random random = new Random();

    public GameConsoleView(Hero hero, Map map, HeroManager heroManager, GameController gameController) {
        this.hero = hero;
        this.map = map;
        this.heroManager = heroManager;
        this.gameController = gameController;
        this.scanner = new Scanner(System.in);
        startGameLoop();
    }

    private void startGameLoop() {
        System.out.println("\n🎮 Console Game Started!");
        System.out.println("Welcome, " + hero.getName() + "!");

        while (true) {
            printMap();
            System.out.println("\n📍 Choose an action:");
            System.out.println("1. Move");
            System.out.println("2. Check Stats");
            System.out.println("3. Quit Game");

            System.out.print("Your choice:> ");
            int choice = getValidIntInput(1, 3);

            switch (choice) {
                case 1 -> moveHero();
                case 2 -> printHeroStats();
                case 3 -> {
                    saveGame();
                    System.out.println("👋 Exiting game...");
                    return;
                }
            }
        }
    }

    private void moveHero() {
        System.out.println("\n🚶‍♂️ Choose a direction:");
        System.out.println("1. North (↑)");
        System.out.println("2. South (↓)");
        System.out.println("3. East (→)");
        System.out.println("4. West (←)");
        System.out.print("Direction:> ");

        int choice = getValidIntInput(1, 4);
        Direction direction = switch (choice) {
            case 1 -> Direction.NORTH;
            case 2 -> Direction.SOUTH;
            case 3 -> Direction.EAST;
            case 4 -> Direction.WEST;
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };

        int[] newPos = hero.getNewPosition(direction);
        int newX = newPos[0];
        int newY = newPos[1];

        if (map.isValidMove(newX, newY)) {
            System.out.println("➡ Hero moved to (" + newX + ", " + newY + ")");

            char cellContent = map.getGrid(newX, newY);
            if (map.isEnemy(cellContent)) {
                handleVillainEncounter(newX, newY);
            } else if (cellContent == 'A') { // Artefact bulundu
                Artifact artifact = getRandomArtifact();
                System.out.println("✨ Found an artifact: " + artifact);
                hero.equipArtifact(artifact);
            }

            map.moveHero(direction, heroManager);
        } else {
            System.out.println("⛔ Cannot move there!");
        }
    }

    private void handleVillainEncounter(int x, int y) {
        System.out.println("⚔️ A villain blocks your path!");

        Enemy enemy = map.getEnemyAt(x, y);
        updateEnemyInfo(enemy, x, y);

        System.out.print("\nDo you want to (1) Fight or (2) Run?:> ");
        int choice = getValidIntInput(1, 2);

        if (choice == 2 && Math.random() < 0.5) {
            System.out.println("🏃 You successfully escaped!");
            return;
        }

        if (enemy != null) {
            gameController.startBattleForConsole(enemy);
        }

        if (enemy.getHitPoint() <= 0) {
            int xpGained = enemy.getStrength() * 500;
            hero.gainExperience(xpGained);
            System.out.println("🏆 " + hero.getName() + " won the battle and gained " + xpGained + " XP!");

            hero.Heal();
            printHeroStats();
        }
    }

    private Artifact getRandomArtifact() {
        int randomNumber = random.nextInt(3);
        int randomBoost = random.nextInt(1, 45);
        return switch (randomNumber) {
            case 0 -> new Armor("ARMOR", randomBoost);
            case 1 -> new Helm("HELM", randomBoost);
            case 2 -> new Weapon("WEAPON", randomBoost);
            default -> throw new IllegalStateException("Unexpected value: " + randomNumber);
        };
    }

    private void updateEnemyInfo(Enemy enemy, int x, int y) {
        if (enemy != null) {
            System.out.println("\n⚠️ Enemy Detected!");
            System.out.println("Class: " + enemy.getClas());
            System.out.println("Position: (" + x + ", " + y + ")");
            System.out.println("Health: " + enemy.getHitPoint() + " HP");
            System.out.println("Attack: " + enemy.getAttack());
            System.out.println("Defense: " + enemy.getDefence());
            System.out.println("Strength: " + enemy.getStrength());
        } else {
            System.out.println("No enemy nearby.");
        }
    }

    private void printMap() {
        System.out.println("\n🗺️ Current Map:");
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                char symbol = map.getGrid(i, j);
                if (hero.getX() == i && hero.getY() == j) {
                    System.out.print(" H ");
                } else if (map.isEnemy(symbol)) {
                    System.out.print(" 👿 ");
                } else if (symbol == 'A') {
                    System.out.print(" ✨ ");
                } else {
                    System.out.print(" ⬜ ");
                }
            }
            System.out.println();
        }
    }

    private void printHeroStats() {
        System.out.println("\n📜 Hero Stats:");
        System.out.println("Name: " + hero.getName());
        System.out.println("Class: " + hero.getHeroClass());
        System.out.println("Level: " + hero.getLevel());
        System.out.println("HP: " + hero.getHitPoints());
        System.out.println("Attack: " + hero.getAttack());
        System.out.println("Defense: " + hero.getDefense());
    }

    private void saveGame() {
        heroManager.updateHeroStatus(
                hero.getName(), hero.getLevel(), hero.getExperience(),
                hero.getAttack(), hero.getDefense(), hero.getHitPoints(),
                hero.getX(), hero.getY()
        );
        System.out.println("\n💾 Game state saved.");
    }

    private int getValidIntInput(int min, int max) {
        while (true) {
            if (!scanner.hasNextInt()) {
                System.out.print("❌ Invalid input. Please enter a number:> ");
                scanner.next();
                continue;
            }
            int input = scanner.nextInt();
            scanner.nextLine();
            if (input >= min && input <= max) {
                return input;
            }
            System.out.printf("❌ Please enter a number between %d and %d:> ", min, max);
        }
    }

    public void UpdateHeroStatAfterVictory(Enemy enemy)
    {
        if (enemy.getHitPoint() <= 0) { // Düşman öldüyse
            int xpGained = enemy.getStrength() * 500; // Düşman seviyesine bağlı XP hesapla
            hero.gainExperience(xpGained); // Kahramana XP ekle

            hero.Heal();
            heroManager.updateHeroStatus(
                    hero.getName(),
                    hero.getLevel(),
                    hero.getExperience(),
                    hero.getAttack(),
                    hero.getDefense(),
                    hero.getHitPoints(),
                    hero.getX(),
                    hero.getY()
            );
        }
    }
}
