// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.connection;

import io.netty.channel.ChannelFuture;
import io.netty.channel.Channel;
import com.viaversion.viaversion.api.Via;
import java.util.Objects;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.UUID;
import java.util.Map;
import com.viaversion.viaversion.api.connection.ConnectionManager;

public class ConnectionManagerImpl implements ConnectionManager
{
    protected final Map<UUID, UserConnection> clients;
    protected final Set<UserConnection> connections;
    
    public ConnectionManagerImpl() {
        this.clients = new ConcurrentHashMap<UUID, UserConnection>();
        this.connections = Collections.newSetFromMap(new ConcurrentHashMap<UserConnection, Boolean>());
    }
    
    @Override
    public void onLoginSuccess(final UserConnection connection) {
        Objects.requireNonNull(connection, "connection is null!");
        final Channel channel = connection.getChannel();
        if (channel != null && !channel.isOpen()) {
            return;
        }
        final boolean newlyAdded = this.connections.add(connection);
        if (this.isFrontEnd(connection)) {
            final UUID id = connection.getProtocolInfo().getUuid();
            final UserConnection previous = this.clients.put(id, connection);
            if (previous != null && previous != connection) {
                Via.getPlatform().getLogger().warning("Duplicate UUID on frontend connection! (" + id + ")");
            }
        }
        if (channel != null) {
            if (!channel.isOpen()) {
                this.onDisconnect(connection);
            }
            else if (newlyAdded) {
                channel.closeFuture().addListener(future -> this.onDisconnect(connection));
            }
        }
    }
    
    @Override
    public void onDisconnect(final UserConnection connection) {
        Objects.requireNonNull(connection, "connection is null!");
        this.connections.remove(connection);
        if (this.isFrontEnd(connection)) {
            final UUID id = connection.getProtocolInfo().getUuid();
            this.clients.remove(id);
        }
    }
    
    @Override
    public Map<UUID, UserConnection> getConnectedClients() {
        return Collections.unmodifiableMap((Map<? extends UUID, ? extends UserConnection>)this.clients);
    }
    
    @Override
    public UserConnection getConnectedClient(final UUID clientIdentifier) {
        return this.clients.get(clientIdentifier);
    }
    
    @Override
    public UUID getConnectedClientId(final UserConnection connection) {
        if (connection.getProtocolInfo() == null) {
            return null;
        }
        final UUID uuid = connection.getProtocolInfo().getUuid();
        final UserConnection client = this.clients.get(uuid);
        if (connection.equals(client)) {
            return uuid;
        }
        return null;
    }
    
    @Override
    public Set<UserConnection> getConnections() {
        return Collections.unmodifiableSet((Set<? extends UserConnection>)this.connections);
    }
    
    @Override
    public boolean isClientConnected(final UUID playerId) {
        return this.clients.containsKey(playerId);
    }
}
