// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.sponge.platform;

import io.netty.channel.ChannelHandler;
import com.viaversion.viaversion.sponge.handlers.SpongeChannelInitializer;
import com.viaversion.viaversion.platform.WrappedChannelInitializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import java.lang.reflect.Method;
import org.spongepowered.api.MinecraftVersion;
import org.spongepowered.api.Sponge;
import com.viaversion.viaversion.platform.LegacyViaInjector;

public class SpongeViaInjector extends LegacyViaInjector
{
    @Override
    public int getServerProtocolVersion() throws ReflectiveOperationException {
        final MinecraftVersion version = Sponge.getPlatform().getMinecraftVersion();
        return (int)version.getClass().getDeclaredMethod("getProtocol", (Class<?>[])new Class[0]).invoke(version, new Object[0]);
    }
    
    @Override
    protected Object getServerConnection() throws ReflectiveOperationException {
        final Class<?> serverClazz = Class.forName("net.minecraft.server.MinecraftServer");
        for (final Method method : serverClazz.getDeclaredMethods()) {
            if (method.getReturnType().getSimpleName().equals("NetworkSystem") && method.getParameterTypes().length == 0) {
                final Object connection = method.invoke(Sponge.getServer(), new Object[0]);
                if (connection != null) {
                    return connection;
                }
            }
        }
        return null;
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
