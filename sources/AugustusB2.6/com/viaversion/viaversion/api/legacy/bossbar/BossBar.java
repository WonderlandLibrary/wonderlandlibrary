// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.legacy.bossbar;

import java.util.Set;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.UUID;

public interface BossBar
{
    String getTitle();
    
    BossBar setTitle(final String p0);
    
    float getHealth();
    
    BossBar setHealth(final float p0);
    
    BossColor getColor();
    
    BossBar setColor(final BossColor p0);
    
    BossStyle getStyle();
    
    BossBar setStyle(final BossStyle p0);
    
    BossBar addPlayer(final UUID p0);
    
    BossBar addConnection(final UserConnection p0);
    
    BossBar removePlayer(final UUID p0);
    
    BossBar removeConnection(final UserConnection p0);
    
    BossBar addFlag(final BossFlag p0);
    
    BossBar removeFlag(final BossFlag p0);
    
    boolean hasFlag(final BossFlag p0);
    
    Set<UUID> getPlayers();
    
    Set<UserConnection> getConnections();
    
    BossBar show();
    
    BossBar hide();
    
    boolean isVisible();
    
    UUID getId();
}
