package me.dev.legacy.manager;

import io.netty.util.internal.ConcurrentSet;
import me.dev.legacy.Legacy;
import me.dev.legacy.util.Enemy;

public class Enemies extends RotationManager {
    private static ConcurrentSet<Enemy> enemies = new ConcurrentSet<>();
    public static void addEnemy(String name){
        enemies.add(new Enemy(name));
    }
    public static void delEnemy(String name) {
        enemies.remove(getEnemyByName(name));
    }
    public static Enemy getEnemyByName(String name) {
        for (Enemy e : enemies) {
            if (Legacy.enemy.username.equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
    public static ConcurrentSet<Enemy> getEnemies() {
        return enemies;
    }
    public static boolean isEnemy(String name) {
        return enemies.stream().anyMatch(enemy -> enemy.username.equalsIgnoreCase(name));
    }

}