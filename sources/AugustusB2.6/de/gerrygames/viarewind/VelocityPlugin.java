// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import de.gerrygames.viarewind.api.ViaRewindConfig;
import de.gerrygames.viarewind.api.ViaRewindConfigImpl;
import com.viaversion.viaversion.sponge.util.LoggerWrapper;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import java.nio.file.Path;
import com.google.inject.Inject;
import java.util.logging.Logger;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import de.gerrygames.viarewind.api.ViaRewindPlatform;

@Plugin(id = "viarewind", name = "ViaRewind", version = "2.0.2-SNAPSHOT", authors = { "Gerrygames" }, dependencies = { @Dependency(id = "viaversion"), @Dependency(id = "viabackwards", optional = true) }, url = "https://viaversion.com/rewind")
public class VelocityPlugin implements ViaRewindPlatform
{
    private Logger logger;
    @Inject
    private org.slf4j.Logger loggerSlf4j;
    @Inject
    @DataDirectory
    private Path configDir;
    
    @Subscribe(order = PostOrder.LATE)
    public void onProxyStart(final ProxyInitializeEvent e) {
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
