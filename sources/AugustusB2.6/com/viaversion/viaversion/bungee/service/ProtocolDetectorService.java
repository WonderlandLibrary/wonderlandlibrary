// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bungee.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.Callback;
import java.util.Iterator;
import net.md_5.bungee.api.config.ServerInfo;
import com.viaversion.viaversion.bungee.providers.BungeeVersionProvider;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.bungee.platform.BungeeViaConfig;
import com.viaversion.viaversion.BungeePlugin;
import java.util.Map;

public class ProtocolDetectorService implements Runnable
{
    private static final Map<String, Integer> detectedProtocolIds;
    private static ProtocolDetectorService instance;
    private final BungeePlugin plugin;
    
    public ProtocolDetectorService(final BungeePlugin plugin) {
        this.plugin = plugin;
        ProtocolDetectorService.instance = this;
    }
    
    public static Integer getProtocolId(final String serverName) {
        final Map<String, Integer> servers = ((BungeeViaConfig)Via.getConfig()).getBungeeServerProtocols();
        final Integer protocol = servers.get(serverName);
        if (protocol != null) {
            return protocol;
        }
        final Integer detectedProtocol = ProtocolDetectorService.detectedProtocolIds.get(serverName);
        if (detectedProtocol != null) {
            return detectedProtocol;
        }
        final Integer defaultProtocol = servers.get("default");
        if (defaultProtocol != null) {
            return defaultProtocol;
        }
        return BungeeVersionProvider.getLowestSupportedVersion();
    }
    
    @Override
    public void run() {
        for (final Map.Entry<String, ServerInfo> lists : this.plugin.getProxy().getServers().entrySet()) {
            probeServer(lists.getValue());
        }
    }
    
    public static void probeServer(final ServerInfo serverInfo) {
        final String key = serverInfo.getName();
        serverInfo.ping((Callback)new Callback<ServerPing>() {
            public void done(final ServerPing serverPing, final Throwable throwable) {
                if (throwable == null && serverPing != null && serverPing.getVersion() != null && serverPing.getVersion().getProtocol() > 0) {
                    ProtocolDetectorService.detectedProtocolIds.put(key, serverPing.getVersion().getProtocol());
                    if (((BungeeViaConfig)Via.getConfig()).isBungeePingSave()) {
                        final Map<String, Integer> servers = ((BungeeViaConfig)Via.getConfig()).getBungeeServerProtocols();
                        final Integer protocol = servers.get(key);
                        if (protocol != null && protocol == serverPing.getVersion().getProtocol()) {
                            return;
                        }
                        synchronized (Via.getPlatform().getConfigurationProvider()) {
                            servers.put(key, serverPing.getVersion().getProtocol());
                        }
                        Via.getPlatform().getConfigurationProvider().saveConfig();
                    }
                }
            }
        });
    }
    
    public static Map<String, Integer> getDetectedIds() {
        return new HashMap<String, Integer>(ProtocolDetectorService.detectedProtocolIds);
    }
    
    public static ProtocolDetectorService getInstance() {
        return ProtocolDetectorService.instance;
    }
    
    public BungeePlugin getPlugin() {
        return this.plugin;
    }
    
    static {
        detectedProtocolIds = new ConcurrentHashMap<String, Integer>();
    }
}
