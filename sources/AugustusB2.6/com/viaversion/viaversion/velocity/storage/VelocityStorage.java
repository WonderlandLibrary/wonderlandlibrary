// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.velocity.storage;

import java.util.Objects;
import java.lang.reflect.InvocationTargetException;
import com.viaversion.viaversion.util.ReflectionUtil;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.List;
import com.velocitypowered.api.proxy.Player;
import com.viaversion.viaversion.api.connection.StorableObject;

public class VelocityStorage implements StorableObject
{
    private final Player player;
    private String currentServer;
    private List<UUID> cachedBossbar;
    private static Method getServerBossBars;
    private static Class<?> clientPlaySessionHandler;
    private static Method getMinecraftConnection;
    
    public VelocityStorage(final Player player) {
        this.player = player;
        this.currentServer = "";
    }
    
    public List<UUID> getBossbar() {
        if (this.cachedBossbar == null) {
            if (VelocityStorage.clientPlaySessionHandler == null) {
                return null;
            }
            if (VelocityStorage.getServerBossBars == null) {
                return null;
            }
            if (VelocityStorage.getMinecraftConnection == null) {
                return null;
            }
            try {
                final Object connection = VelocityStorage.getMinecraftConnection.invoke(this.player, new Object[0]);
                final Object sessionHandler = ReflectionUtil.invoke(connection, "getSessionHandler");
                if (VelocityStorage.clientPlaySessionHandler.isInstance(sessionHandler)) {
                    this.cachedBossbar = (List<UUID>)VelocityStorage.getServerBossBars.invoke(sessionHandler, new Object[0]);
                }
            }
            catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                e.printStackTrace();
            }
        }
        return this.cachedBossbar;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public String getCurrentServer() {
        return this.currentServer;
    }
    
    public void setCurrentServer(final String currentServer) {
        this.currentServer = currentServer;
    }
    
    public List<UUID> getCachedBossbar() {
        return this.cachedBossbar;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final VelocityStorage that = (VelocityStorage)o;
        return Objects.equals(this.player, that.player) && Objects.equals(this.currentServer, that.currentServer) && Objects.equals(this.cachedBossbar, that.cachedBossbar);
    }
    
    @Override
    public int hashCode() {
        int result = (this.player != null) ? this.player.hashCode() : 0;
        result = 31 * result + ((this.currentServer != null) ? this.currentServer.hashCode() : 0);
        result = 31 * result + ((this.cachedBossbar != null) ? this.cachedBossbar.hashCode() : 0);
        return result;
    }
    
    static {
        try {
            VelocityStorage.clientPlaySessionHandler = Class.forName("com.velocitypowered.proxy.connection.client.ClientPlaySessionHandler");
            VelocityStorage.getServerBossBars = VelocityStorage.clientPlaySessionHandler.getDeclaredMethod("getServerBossBars", (Class<?>[])new Class[0]);
            VelocityStorage.getMinecraftConnection = Class.forName("com.velocitypowered.proxy.connection.client.ConnectedPlayer").getDeclaredMethod("getMinecraftConnection", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException | ClassNotFoundException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            e.printStackTrace();
        }
    }
}
