// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.sponge.platform;

import java.util.function.Consumer;
import com.viaversion.viaversion.sponge.listeners.UpdateListener;
import org.spongepowered.api.Sponge;
import java.util.HashSet;
import com.viaversion.viaversion.api.platform.PlatformTask;
import java.util.Set;
import com.viaversion.viaversion.SpongePlugin;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;

public class SpongeViaLoader implements ViaPlatformLoader
{
    private final SpongePlugin plugin;
    private final Set<Object> listeners;
    private final Set<PlatformTask> tasks;
    
    public SpongeViaLoader(final SpongePlugin plugin) {
        this.listeners = new HashSet<Object>();
        this.tasks = new HashSet<PlatformTask>();
        this.plugin = plugin;
    }
    
    private void registerListener(final Object listener) {
        Sponge.eventManager().registerListeners(this.plugin.container(), this.storeListener(listener));
    }
    
    private <T> T storeListener(final T listener) {
        this.listeners.add(listener);
        return listener;
    }
    
    @Override
    public void load() {
        this.registerListener(new UpdateListener());
    }
    
    @Override
    public void unload() {
        this.listeners.forEach(Sponge.eventManager()::unregisterListeners);
        this.listeners.clear();
        this.tasks.forEach(PlatformTask::cancel);
        this.tasks.clear();
    }
}
