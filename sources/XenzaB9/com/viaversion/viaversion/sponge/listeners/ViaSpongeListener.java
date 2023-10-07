// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.sponge.listeners;

import org.spongepowered.api.Sponge;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.SpongePlugin;
import java.lang.reflect.Field;
import com.viaversion.viaversion.ViaListener;

public class ViaSpongeListener extends ViaListener
{
    private static Field entityIdField;
    private final SpongePlugin plugin;
    
    public ViaSpongeListener(final SpongePlugin plugin, final Class<? extends Protocol> requiredPipeline) {
        super(requiredPipeline);
        this.plugin = plugin;
    }
    
    @Override
    public void register() {
        if (this.isRegistered()) {
            return;
        }
        Sponge.eventManager().registerListeners(this.plugin.container(), (Object)this);
        this.setRegistered(true);
    }
}
