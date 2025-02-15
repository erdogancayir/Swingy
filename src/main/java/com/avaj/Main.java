package com.avaj;

import com.avaj.controller.GameController;
import com.avaj.controller.HeroSpawnController;
import com.avaj.controller.ValidationController;
import com.avaj.database.HeroManager;
import com.avaj.model.hero.Hero;
import com.avaj.model.map.Map;

import static com.avaj.model.GameGlobalInstance.CONSOLE_MODE;
import static com.avaj.model.GameGlobalInstance.GUI_MODE;

public class Main {
    public static void main(String[] args) {
        boolean guiMode = parseArguments(args);
        var heroManager = new HeroManager();
        var heroSpawnController = new HeroSpawnController(heroManager, guiMode);

        Hero selectedHero = heroSpawnController.OpenSpawnHeroGui();

        Map gameMap = new Map(selectedHero);

        ValidationController validationController = new ValidationController();

        GameController gameController = new GameController(gameMap, selectedHero, heroManager, validationController, guiMode);

        gameController.StartGame();
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