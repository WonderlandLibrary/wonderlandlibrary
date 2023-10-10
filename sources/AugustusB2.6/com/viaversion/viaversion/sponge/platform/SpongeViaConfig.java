// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.sponge.platform;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import org.spongepowered.api.asset.Asset;
import java.net.URL;
import java.io.File;
import org.spongepowered.api.plugin.PluginContainer;
import java.util.List;
import com.viaversion.viaversion.configuration.AbstractViaConfig;

public class SpongeViaConfig extends AbstractViaConfig
{
    private static final List<String> UNSUPPORTED;
    private final PluginContainer pluginContainer;
    
    public SpongeViaConfig(final PluginContainer pluginContainer, final File configFile) {
        super(new File(configFile, "config.yml"));
        this.pluginContainer = pluginContainer;
        this.reloadConfig();
    }
    
    @Override
    public URL getDefaultConfigURL() {
        final Optional<Asset> config = (Optional<Asset>)this.pluginContainer.getAsset("config.yml");
        if (!config.isPresent()) {
            throw new IllegalArgumentException("Default config is missing from jar");
        }
        return config.get().getUrl();
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
