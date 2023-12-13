// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.sponge.platform;

import java.util.Arrays;
import java.util.Map;
import java.io.File;
import java.util.List;
import com.viaversion.viaversion.configuration.AbstractViaConfig;

public class SpongeViaConfig extends AbstractViaConfig
{
    private static final List<String> UNSUPPORTED;
    
    public SpongeViaConfig(final File configFile) {
        super(new File(configFile, "config.yml"));
        this.reloadConfig();
    }
    
    @Override
    protected void handleConfig(final Map<String, Object> config) {
    }
    
    @Override
    public List<String> getUnsupportedOptions() {
        return SpongeViaConfig.UNSUPPORTED;
    }
    
    static {
        UNSUPPORTED = Arrays.asList("anti-xray-patch", "bungee-ping-interval", "bungee-ping-save", "bungee-servers", "velocity-ping-interval", "velocity-ping-save", "velocity-servers", "quick-move-action-fix", "change-1_9-hitbox", "change-1_14-hitbox", "blockconnection-method");
    }
}
