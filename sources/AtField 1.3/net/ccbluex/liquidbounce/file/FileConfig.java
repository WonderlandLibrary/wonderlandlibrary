/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.file;

import java.io.File;
import java.io.IOException;

public abstract class FileConfig {
    private final File file;

    protected abstract void loadConfig() throws IOException;

    public FileConfig(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public boolean hasConfig() {
        return this.file.exists();
    }

    protected abstract void saveConfig() throws IOException;

    public void createConfig() throws IOException {
        this.file.createNewFile();
    }
}

