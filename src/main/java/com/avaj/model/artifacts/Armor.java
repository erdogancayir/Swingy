package com.avaj.model.artifacts;

public class Armor extends AArtifact {

    public Armor(int lvl) {

        this.type = ArtifactType.ARMOR;

        namesAdr = new String[][] {
                {"Armour of the Brave", "src/main/resources/img/artifacts/armor/bikini.png"},
                {"Super-Pajama", "src/main/resources/img/artifacts/armor/pajama.png"},
                {"Towel", "src/main/resources/img/artifacts/armor/towel.jpg"},
                {"Old-School Toga", "src/main/resources/img/artifacts//armor/toga.png"},
                {"Life Jacket", "src/main/resources/img/artifacts//armor/life-jacket.png"},
                {"Namaz", "src/main/resources/img/artifacts//armor/namaz.png"},
                {"Space Suit", "src/main/resources/img/artifacts//armor/space-suit.png"},
                {"Bunker", "src/main/resources/img/artifacts//armor/bunker.png"},
                {"Your Mom", "src/main/resources/img/artifacts//armor/mother.png"},
                {"Life Insurance", "src/main/resources/img/artifacts//armor/insurance.png"},
        };

        this.name = namesAdr[lvl][0];
        this.power = lvl + 1;

        iconAddr = namesAdr[lvl][1];

    }

}
