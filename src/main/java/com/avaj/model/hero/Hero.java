package com.avaj.model.hero;

import com.avaj.model.artifact.Armor;
import com.avaj.model.artifact.Artifact;
import com.avaj.model.artifact.Helm;
import com.avaj.model.artifact.Weapon;

public abstract class Hero
{
    protected String name;
    protected String heroClass;
    protected int level;
    protected int experience;
    protected int attack;
    protected int defense;
    protected int hitPoints;

    public Hero(String name, String heroClass, int attack, int defense, int hitPoints) {
        this.name = name;
        this.heroClass = heroClass;
        this.level = 1;
        this.experience = 0;
        this.attack = attack;
        this.defense = defense;
        this.hitPoints = hitPoints;
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

    public void equipArtifact(Artifact artifact) {
        if (artifact instanceof Weapon weapon) {
           // this.attack += weapon.getBoost();
        } else if (artifact instanceof Armor) {
            //this.defense += artifact.getBoost();
        } else if (artifact instanceof Helm) {
            //this.hitPoints += artifact.getBoost();
        }
    }

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
