package com.avaj.model.enemy;

public class Enemy
{
    protected String avatarPath;

    protected String name;
    protected String heroClass;
    protected int strength;
    protected int attack;
    protected int defense;
    protected int hitPoints;

    protected int x;
    protected int y;

    public Enemy()
    {
    }

    public int getMaxHp() {
        return 0;
    }

    public int getHitPoint() {
        return hitPoints;
    }

    public String getClas() {
        return heroClass;
    }

    public void takeDamage(int heroDamage) {
        hitPoints -= heroDamage;
    }

    public int getDefence() {
        return defense;
    }

    public int getAttack() {
        return attack;
    }

    public int getStrength() {
        return strength;
    }

    public String getAvatarPath() {
        return avatarPath;
    }
}
