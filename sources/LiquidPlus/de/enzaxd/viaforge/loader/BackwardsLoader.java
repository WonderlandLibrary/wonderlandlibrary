/*
 * Decompiled with CFR 0.152.
 */
package de.enzaxd.viaforge.loader;

import com.viaversion.viabackwards.api.ViaBackwardsPlatform;
import de.enzaxd.viaforge.ViaForge;
import java.io.File;
import java.util.logging.Logger;

public class BackwardsLoader
implements ViaBackwardsPlatform {
    private final File file;

    public BackwardsLoader(File file) {
        this.file = new File(file, "ViaBackwards");
        this.init(this.file);
    }

    @Override
    public Logger getLogger() {
        return ViaForge.getInstance().getjLogger();
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

