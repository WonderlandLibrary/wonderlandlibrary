// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bungee.service;

import net.md_5.bungee.api.ServerPing;
import com.viaversion.viaversion.bungee.providers.BungeeVersionProvider;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.bungee.platform.BungeeViaConfig;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import com.viaversion.viaversion.platform.AbstractProtocolDetectorService;

public final class ProtocolDetectorService extends AbstractProtocolDetectorService
{
    public void probeServer(final ServerInfo serverInfo) {
        final String serverName = serverInfo.getName();
        serverInfo.ping((serverPing, throwable) -> {
            if (throwable != null || serverPing == null || serverPing.getVersion() == null || serverPing.getVersion().getProtocol() <= 0) {
                return;
            }
            final int oldProtocolVersion = this.serverProtocolVersion(serverName);
            if (oldProtocolVersion == serverPing.getVersion().getProtocol()) {
                return;
            }
            this.setProtocolVersion(serverName, serverPing.getVersion().getProtocol());
            if (((BungeeViaConfig)Via.getConfig()).isBungeePingSave()) {
                final Map<String, Integer> servers = ((BungeeViaConfig)Via.getConfig()).getBungeeServerProtocols();
                final Integer protocol = servers.get(serverName);
                if (protocol != null && protocol == serverPing.getVersion().getProtocol()) {
                    return;
                }
                synchronized (Via.getPlatform().getConfigurationProvider()) {
                    servers.put(serverName, serverPing.getVersion().getProtocol());
                }
                Via.getPlatform().getConfigurationProvider().saveConfig();
            }
        });
    }
    
    @Override
    public void probeAllServers() {
        final Collection<ServerInfo> servers = ProxyServer.getInstance().getServers().values();
        final Set<String> serverNames = new HashSet<String>(servers.size());
        for (final ServerInfo serverInfo : servers) {
            this.probeServer(serverInfo);
            serverNames.add(serverInfo.getName());
        }
        this.lock.writeLock().lock();
        try {
            this.detectedProtocolIds.keySet().retainAll(serverNames);
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    @Override
    protected Map<String, Integer> configuredServers() {
        return ((BungeeViaConfig)Via.getConfig()).getBungeeServerProtocols();
    }
    
    @Override
    protected int lowestSupportedProtocolVersion() {
        return BungeeVersionProvider.getLowestSupportedVersion();
    }
}
