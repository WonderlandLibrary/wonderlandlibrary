/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.people;

import java.util.ArrayList;
import net.minecraft.util.StringUtils;

public class EnemyManager {
    public static ArrayList<Enemy> Enemies = new ArrayList();

    public static void addEnemy(String name) {
        Enemies.add(new Enemy(name));
    }

    public static void deleteEnemy(String name) {
        for (Enemy Enemy2 : Enemies) {
            if (!Enemy2.getName().equalsIgnoreCase(name)) continue;
            Enemies.remove(Enemy2);
            break;
        }
    }

    public static boolean isEnemy(String name) {
        boolean isEnemy = false;
        for (Enemy Enemy2 : Enemies) {
            if (!Enemy2.getName().equalsIgnoreCase(StringUtils.stripControlCodes(name))) continue;
            isEnemy = true;
            break;
        }
        return isEnemy;
    }

    public static class Enemy {
        private String name;

        public Enemy(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String s) {
            this.name = s;
        }
    }

}

