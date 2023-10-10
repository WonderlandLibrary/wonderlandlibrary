// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.connection;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ConnectionManager
{
    boolean isClientConnected(final UUID p0);
    
    default boolean isFrontEnd(final UserConnection connection) {
        return !connection.isClientSide();
    }
    
    UserConnection getConnectedClient(final UUID p0);
    
    UUID getConnectedClientId(final UserConnection p0);
    
    Set<UserConnection> getConnections();
    
    Map<UUID, UserConnection> getConnectedClients();
    
    void onLoginSuccess(final UserConnection p0);
    
    void onDisconnect(final UserConnection p0);
}
