// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.velocity.platform;

import java.util.Arrays;
import java.util.Iterator;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import java.util.Collection;
import java.util.HashSet;
import java.net.URL;
import java.util.HashMap;
import java.io.File;
import java.util.Map;
import java.util.List;
import com.viaversion.viaversion.configuration.AbstractViaConfig;

public class VelocityViaConfig extends AbstractViaConfig
{
    private static final List<String> UNSUPPORTED;
    private int velocityPingInterval;
    private boolean velocityPingSave;
    private Map<String, Integer> velocityServerProtocols;
    
    public VelocityViaConfig(final File configFile) {
        super(new File(configFile, "config.yml"));
        this.reloadConfig();
    }
    
    @Override
    protected void loadFields() {
        super.loadFields();
        this.velocityPingInterval = this.getInt("velocity-ping-interval", 60);
        this.velocityPingSave = this.getBoolean("velocity-ping-save", true);
        this.velocityServerProtocols = this.get("velocity-servers", (Class<HashMap<String, Integer>>)Map.class, new HashMap<String, Integer>());
    }
    
    @Override
    public URL getDefaultConfigURL() {
        return this.getClass().getClassLoader().getResource("assets/viaversion/config.yml");
    }
    
    @Override
    protected void handleConfig(final Map<String, Object> config) {
        Map<String, Object> servers;
        if (!(config.get("velocity-servers") instanceof Map)) {
            servers = new HashMap<String, Object>();
        }
        else {
            servers = config.get("velocity-servers");
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
            try {
                servers.put("default", VelocityViaInjector.getLowestSupportedProtocolVersion());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        config.put("velocity-servers", servers);
    }
    
    @Override
    public List<String> getUnsupportedOptions() {
        return VelocityViaConfig.UNSUPPORTED;
    }
    
    @Override
    public boolean isItemCache() {
        return false;
    }
    
    @Override
    public boolean isNMSPlayerTicking() {
        return false;
    }
    
    public int getVelocityPingInterval() {
        return this.velocityPingInterval;
    }
    
    public boolean isVelocityPingSave() {
        return this.velocityPingSave;
    }
    
    public Map<String, Integer> getVelocityServerProtocols() {
        return this.velocityServerProtocols;
    }
    
    static {
        UNSUPPORTED = Arrays.asList("nms-player-ticking", "item-cache", "anti-xray-patch", "quick-move-action-fix", "bungee-ping-interval", "bungee-ping-save", "bungee-servers", "blockconnection-method", "change-1_9-hitbox", "change-1_14-hitbox");
    }
}
