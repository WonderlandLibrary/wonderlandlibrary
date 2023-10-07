// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind;

import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import de.gerrygames.viarewind.api.ViaRewindConfig;
import com.viaversion.viaversion.velocity.util.LoggerWrapper;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import de.gerrygames.viarewind.api.ViaRewindConfigImpl;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import java.nio.file.Path;
import com.google.inject.Inject;
import java.util.logging.Logger;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import de.gerrygames.viarewind.api.ViaRewindPlatform;

@Plugin(id = "viarewind", name = "ViaRewind", version = "2.0.3-SNAPSHOT", authors = { "Gerrygames" }, dependencies = { @Dependency(id = "viaversion"), @Dependency(id = "viabackwards", optional = true) }, url = "https://viaversion.com/rewind")
public class VelocityPlugin implements ViaRewindPlatform
{
    private Logger logger;
    @Inject
    private org.slf4j.Logger loggerSlf4j;
    @Inject
    @DataDirectory
    private Path configDir;
    private ViaRewindConfigImpl conf;
    
    @Subscribe(order = PostOrder.LATE)
    public void onProxyStart(final ProxyInitializeEvent e) {
        this.logger = new LoggerWrapper(this.loggerSlf4j);
        (this.conf = new ViaRewindConfigImpl(this.configDir.resolve("config.yml").toFile())).reloadConfig();
        this.init(this.conf);
    }
    
    @Subscribe
    public void onReload(final ProxyReloadEvent e) {
        this.conf.reloadConfig();
    }
    
    @Override
    public Logger getLogger() {
        return this.logger;
    }
}
