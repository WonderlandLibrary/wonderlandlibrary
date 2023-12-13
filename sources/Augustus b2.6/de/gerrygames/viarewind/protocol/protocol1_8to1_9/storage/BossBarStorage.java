// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage;

import java.util.HashMap;
import com.viaversion.viaversion.api.connection.UserConnection;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.bossbar.WitherBossBar;
import java.util.UUID;
import java.util.Map;
import com.viaversion.viaversion.api.connection.StoredObject;

public class BossBarStorage extends StoredObject
{
    private Map<UUID, WitherBossBar> bossBars;
    
    public BossBarStorage(final UserConnection user) {
        super(user);
        this.bossBars = new HashMap<UUID, WitherBossBar>();
    }
    
    public void add(final UUID uuid, final String title, final float health) {
        final WitherBossBar bossBar = new WitherBossBar(this.getUser(), uuid, title, health);
        final PlayerPosition playerPosition = this.getUser().get(PlayerPosition.class);
        bossBar.setPlayerLocation(playerPosition.getPosX(), playerPosition.getPosY(), playerPosition.getPosZ(), playerPosition.getYaw(), playerPosition.getPitch());
        bossBar.show();
        this.bossBars.put(uuid, bossBar);
    }
    
    public void remove(final UUID uuid) {
        final WitherBossBar bossBar = this.bossBars.remove(uuid);
        if (bossBar == null) {
            return;
        }
        bossBar.hide();
    }
    
    public void updateLocation() {
        final PlayerPosition playerPosition = this.getUser().get(PlayerPosition.class);
        final PlayerPosition playerPosition2;
        this.bossBars.values().forEach(bossBar -> bossBar.setPlayerLocation(playerPosition2.getPosX(), playerPosition2.getPosY(), playerPosition2.getPosZ(), playerPosition2.getYaw(), playerPosition2.getPitch()));
    }
    
    public void changeWorld() {
        this.bossBars.values().forEach(bossBar -> {
            bossBar.hide();
            bossBar.show();
        });
    }
    
    public void updateHealth(final UUID uuid, final float health) {
        final WitherBossBar bossBar = this.bossBars.get(uuid);
        if (bossBar == null) {
            return;
        }
        bossBar.setHealth(health);
    }
    
    public void updateTitle(final UUID uuid, final String title) {
        final WitherBossBar bossBar = this.bossBars.get(uuid);
        if (bossBar == null) {
            return;
        }
        bossBar.setTitle(title);
    }
}
