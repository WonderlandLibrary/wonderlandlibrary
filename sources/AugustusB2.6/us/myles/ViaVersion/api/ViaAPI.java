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
public interface ViaAPI<T>
{
    int getPlayerVersion(final T p0);
    
    int getPlayerVersion(final UUID p0);
    
    default boolean isPorted(final UUID playerUUID) {
        return this.isInjected(playerUUID);
    }
    
    boolean isInjected(final UUID p0);
    
    String getVersion();
    
    void sendRawPacket(final T p0, final ByteBuf p1);
    
    void sendRawPacket(final UUID p0, final ByteBuf p1);
    
    BossBar createBossBar(final String p0, final BossColor p1, final BossStyle p2);
    
    BossBar createBossBar(final String p0, final float p1, final BossColor p2, final BossStyle p3);
    
    SortedSet<Integer> getSupportedVersions();
    
    SortedSet<Integer> getFullSupportedVersions();
}
