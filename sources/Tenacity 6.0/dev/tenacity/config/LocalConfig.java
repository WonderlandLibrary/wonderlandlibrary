// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.config;

import net.minecraft.client.Minecraft;
import java.io.File;

public class LocalConfig
{
    private final String name;
    private final File file;
    
    public LocalConfig(final String name) {
        this.name = name;
        this.file = new File(Minecraft.getMinecraft().mcDataDir + "/Tenacity/Configs/" + name + ".json");
    }
    
    public String getName() {
        return this.name;
    }
    
    public File getFile() {
        return this.file;
    }
}
