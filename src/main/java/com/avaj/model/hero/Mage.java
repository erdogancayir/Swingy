package com.avaj.model.hero;

public class Mage extends Hero
{
    public Mage(String name) {
        super(name, "Mage", 5, 3, 50, 0, 0);
        SetAvatarPath();
    }

    public Mage(String name, String heroClass, int level, int experience, int attack, int defense, int hitPoints, int positionX, int positionY) {
        super(name, heroClass, level, experience, attack, defense, hitPoints, positionX, positionY);
        SetAvatarPath();
    }

    public void SetAvatarPath() {
        super.SetAvatarPath("./src/main/resources/assets/avatars/Mage.png");
    }
}
