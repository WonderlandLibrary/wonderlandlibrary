// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.velocity.platform;

import com.viaversion.viaversion.velocity.listeners.UpdateListener;
import com.viaversion.viaversion.velocity.providers.VelocityVersionProvider;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.velocity.providers.VelocityBossBarProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.BossBarProvider;
import com.viaversion.viaversion.velocity.providers.VelocityMovementTransmitter;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.Via;
import com.velocitypowered.api.plugin.PluginContainer;
import com.viaversion.viaversion.VelocityPlugin;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;

public class VelocityViaLoader implements ViaPlatformLoader
{
    @Override
    public void load() {
        final Object plugin = VelocityPlugin.PROXY.getPluginManager().getPlugin("viaversion").flatMap(PluginContainer::getInstance).get();
        if (Via.getAPI().getServerVersion().lowestSupportedVersion() < ProtocolVersion.v1_9.getVersion()) {
            Via.getManager().getProviders().use((Class<VelocityMovementTransmitter>)MovementTransmitterProvider.class, new VelocityMovementTransmitter());
            Via.getManager().getProviders().use((Class<VelocityBossBarProvider>)BossBarProvider.class, new VelocityBossBarProvider());
        }
        Via.getManager().getProviders().use((Class<VelocityVersionProvider>)VersionProvider.class, new VelocityVersionProvider());
        VelocityPlugin.PROXY.getEventManager().register(plugin, (Object)new UpdateListener());
        final int pingInterval = ((VelocityViaConfig)Via.getPlatform().getConf()).getVelocityPingInterval();
        if (pingInterval > 0) {
            Via.getPlatform().runRepeatingSync(() -> Via.proxyPlatform().protocolDetectorService().probeAllServers(), pingInterval * 20L);
        }
    }
    
    @Override
    public void unload() {
    }
}
