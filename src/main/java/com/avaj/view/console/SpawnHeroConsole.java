package com.avaj.view.console;

import java.util.ArrayList;

import com.avaj.database.HeroManager;
import com.avaj.model.hero.Hero;
import com.avaj.model.hero.Mage;
import com.avaj.model.hero.Rogue;
import com.avaj.model.hero.Warrior;

import java.util.ArrayList;
import java.util.Scanner;

public class SpawnHeroConsole {
    private final ArrayList<Hero> heroesList;
    private final Scanner scanner;
    private final HeroManager heroManager;

    public SpawnHeroConsole(ArrayList<Hero> heroesList, HeroManager heroManager) {
        this.heroesList = heroesList;
        this.heroManager = heroManager;
        this.scanner = new Scanner(System.in);
    }

    public Hero spawnHero() {
        Hero selectedHero = null;

        System.out.println("\n🎮 Welcome to the Hero Selection!");
        do {
            System.out.println("Choose your hero by entering a number:");
            System.out.println("Enter '0' to CREATE a new hero.");
            System.out.println("Enter a NEGATIVE number to DELETE a hero permanently.");
            System.out.printf("%-3s %-20s %-10s %-5s %-5s\n", "##", "NAME", "CLASS", "LEVEL", "EXP");

            // 📌 Kahramanları listele
            int index = 1;
            for (Hero hero : heroesList) {
                System.out.printf("%-3d %-20s %-10s %-5d %-5d\n",
                        index, hero.getName(), hero.getHeroClass(), hero.getLevel(), hero.getExperience());
                index++;
            }

            System.out.print("\nYour choice:> ");
            int choice = getValidIntInput(-heroesList.size(), heroesList.size());

            if (choice == 0) {
                return createNewHero();
            } else if (choice < 0) {
                deleteHero(-choice - 1);
            } else {
                selectedHero = confirmSelection(heroesList.get(choice - 1));
            }

        } while (selectedHero == null);

        return selectedHero;
    }

    private int getValidIntInput(int min, int max) {
        while (true) {
            if (!scanner.hasNextInt()) {
                System.out.print("❌ Invalid input. Please enter a number:> ");
                scanner.next();
                continue;
            }
            int input = scanner.nextInt();
            scanner.nextLine(); // Buffer temizle
            if (input >= min && input <= max) {
                return input;
            }
            System.out.printf("❌ Please enter a number between %d and %d:> ", min, max);
        }
    }

    private Hero confirmSelection(Hero hero) {
        System.out.println("\n📜 Selected Hero:");
        printHeroStats(hero);

        System.out.print("\n✅ Do you want to play with this hero? (Yes/No):> ");
        if (scanYesOrNo()) {
            return hero;
        }
        return null;
    }

    private void printHeroStats(Hero hero) {
        System.out.printf("Name: %-20s  Class: %-10s  Level: %-5d  EXP: %-5d\n",
                hero.getName(), hero.getHeroClass(), hero.getLevel(), hero.getExperience());
        System.out.println("HP: " + hero.getHitPoints() + "  Attack: " + hero.getAttack() + "  Defence: " + hero.getDefence());
    }

    private boolean scanYesOrNo() {
        while (true) {
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("y") || answer.equals("yes")) {
                return true;
            } else if (answer.equals("n") || answer.equals("no")) {
                return false;
            }
            System.out.print("❌ Invalid input. Please type 'Yes' or 'No':> ");
        }
    }

    private void deleteHero(int index) {
        if (index < 0 || index >= heroesList.size()) {
            System.out.println("❌ Invalid hero index!");
            return;
        }

        Hero heroToDelete = heroesList.get(index);
        heroManager.deleteHero(heroToDelete);
        System.out.printf("\n⚠️ Are you sure you want to DELETE '%s' forever? (Yes/No):> ", heroToDelete.getName());
        if (scanYesOrNo()) {
            heroesList.remove(index);
            System.out.println("🗑️ Hero has been removed successfully!");
        } else {
            System.out.println("✅ Deletion canceled.");
        }
    }

    private Hero createNewHero() {
        System.out.print("\n🛠️ Enter your new hero's name:> ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty() || name.length() > 20) {
            System.out.print("❌ Name must be 1-20 characters long. Try again:> ");
            name = scanner.nextLine().trim();
        }

        System.out.println("\n🎭 Choose a Hero Class:");
        Hero selectedHero = selectHeroClass(name);

        System.out.println("\n🎉 New Hero Created! Welcome, " + name + " the " + selectedHero.getClass().getSimpleName() + "!");
        return selectedHero;
    }

    private Hero selectHeroClass(String name) {
        while (true) {
            System.out.println("1. Rogue 🗡️ (Agile and sneaky, high evasion)");
            System.out.println("2. Warrior 🛡️ (Strong and durable, high defense)");
            System.out.println("3. Mage 🔥 (Powerful spells, high magic damage)");
            System.out.print("Choose a class by number:> ");

            int choice = getValidIntInput(1, 3);

            Hero chosenHero;
            switch (choice) {
                case 1 -> chosenHero = new Rogue(name);
                case 2 -> chosenHero = new Warrior(name);
                case 3 -> chosenHero = new Mage(name);
                default -> throw new IllegalStateException("Unexpected value: " + choice);
            }

            System.out.println("\n📜 Class Details:");
            printHeroStats(chosenHero);
            System.out.print("\n✅ Do you want to pick this class? (Yes/No):> ");

            if (scanYesOrNo()) {
                return chosenHero;
            }
        }
    }
}
