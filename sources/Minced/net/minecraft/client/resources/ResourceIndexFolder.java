// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import net.minecraft.util.ResourceLocation;
import java.io.File;

public class ResourceIndexFolder extends ResourceIndex
{
    private final File baseDir;
    
    public ResourceIndexFolder(final File folder) {
        this.baseDir = folder;
    }
    
    @Override
    public File getFile(final ResourceLocation location) {
        return new File(this.baseDir, location.toString().replace(':', '/'));
    }
    
    @Override
    public File getPackMcmeta() {
        return new File(this.baseDir, "pack.mcmeta");
    }
}
