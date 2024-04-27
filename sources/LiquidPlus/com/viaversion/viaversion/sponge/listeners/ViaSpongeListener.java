/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.spongepowered.api.Sponge
 */
package com.viaversion.viaversion.sponge.listeners;

import com.viaversion.viaversion.SpongePlugin;
import com.viaversion.viaversion.ViaListener;
import com.viaversion.viaversion.api.protocol.Protocol;
import java.lang.reflect.Field;
import org.spongepowered.api.Sponge;

public class ViaSpongeListener
extends ViaListener {
    private static Field entityIdField;
    private final SpongePlugin plugin;

    public ViaSpongeListener(SpongePlugin plugin, Class<? extends Protocol> requiredPipeline) {
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

