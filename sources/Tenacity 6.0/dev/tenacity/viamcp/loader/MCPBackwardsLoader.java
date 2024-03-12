// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.viamcp.loader;

import dev.tenacity.viamcp.ViaMCP;
import java.util.logging.Logger;
import java.io.File;
import com.viaversion.viabackwards.api.ViaBackwardsPlatform;

public class MCPBackwardsLoader implements ViaBackwardsPlatform
{
    private final File file;
    
    public MCPBackwardsLoader(final File file) {
        this.init(this.file = new File(file, "ViaBackwards"));
    }
    
    public Logger getLogger() {
        return ViaMCP.getInstance().getjLogger();
    }
    
    public void disable() {
    }
    
    public boolean isOutdated() {
        return false;
    }
    
    public File getDataFolder() {
        return new File(this.file, "config.yml");
    }
}
