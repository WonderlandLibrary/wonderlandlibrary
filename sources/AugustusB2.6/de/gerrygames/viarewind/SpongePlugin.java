// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind;

import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.Listener;
import de.gerrygames.viarewind.api.ViaRewindConfig;
import de.gerrygames.viarewind.api.ViaRewindConfigImpl;
import com.viaversion.viaversion.sponge.util.LoggerWrapper;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.config.ConfigDir;
import java.nio.file.Path;
import com.google.inject.Inject;
import java.util.logging.Logger;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import de.gerrygames.viarewind.api.ViaRewindPlatform;

@Plugin(id = "viarewind", name = "ViaRewind", version = "2.0.2-SNAPSHOT", authors = { "Gerrygames" }, dependencies = { @Dependency(id = "viaversion"), @Dependency(id = "viabackwards", optional = true) }, url = "https://viaversion.com/rewind")
public class SpongePlugin implements ViaRewindPlatform
{
    private Logger logger;
    @Inject
    private org.slf4j.Logger loggerSlf4j;
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;
    
    @Listener(order = Order.LATE)
    public void onGameStart(final GameInitializationEvent e) {
        this.logger = new LoggerWrapper(this.loggerSlf4j);
        final ViaRewindConfigImpl conf = new ViaRewindConfigImpl(this.configDir.resolve("config.yml").toFile());
        conf.reloadConfig();
        this.init(conf);
    }
    
    @Override
    public Logger getLogger() {
        return this.logger;
    }
}
