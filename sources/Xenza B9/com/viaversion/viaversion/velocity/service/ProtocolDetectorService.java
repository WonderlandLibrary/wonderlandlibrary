// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.velocity.service;

import com.velocitypowered.api.proxy.server.ServerPing;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import java.util.Map;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.velocity.platform.VelocityViaConfig;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import java.util.HashSet;
import com.viaversion.viaversion.VelocityPlugin;
import com.viaversion.viaversion.platform.AbstractProtocolDetectorService;

public final class ProtocolDetectorService extends AbstractProtocolDetectorService
{
    @Override
    public void probeAllServers() {
        final Collection<RegisteredServer> servers = VelocityPlugin.PROXY.getAllServers();
        final Set<String> serverNames = new HashSet<String>(servers.size());
        for (final RegisteredServer server : servers) {
            this.probeServer(server);
            serverNames.add(server.getServerInfo().getName());
        }
        this.lock.writeLock().lock();
        try {
            this.detectedProtocolIds.keySet().retainAll(serverNames);
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    public void probeServer(final RegisteredServer server) {
        final String serverName = server.getServerInfo().getName();
        server.ping().thenAccept(serverPing -> {
            if (serverPing != null && serverPing.getVersion() != null) {
                final int oldProtocolVersion = this.serverProtocolVersion(serverName);
                if (oldProtocolVersion == -1 || oldProtocolVersion != serverPing.getVersion().getProtocol()) {
                    this.setProtocolVersion(serverName, serverPing.getVersion().getProtocol());
                    if (((VelocityViaConfig)Via.getConfig()).isVelocityPingSave()) {
                        final Map<String, Integer> servers = this.configuredServers();
                        final Integer protocol = servers.get(serverName);
                        if (protocol == null || protocol != serverPing.getVersion().getProtocol()) {
                            synchronized (Via.getPlatform().getConfigurationProvider()) {
                                servers.put(serverName, serverPing.getVersion().getProtocol());
                            }
                            Via.getPlatform().getConfigurationProvider().saveConfig();
                        }
                    }
                }
            }
        });
    }
    
    @Override
    protected Map<String, Integer> configuredServers() {
        return ((VelocityViaConfig)Via.getConfig()).getVelocityServerProtocols();
    }
    
    @Override
    protected int lowestSupportedProtocolVersion() {
        try {
            return ProtocolVersion.getProtocol(Via.getManager().getInjector().getServerProtocolVersion()).getVersion();
        }
        catch (final Exception e) {
            e.printStackTrace();
            return ProtocolVersion.v1_8.getVersion();
        }
    }
}
