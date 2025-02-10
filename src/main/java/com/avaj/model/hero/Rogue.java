package com.avaj.model.hero;

import com.avaj.model.GameGlobalInstance;

public class Rogue extends Hero {
    public Rogue(String name) {
        super(name, "Rogue", 10, 5, 80, 0, 0);
        SetAvatarPath();
    }

    public Rogue(String name, int level, int experience, int attack, int defense, int hitPoints, int positionX, int positionY) {
        super(name, "Rogue", level, experience, attack, defense, hitPoints, positionX, positionY);
        SetAvatarPath();
    }

    public void SetAvatarPath() {
        super.SetAvatarPath(GameGlobalInstance.HERO2_ICON_PATH);
    }
}
