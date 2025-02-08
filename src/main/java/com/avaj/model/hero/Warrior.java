package com.avaj.model.hero;

public class Warrior extends Hero {
    public Warrior(String name) {
        super(name, "Warrior", 15, 10, 100, 0, 0);
    }

    public Warrior(String name, int level, int experience, int attack, int defense, int hitPoints, int positionX, int positionY) {
        super(name, "Warrior", level, experience, attack, defense, hitPoints, positionX, positionY);
    }
}