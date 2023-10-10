// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bungee.platform;

import java.util.Arrays;
import java.util.Iterator;
import com.viaversion.viaversion.bungee.providers.BungeeVersionProvider;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import java.util.Collection;
import java.util.HashSet;
import java.net.URL;
import java.util.HashMap;
import java.io.File;
import java.util.Map;
import java.util.List;
import com.viaversion.viaversion.configuration.AbstractViaConfig;

public class BungeeViaConfig extends AbstractViaConfig
{
    private static final List<String> UNSUPPORTED;
    private int bungeePingInterval;
    private boolean bungeePingSave;
    private Map<String, Integer> bungeeServerProtocols;
    
    public BungeeViaConfig(final File configFile) {
        super(new File(configFile, "config.yml"));
        this.reloadConfig();
    }
    
    @Override
    protected void loadFields() {
        super.loadFields();
        this.bungeePingInterval = this.getInt("bungee-ping-interval", 60);
        this.bungeePingSave = this.getBoolean("bungee-ping-save", true);
        this.bungeeServerProtocols = this.get("bungee-servers", (Class<HashMap<String, Integer>>)Map.class, new HashMap<String, Integer>());
    }
    
    @Override
    public URL getDefaultConfigURL() {
        return BungeeViaConfig.class.getClassLoader().getResource("assets/viaversion/config.yml");
    }
    
    @Override
    protected void handleConfig(final Map<String, Object> config) {
        Map<String, Object> servers;
        if (!(config.get("bungee-servers") instanceof Map)) {
            servers = new HashMap<String, Object>();
        }
        else {
            servers = config.get("bungee-servers");
        }
        for (final Map.Entry<String, Object> entry : new HashSet<Map.Entry<String, Object>>(servers.entrySet())) {
            if (!(entry.getValue() instanceof Integer)) {
                if (entry.getValue() instanceof String) {
                    final ProtocolVersion found = ProtocolVersion.getClosest(entry.getValue());
                    if (found != null) {
                        servers.put(entry.getKey(), found.getVersion());
                    }
                    else {
                        servers.remove(entry.getKey());
                    }
                }
                else {
                    servers.remove(entry.getKey());
                }
            }
        }
        if (!servers.containsKey("default")) {
            servers.put("default", BungeeVersionProvider.getLowestSupportedVersion());
        }
        config.put("bungee-servers", servers);
    }
    
    @Override
    public List<String> getUnsupportedOptions() {
        return BungeeViaConfig.UNSUPPORTED;
    }
    
    @Override
    public boolean isItemCache() {
        return false;
    }
    
    @Override
    public boolean isNMSPlayerTicking() {
        return false;
    }
    
    public int getBungeePingInterval() {
        return this.bungeePingInterval;
    }
    
    public boolean isBungeePingSave() {
        return this.bungeePingSave;
    }
    
    public Map<String, Integer> getBungeeServerProtocols() {
        return this.bungeeServerProtocols;
    }
    
    static {
        UNSUPPORTED = Arrays.asList("nms-player-ticking", "item-cache", "anti-xray-patch", "quick-move-action-fix", "velocity-ping-interval", "velocity-ping-save", "velocity-servers", "blockconnection-method", "change-1_9-hitbox", "change-1_14-hitbox");
    }
}
