// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind;

import de.gerrygames.viarewind.api.ViaRewindConfig;
import de.gerrygames.viarewind.api.ViaRewindConfigImpl;
import net.fabricmc.loader.api.FabricLoader;
import de.gerrygames.viarewind.fabric.util.LoggerWrapper;
import org.apache.logging.log4j.LogManager;
import java.util.logging.Logger;
import de.gerrygames.viarewind.api.ViaRewindPlatform;

public class ViaFabricAddon implements ViaRewindPlatform, Runnable
{
    private final Logger logger;
    
    public ViaFabricAddon() {
        this.logger = new LoggerWrapper(LogManager.getLogger("ViaRewind"));
    }
    
    @Override
    public void run() {
        final ViaRewindConfigImpl conf = new ViaRewindConfigImpl(FabricLoader.getInstance().getConfigDirectory().toPath().resolve("ViaRewind").resolve("config.yml").toFile());
        conf.reloadConfig();
        this.init(conf);
    }
    
    @Override
    public Logger getLogger() {
        return this.logger;
    }
}
