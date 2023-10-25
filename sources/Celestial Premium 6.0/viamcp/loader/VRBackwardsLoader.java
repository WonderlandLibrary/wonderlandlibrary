/*
 * Decompiled with CFR 0.150.
 */
package viamcp.loader;

import com.viaversion.viabackwards.api.ViaBackwardsPlatform;
import java.io.File;
import java.util.logging.Logger;
import viamcp.ViaMCP;

public class VRBackwardsLoader
implements ViaBackwardsPlatform {
    private final File file;

    public VRBackwardsLoader(File file) {
        this.file = new File(file, "ViaBackwards");
        this.init(this.file);
    }

    @Override
    public Logger getLogger() {
        return ViaMCP.getInstance().getjLogger();
    }

    @Override
    public void disable() {
    }

    @Override
    public boolean isOutdated() {
        return false;
    }

    @Override
    public File getDataFolder() {
        return new File(this.file, "config.yml");
    }
}

