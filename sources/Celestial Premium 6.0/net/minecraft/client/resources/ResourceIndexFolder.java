/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.resources;

import java.io.File;
import net.minecraft.client.resources.ResourceIndex;
import net.minecraft.util.ResourceLocation;

public class ResourceIndexFolder
extends ResourceIndex {
    private final File baseDir;

    public ResourceIndexFolder(File folder) {
        this.baseDir = folder;
    }

    @Override
    public File getFile(ResourceLocation location) {
        return new File(this.baseDir, location.toString().replace(':', '/'));
    }

    @Override
    public File getPackMcmeta() {
        return new File(this.baseDir, "pack.mcmeta");
    }
}

