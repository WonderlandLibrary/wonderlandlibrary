// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards;

import java.io.File;
import org.spongepowered.api.event.Listener;
import com.viaversion.viaversion.api.Via;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import com.viaversion.viaversion.sponge.util.LoggerWrapper;
import org.spongepowered.api.config.ConfigDir;
import com.google.inject.Inject;
import java.nio.file.Path;
import java.util.logging.Logger;
import org.spongepowered.plugin.builtin.jvm.Plugin;
import com.viaversion.viabackwards.api.ViaBackwardsPlatform;

@Plugin("viabackwards")
public class SpongePlugin implements ViaBackwardsPlatform
{
    private final Logger logger;
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configPath;
    
    @Inject
    SpongePlugin(final org.apache.logging.log4j.Logger logger) {
        this.logger = new LoggerWrapper(logger);
    }
    
    @Listener
    public void constructPlugin(final ConstructPluginEvent event) {
        Via.getManager().addEnableListener(() -> this.init(this.getDataFolder()));
    }
    
    @Override
    public void disable() {
    }
    
    @Override
    public File getDataFolder() {
        return this.configPath.toFile();
    }
    
    @Override
    public Logger getLogger() {
        return this.logger;
    }
}
