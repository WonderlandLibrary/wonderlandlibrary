// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.com.viamcp.loader;

import com.viaversion.viaversion.api.Via;
import java.util.logging.Logger;
import de.gerrygames.viarewind.api.ViaRewindConfig;
import de.gerrygames.viarewind.api.ViaRewindConfigImpl;
import java.io.File;
import de.gerrygames.viarewind.api.ViaRewindPlatform;

public class VRRewindLoader implements ViaRewindPlatform
{
    public VRRewindLoader(final File file) {
        final ViaRewindConfigImpl conf = new ViaRewindConfigImpl(file.toPath().resolve("ViaRewind").resolve("config.yml").toFile());
        conf.reloadConfig();
        this.init(conf);
    }
    
    @Override
    public Logger getLogger() {
        return Via.getPlatform().getLogger();
    }
}
