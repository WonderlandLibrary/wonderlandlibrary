// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api;

import com.viaversion.viaversion.api.legacy.LegacyViaAPI;
import java.util.SortedSet;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.UUID;
import com.viaversion.viaversion.api.protocol.version.ServerProtocolVersion;

public interface ViaAPI<T>
{
    default int majorVersion() {
        return 4;
    }
    
    default int apiVersion() {
        return 10;
    }
    
    ServerProtocolVersion getServerVersion();
    
    int getPlayerVersion(final T p0);
    
    int getPlayerVersion(final UUID p0);
    
    boolean isInjected(final UUID p0);
    
    UserConnection getConnection(final UUID p0);
    
    String getVersion();
    
    void sendRawPacket(final T p0, final ByteBuf p1);
    
    void sendRawPacket(final UUID p0, final ByteBuf p1);
    
    SortedSet<Integer> getSupportedVersions();
    
    SortedSet<Integer> getFullSupportedVersions();
    
    LegacyViaAPI<T> legacyAPI();
}
