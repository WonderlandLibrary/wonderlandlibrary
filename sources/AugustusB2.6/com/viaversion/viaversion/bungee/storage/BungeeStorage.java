// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bungee.storage;

import java.util.Objects;
import java.util.UUID;
import java.util.Set;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import java.lang.reflect.Field;
import com.viaversion.viaversion.api.connection.StorableObject;

public class BungeeStorage implements StorableObject
{
    private static Field bossField;
    private final ProxiedPlayer player;
    private String currentServer;
    private Set<UUID> bossbar;
    
    public BungeeStorage(final ProxiedPlayer player) {
        this.player = player;
        this.currentServer = "";
        if (BungeeStorage.bossField != null) {
            try {
                this.bossbar = (Set<UUID>)BungeeStorage.bossField.get(player);
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    
    public ProxiedPlayer getPlayer() {
        return this.player;
    }
    
    public String getCurrentServer() {
        return this.currentServer;
    }
    
    public void setCurrentServer(final String currentServer) {
        this.currentServer = currentServer;
    }
    
    public Set<UUID> getBossbar() {
        return this.bossbar;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BungeeStorage that = (BungeeStorage)o;
        return Objects.equals(this.player, that.player) && Objects.equals(this.currentServer, that.currentServer) && Objects.equals(this.bossbar, that.bossbar);
    }
    
    @Override
    public int hashCode() {
        int result = (this.player != null) ? this.player.hashCode() : 0;
        result = 31 * result + ((this.currentServer != null) ? this.currentServer.hashCode() : 0);
        result = 31 * result + ((this.bossbar != null) ? this.bossbar.hashCode() : 0);
        return result;
    }
    
    static {
        try {
            final Class<?> user = Class.forName("net.md_5.bungee.UserConnection");
            (BungeeStorage.bossField = user.getDeclaredField("sentBossBars")).setAccessible(true);
        }
        catch (ClassNotFoundException ex) {}
        catch (NoSuchFieldException ex2) {}
    }
}
