package com.avaj.model.hero;

import com.avaj.model.artifact.Armor;
import com.avaj.model.artifact.Artifact;
import com.avaj.model.artifact.Helm;
import com.avaj.model.artifact.Weapon;
import com.avaj.model.map.Direction;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public abstract class Hero
{
    private String avatarPath;

    @NotNull(message = "Hero name cannot be null.")
    @Size(min = 3, max = 20, message = "Hero name must be between 3 and 20 characters.")
    protected String name;

    @NotNull(message = "Hero class cannot be null.")
    protected String heroClass;

    @Min(value = 1, message = "Level must be at least 1.")
    protected int level;

    @Min(value = 0, message = "Experience cannot be negative.")
    protected int experience;

    @Min(value = 1, message = "Attack value must be at least 1.")
    protected int attack;

    @Min(value = 1, message = "Defense value must be at least 1.")
    protected int defense;

    @Min(value = 10, message = "Hit points must be at least 10.")
    protected int hitPoints;

    protected int bonusAttack;
    protected int bonusDefense;
    protected int bonusHitPoints;

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
            this.bonusAttack += weapon.getBoost();
        } else if (artifact instanceof Armor) {
            this.bonusDefense += artifact.getBoost();
        } else if (artifact instanceof Helm) {
            this.bonusHitPoints += artifact.getBoost();
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
        return attack + bonusAttack;
    }

    public int getDefense() {
        return defense + bonusDefense;
    }

    public int getHitPoints() {
        return hitPoints + bonusHitPoints;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void SetLevel(int level) {
        this.level = level;
    }

    public void SetHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
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

    public void Heal() {
        hitPoints = hitPoints;
    }
}
