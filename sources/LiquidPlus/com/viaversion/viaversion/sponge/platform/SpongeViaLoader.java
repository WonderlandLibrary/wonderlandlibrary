/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.spongepowered.api.Sponge
 *  org.spongepowered.api.event.EventManager
 */
package com.viaversion.viaversion.sponge.platform;

import com.viaversion.viaversion.SpongePlugin;
import com.viaversion.viaversion.api.platform.PlatformTask;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import com.viaversion.viaversion.sponge.listeners.UpdateListener;
import java.util.HashSet;
import java.util.Set;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.EventManager;

public class SpongeViaLoader
implements ViaPlatformLoader {
    private final SpongePlugin plugin;
    private final Set<Object> listeners = new HashSet<Object>();
    private final Set<PlatformTask> tasks = new HashSet<PlatformTask>();

    public SpongeViaLoader(SpongePlugin plugin) {
        this.plugin = plugin;
    }

    private void registerListener(Object listener) {
        Sponge.eventManager().registerListeners(this.plugin.container(), this.storeListener(listener));
    }

    private <T> T storeListener(T listener) {
        this.listeners.add(listener);
        return listener;
    }

    @Override
    public void load() {
        this.registerListener(new UpdateListener());
    }

    @Override
    public void unload() {
        this.listeners.forEach(arg_0 -> ((EventManager)Sponge.eventManager()).unregisterListeners(arg_0));
        this.listeners.clear();
        this.tasks.forEach(PlatformTask::cancel);
        this.tasks.clear();
    }
}

