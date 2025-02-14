package com.avaj.model.hero;

import com.avaj.model.GameGlobalInstance;

public class Mage extends Hero
{
    public Mage(String name) {
        super(name, "Mage", 5, 3, 50, 4, 4);
        SetAvatarPath();
    }

    public Mage(String name, String heroClass, int level, int experience, int attack, int defense, int hitPoints, int positionX, int positionY) {
        super(name, heroClass, level, experience, attack, defense, hitPoints, positionX, positionY);
        SetAvatarPath();
    }

    public void SetAvatarPath() {
        super.SetAvatarPath(GameGlobalInstance.HERO_ICON_PATH);
    }
}
