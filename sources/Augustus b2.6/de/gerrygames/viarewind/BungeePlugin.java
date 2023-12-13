// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind;

import de.gerrygames.viarewind.api.ViaRewindConfig;
import de.gerrygames.viarewind.api.ViaRewindConfigImpl;
import java.io.File;
import de.gerrygames.viarewind.api.ViaRewindPlatform;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeePlugin extends Plugin implements ViaRewindPlatform
{
    public void onEnable() {
        final ViaRewindConfigImpl conf = new ViaRewindConfigImpl(new File(this.getDataFolder(), "config.yml"));
        conf.reloadConfig();
        this.init(conf);
    }
}
