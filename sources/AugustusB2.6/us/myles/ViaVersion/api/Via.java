// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api;

import java.util.SortedSet;
import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.boss.BossStyle;
import us.myles.ViaVersion.api.boss.BossColor;
import io.netty.buffer.ByteBuf;
import java.util.UUID;

@Deprecated
public class Via<T> implements ViaAPI<T>
{
    private static final ViaAPI INSTANCE;
    
    private Via() {
    }
    
    @Deprecated
    public static ViaAPI getAPI() {
        return Via.INSTANCE;
    }
    
    @Override
    public int getPlayerVersion(final T player) {
        return com.viaversion.viaversion.api.Via.getAPI().getPlayerVersion(player);
    }
    
    @Override
    public int getPlayerVersion(final UUID uuid) {
        return com.viaversion.viaversion.api.Via.getAPI().getPlayerVersion(uuid);
    }
    
    @Override
    public boolean isInjected(final UUID playerUUID) {
        return com.viaversion.viaversion.api.Via.getAPI().isInjected(playerUUID);
    }
    
    @Override
    public String getVersion() {
        return com.viaversion.viaversion.api.Via.getAPI().getVersion();
    }
    
    @Override
    public void sendRawPacket(final T player, final ByteBuf packet) {
        com.viaversion.viaversion.api.Via.getAPI().sendRawPacket(player, packet);
    }
    
    @Override
    public void sendRawPacket(final UUID uuid, final ByteBuf packet) {
        com.viaversion.viaversion.api.Via.getAPI().sendRawPacket(uuid, packet);
    }
    
    @Override
    public BossBar createBossBar(final String title, final BossColor color, final BossStyle style) {
        return new BossBar(com.viaversion.viaversion.api.Via.getAPI().legacyAPI().createLegacyBossBar(title, com.viaversion.viaversion.api.legacy.bossbar.BossColor.values()[color.ordinal()], com.viaversion.viaversion.api.legacy.bossbar.BossStyle.values()[style.ordinal()]));
    }
    
    @Override
    public BossBar createBossBar(final String title, final float health, final BossColor color, final BossStyle style) {
        return new BossBar(com.viaversion.viaversion.api.Via.getAPI().legacyAPI().createLegacyBossBar(title, health, com.viaversion.viaversion.api.legacy.bossbar.BossColor.values()[color.ordinal()], com.viaversion.viaversion.api.legacy.bossbar.BossStyle.values()[style.ordinal()]));
    }
    
    @Override
    public SortedSet<Integer> getSupportedVersions() {
        return (SortedSet<Integer>)com.viaversion.viaversion.api.Via.getAPI().getSupportedVersions();
    }
    
    @Override
    public SortedSet<Integer> getFullSupportedVersions() {
        return (SortedSet<Integer>)com.viaversion.viaversion.api.Via.getAPI().getFullSupportedVersions();
    }
    
    static {
        INSTANCE = new Via();
    }
}
