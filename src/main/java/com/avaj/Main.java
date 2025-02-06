package com.avaj;

import com.avaj.database.HeroManager;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String GUI_MODE = "gui";
    private static final String CONSOLE_MODE = "console";

    public static void main(String[] args) {
        HeroManager heroManager = new HeroManager();
        heroManager.createHeroesTable();

        boolean guiMode = parseArguments(args);

        if (guiMode) {
            System.out.println("Launching GUI mode...");
        } else {
            System.out.println("Launching Console mode...");
        }
    }

    private static boolean parseArguments(String[] args) {
        if (args.length != 1) {
            printUsage();
        }

        return switch (args[0].toLowerCase()) {
            case GUI_MODE -> true;
            case CONSOLE_MODE -> false;
            default -> {
                printUsage();
                throw new IllegalArgumentException("Invalid game mode: " + args[0]);
            }
        };
    }

    private static void printUsage() {
        System.out.println("Choose the game mode by providing an argument.");
        System.out.println("\t$ java -jar swingy-1.0-jar-with-dependencies.jar console");
        System.out.println("\t$ java -jar swingy-1.0-jar-with-dependencies.jar gui");
        System.exit(1);
    }
}