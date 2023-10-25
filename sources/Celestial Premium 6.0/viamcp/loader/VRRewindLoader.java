/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  de.gerrygames.viarewind.api.ViaRewindConfig
 *  de.gerrygames.viarewind.api.ViaRewindConfigImpl
 *  de.gerrygames.viarewind.api.ViaRewindPlatform
 */
package viamcp.loader;

import com.viaversion.viaversion.api.Via;
import de.gerrygames.viarewind.api.ViaRewindConfig;
import de.gerrygames.viarewind.api.ViaRewindConfigImpl;
import de.gerrygames.viarewind.api.ViaRewindPlatform;
import java.io.File;
import java.util.logging.Logger;

public class VRRewindLoader
implements ViaRewindPlatform {
    public VRRewindLoader(File file) {
        ViaRewindConfigImpl conf = new ViaRewindConfigImpl(file.toPath().resolve("ViaRewind").resolve("config.yml").toFile());
        conf.reloadConfig();
        this.init((ViaRewindConfig)conf);
    }

    public Logger getLogger() {
        return Via.getPlatform().getLogger();
    }
}

