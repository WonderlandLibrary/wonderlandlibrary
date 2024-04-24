package me.crimp.claudius.managers;

import me.crimp.claudius.mod.Feature;
import me.crimp.claudius.mod.setting.Setting;
import me.crimp.claudius.utils.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EnemyManager
        extends Feature {
    private List<Enemy> Enemys = new ArrayList<Enemy>();

    public EnemyManager() {
        super("Enemys");
    }

    public boolean isEnemy(String name) {
        this.cleanEnemys();
        return this.Enemys.stream().anyMatch(Enemy -> Enemy.username.equalsIgnoreCase(name));
    }

//    public boolean isEnemy(EntityPlayer player) {
//        return this.isEnemy(player.getName());
//    }

    public void addEnemy(String name) {
        Enemy Enemy = this.getEnemyByName(name);
        if (Enemy != null) {
            this.Enemys.add(Enemy);
        }
        this.cleanEnemys();
    }

    public void removeEnemy(String name) {
        this.cleanEnemys();
        for (Enemy Enemy : this.Enemys) {
            if (!Enemy.getUsername().equalsIgnoreCase(name)) continue;
            this.Enemys.remove(Enemy);
            break;
        }
    }

    public void onLoad() {
        this.Enemys = new ArrayList<Enemy>();
        this.clearSettings();
    }

    public void saveEnemys() {
        this.clearSettings();
        this.cleanEnemys();
        for (Enemy Enemy : this.Enemys) {
           register(new Setting<String>(Enemy.getUuid().toString(), Enemy.getUsername()));
        }
    }

    public void cleanEnemys() {
        this.Enemys.stream().filter(Objects::nonNull).filter(Enemy -> Enemy.getUsername() != null);
    }

    public List<Enemy> getEnemys() {
        this.cleanEnemys();
        return this.Enemys;
    }

    public Enemy getEnemyByName(String input) {
        UUID uuid = PlayerUtil.getUUIDFromName(input);
        if (uuid != null) {
            Enemy Enemy = new Enemy(input, uuid);
            return Enemy;
        }
        return null;
    }

    public void addEnemy(Enemy Enemy) {
        this.Enemys.add(Enemy);
    }

    public static class Enemy {
        private final String username;
        private final UUID uuid;

        public Enemy(String username, UUID uuid) {
            this.username = username;
            this.uuid = uuid;
        }

        public String getUsername() {
            return this.username;
        }

        public UUID getUuid() {
            return this.uuid;
        }
    }
}

