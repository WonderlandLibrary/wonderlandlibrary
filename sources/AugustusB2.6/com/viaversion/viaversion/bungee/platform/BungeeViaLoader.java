// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bungee.platform;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import com.viaversion.viaversion.bungee.service.ProtocolDetectorService;
import com.viaversion.viaversion.bungee.providers.BungeeMainHandProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MainHandProvider;
import com.viaversion.viaversion.bungee.providers.BungeeBossBarProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.BossBarProvider;
import com.viaversion.viaversion.bungee.providers.BungeeMovementTransmitter;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
import com.viaversion.viaversion.bungee.providers.BungeeEntityIdProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.EntityIdProvider;
import com.viaversion.viaversion.bungee.providers.BungeeVersionProvider;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.bungee.listeners.ElytraPatch;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.bungee.handlers.BungeeServerHandler;
import com.viaversion.viaversion.bungee.listeners.UpdateListener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.ProxyServer;
import java.util.HashSet;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.plugin.Listener;
import java.util.Set;
import com.viaversion.viaversion.BungeePlugin;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;

public class BungeeViaLoader implements ViaPlatformLoader
{
    private final BungeePlugin plugin;
    private final Set<Listener> listeners;
    private final Set<ScheduledTask> tasks;
    
    public BungeeViaLoader(final BungeePlugin plugin) {
        this.listeners = new HashSet<Listener>();
        this.tasks = new HashSet<ScheduledTask>();
        this.plugin = plugin;
    }
    
    private void registerListener(final Listener listener) {
        this.listeners.add(listener);
        ProxyServer.getInstance().getPluginManager().registerListener((Plugin)this.plugin, listener);
    }
    
    @Override
    public void load() {
        this.registerListener((Listener)this.plugin);
        this.registerListener((Listener)new UpdateListener());
        this.registerListener((Listener)new BungeeServerHandler());
        if (Via.getAPI().getServerVersion().lowestSupportedVersion() < ProtocolVersion.v1_9.getVersion()) {
            this.registerListener((Listener)new ElytraPatch());
        }
        Via.getManager().getProviders().use((Class<BungeeVersionProvider>)VersionProvider.class, new BungeeVersionProvider());
        Via.getManager().getProviders().use((Class<BungeeEntityIdProvider>)EntityIdProvider.class, new BungeeEntityIdProvider());
        if (Via.getAPI().getServerVersion().lowestSupportedVersion() < ProtocolVersion.v1_9.getVersion()) {
            Via.getManager().getProviders().use((Class<BungeeMovementTransmitter>)MovementTransmitterProvider.class, new BungeeMovementTransmitter());
            Via.getManager().getProviders().use((Class<BungeeBossBarProvider>)BossBarProvider.class, new BungeeBossBarProvider());
            Via.getManager().getProviders().use((Class<BungeeMainHandProvider>)MainHandProvider.class, new BungeeMainHandProvider());
        }
        if (this.plugin.getConf().getBungeePingInterval() > 0) {
            this.tasks.add(this.plugin.getProxy().getScheduler().schedule((Plugin)this.plugin, (Runnable)new ProtocolDetectorService(this.plugin), 0L, (long)this.plugin.getConf().getBungeePingInterval(), TimeUnit.SECONDS));
        }
    }
    
    @Override
    public void unload() {
        for (final Listener listener : this.listeners) {
            ProxyServer.getInstance().getPluginManager().unregisterListener(listener);
        }
        this.listeners.clear();
        for (final ScheduledTask task : this.tasks) {
            task.cancel();
        }
        this.tasks.clear();
    }
}
