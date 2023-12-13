// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind;

import org.spongepowered.api.event.lifecycle.RefreshGameEvent;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.Listener;
import de.gerrygames.viarewind.api.ViaRewindConfig;
import com.viaversion.viaversion.sponge.util.LoggerWrapper;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import de.gerrygames.viarewind.api.ViaRewindConfigImpl;
import org.spongepowered.api.config.ConfigDir;
import java.nio.file.Path;
import com.google.inject.Inject;
import java.util.logging.Logger;
import org.spongepowered.plugin.builtin.jvm.Plugin;
import de.gerrygames.viarewind.api.ViaRewindPlatform;

@Plugin("viarewind")
public class SpongePlugin implements ViaRewindPlatform
{
    private Logger logger;
    @Inject
    private org.apache.logging.log4j.Logger loggerSlf4j;
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;
    private ViaRewindConfigImpl conf;
    
    @Listener(order = Order.LATE)
    public void loadPlugin(final ConstructPluginEvent e) {
        this.logger = new LoggerWrapper(this.loggerSlf4j);
        (this.conf = new ViaRewindConfigImpl(this.configDir.resolve("config.yml").toFile())).reloadConfig();
        this.init(this.conf);
    }
    
    @Listener
    public void reload(final RefreshGameEvent e) {
        this.conf.reloadConfig();
    }
    
    @Override
    public Logger getLogger() {
        return this.logger;
    }
}
