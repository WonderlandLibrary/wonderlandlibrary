// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.sponge.platform;

import io.netty.channel.ChannelHandler;
import com.viaversion.viaversion.sponge.handlers.SpongeChannelInitializer;
import com.viaversion.viaversion.platform.WrappedChannelInitializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import org.spongepowered.api.MinecraftVersion;
import org.spongepowered.api.Sponge;
import com.viaversion.viaversion.platform.LegacyViaInjector;

public class SpongeViaInjector extends LegacyViaInjector
{
    @Override
    public int getServerProtocolVersion() throws ReflectiveOperationException {
        final MinecraftVersion version = Sponge.platform().minecraftVersion();
        return (int)version.getClass().getDeclaredMethod("getProtocol", (Class<?>[])new Class[0]).invoke(version, new Object[0]);
    }
    
    @Override
    protected Object getServerConnection() throws ReflectiveOperationException {
        final Class<?> serverClazz = Class.forName("net.minecraft.server.MinecraftServer");
        return serverClazz.getDeclaredMethod("getConnection", (Class<?>[])new Class[0]).invoke(Sponge.server(), new Object[0]);
    }
    
    @Override
    protected WrappedChannelInitializer createChannelInitializer(final ChannelInitializer<Channel> oldInitializer) {
        return new SpongeChannelInitializer(oldInitializer);
    }
    
    @Override
    protected void blame(final ChannelHandler bootstrapAcceptor) {
        throw new RuntimeException("Unable to find core component 'childHandler', please check your plugins. Issue: " + bootstrapAcceptor.getClass().getName());
    }
}
