// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viabackwards.api.ViaBackwardsPlatform;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeePlugin extends Plugin implements ViaBackwardsPlatform
{
    public void onLoad() {
        Via.getManager().addEnableListener(() -> this.init(this.getDataFolder()));
    }
    
    public void disable() {
    }
}
