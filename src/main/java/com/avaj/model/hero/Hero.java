package com.avaj.model.hero;

import com.avaj.model.artifact.Armor;
import com.avaj.model.artifact.Artifact;
import com.avaj.model.artifact.Helm;
import com.avaj.model.artifact.Weapon;
import com.avaj.model.map.Direction;

public abstract class Hero
{
    private String avatarPath;
    protected String name;
    protected String heroClass;
    protected int level;
    protected int experience;
    protected int attack;
    protected int defense;
    protected int hitPoints;
    protected int x;
    protected int y;

    public Hero(String name, String heroClass, int attack, int defense, int hitPoints, int x, int y) {
        this.name = name;
        this.heroClass = heroClass;
        this.level = 1;
        this.experience = 0;
        this.attack = attack;
        this.defense = defense;
        this.hitPoints = hitPoints;
    }

    public Hero(String name, String heroClass, int level, int experience, int attack, int defense, int hitPoints, int x, int y) {
        this.name = name;
        this.heroClass = heroClass;
        this.level = level;
        this.experience = experience;
        this.attack = attack;
        this.defense = defense;
        this.hitPoints = hitPoints;
        this.x = x;
        this.y = y;
    }

    public void gainExperience(int xp) {
        this.experience += xp;
        int requiredXP = level * 1000 + (level - 1) * (level - 1) * 450;
        if (this.experience >= requiredXP) {
            levelUp();
        }
    }

    private void levelUp() {
        this.level++;
        this.attack += 5;
        this.defense += 5;
        this.hitPoints += 10;
        System.out.println(name + " leveled up to " + level + "!");
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int[] getNewPosition(Direction direction) {
        return switch (direction) {
            case NORTH -> new int[]{x - 1, y};
            case SOUTH -> new int[]{x + 1, y};
            case EAST -> new int[]{x, y + 1};
            case WEST -> new int[]{x, y - 1};
        };
    }

    public void equipArtifact(Artifact artifact) {
        if (artifact instanceof Weapon weapon) {
            this.attack += weapon.getBoost();
        } else if (artifact instanceof Armor) {
            this.defense += artifact.getBoost();
        } else if (artifact instanceof Helm) {
            this.hitPoints += artifact.getBoost();
        }
    }

    public void SetAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getName() {
        return name;
    }

    public String getHeroClass() {
        return heroClass;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public String toString() {
        return "Hero{" +
                "name='" + name + '\'' +
                ", class='" + heroClass + '\'' +
                ", level=" + level +
                ", experience=" + experience +
                ", attack=" + attack +
                ", defense=" + defense +
                ", hitPoints=" + hitPoints +
                '}';
    }
}
