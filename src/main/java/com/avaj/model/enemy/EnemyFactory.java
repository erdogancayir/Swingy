package com.avaj.model.enemy;

import com.avaj.model.hero.Hero;
import java.util.Random;

public class EnemyFactory {
    private static final Random random = new Random();

    public static Enemy createEnemyForHero(Hero hero, String avatarPath, int tier, String heroClass) {
        Enemy enemy = new Enemy();

        int heroLevel = hero.getLevel();
        enemy.heroClass = heroClass;
        enemy.avatarPath = avatarPath;

        // Düşmanın seviyesi kahramana göre belirleniyor
        if (tier < 3) {
            // Düşük Tier düşmanlar (Hero için kolay rakip)
            enemy.strength = Math.max(1, heroLevel - random.nextInt(2)); // Kahramanın seviyesinden 0 veya 1 düşük olabilir
            enemy.attack = Math.max(1, hero.getAttack() / 2);
            enemy.defense = Math.max(1, hero.getDefense() / 2);
            enemy.hitPoints = Math.max(10, hero.getHitPoints() / 2);
        } else if (tier == 3) {
            // Dengeli rakip
            enemy.strength = heroLevel;
            enemy.attack = hero.getAttack();
            enemy.defense = hero.getDefense();
            enemy.hitPoints = hero.getHitPoints();
        } else {
            // Yüksek Tier düşmanlar (Hero için zor rakip, şans faktörü devreye girer)
            enemy.strength = heroLevel + random.nextInt(3) + 1; // Kahramandan 1-3 seviye daha güçlü olabilir
            enemy.attack = (int) (hero.getAttack() * (1.2 + random.nextDouble())); // %20-50 daha güçlü olabilir
            enemy.defense = (int) (hero.getDefense() * (1.2 + random.nextDouble()));
            enemy.hitPoints = (int) (hero.getHitPoints() * (1.2 + random.nextDouble()));
        }

        return enemy;
    }
}
