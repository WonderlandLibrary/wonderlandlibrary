// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bungee.platform;

import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.libs.gson.JsonObject;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.ints.IntLinkedOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSortedSet;
import io.netty.channel.ChannelHandler;
import com.viaversion.viaversion.bungee.handlers.BungeeChannelInitializer;
import com.viaversion.viaversion.util.ReflectionUtil;
import io.netty.channel.ChannelInitializer;
import com.viaversion.viaversion.api.Via;
import java.util.Iterator;
import com.viaversion.viaversion.util.SetWrapper;
import net.md_5.bungee.api.ProxyServer;
import java.util.Set;
import java.util.ArrayList;
import io.netty.channel.Channel;
import java.util.List;
import java.lang.reflect.Field;
import com.viaversion.viaversion.api.platform.ViaInjector;

public class BungeeViaInjector implements ViaInjector
{
    private static final Field LISTENERS_FIELD;
    private final List<Channel> injectedChannels;
    
    public BungeeViaInjector() {
        this.injectedChannels = new ArrayList<Channel>();
    }
    
    @Override
    public void inject() throws ReflectiveOperationException {
        final Set<Channel> listeners = (Set<Channel>)BungeeViaInjector.LISTENERS_FIELD.get(ProxyServer.getInstance());
        final Set<Channel> wrapper = new SetWrapper<Channel>(listeners, channel -> {
            try {
                this.injectChannel(channel);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            return;
        });
        BungeeViaInjector.LISTENERS_FIELD.set(ProxyServer.getInstance(), wrapper);
        for (final Channel channel2 : listeners) {
            this.injectChannel(channel2);
        }
    }
    
    @Override
    public void uninject() {
        Via.getPlatform().getLogger().severe("ViaVersion cannot remove itself from Bungee without a reboot!");
    }
    
    private void injectChannel(final Channel channel) throws ReflectiveOperationException {
        final List<String> names = channel.pipeline().names();
        ChannelHandler bootstrapAcceptor = null;
        for (final String name : names) {
            final ChannelHandler handler = channel.pipeline().get(name);
            try {
                ReflectionUtil.get(handler, "childHandler", ChannelInitializer.class);
                bootstrapAcceptor = handler;
            }
            catch (Exception ex) {}
        }
        if (bootstrapAcceptor == null) {
            bootstrapAcceptor = channel.pipeline().first();
        }
        if (bootstrapAcceptor.getClass().getName().equals("net.md_5.bungee.query.QueryHandler")) {
            return;
        }
        try {
            final ChannelInitializer<Channel> oldInit = ReflectionUtil.get(bootstrapAcceptor, "childHandler", (Class<ChannelInitializer<Channel>>)ChannelInitializer.class);
            final ChannelInitializer<Channel> newInit = new BungeeChannelInitializer(oldInit);
            ReflectionUtil.set(bootstrapAcceptor, "childHandler", newInit);
            this.injectedChannels.add(channel);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException("Unable to find core component 'childHandler', please check your plugins. issue: " + bootstrapAcceptor.getClass().getName());
        }
    }
    
    @Override
    public int getServerProtocolVersion() throws Exception {
        return this.getBungeeSupportedVersions().get(0);
    }
    
    @Override
    public IntSortedSet getServerProtocolVersions() throws Exception {
        return new IntLinkedOpenHashSet(this.getBungeeSupportedVersions());
    }
    
    private List<Integer> getBungeeSupportedVersions() throws Exception {
        return ReflectionUtil.getStatic(Class.forName("net.md_5.bungee.protocol.ProtocolConstants"), "SUPPORTED_VERSION_IDS", (Class<List<Integer>>)List.class);
    }
    
    @Override
    public JsonObject getDump() {
        final JsonObject data = new JsonObject();
        final JsonArray injectedChannelInitializers = new JsonArray();
        for (final Channel channel : this.injectedChannels) {
            final JsonObject channelInfo = new JsonObject();
            channelInfo.addProperty("channelClass", channel.getClass().getName());
            final JsonArray pipeline = new JsonArray();
            for (final String pipeName : channel.pipeline().names()) {
                final JsonObject handlerInfo = new JsonObject();
                handlerInfo.addProperty("name", pipeName);
                final ChannelHandler channelHandler = channel.pipeline().get(pipeName);
                if (channelHandler == null) {
                    handlerInfo.addProperty("status", "INVALID");
                }
                else {
                    handlerInfo.addProperty("class", channelHandler.getClass().getName());
                    try {
                        final Object child = ReflectionUtil.get(channelHandler, "childHandler", ChannelInitializer.class);
                        handlerInfo.addProperty("childClass", child.getClass().getName());
                        if (child instanceof BungeeChannelInitializer) {
                            handlerInfo.addProperty("oldInit", ((BungeeChannelInitializer)child).getOriginal().getClass().getName());
                        }
                    }
                    catch (ReflectiveOperationException ex) {}
                    pipeline.add(handlerInfo);
                }
            }
            channelInfo.add("pipeline", pipeline);
            injectedChannelInitializers.add(channelInfo);
        }
        data.add("injectedChannelInitializers", injectedChannelInitializers);
        try {
            final Object list = BungeeViaInjector.LISTENERS_FIELD.get(ProxyServer.getInstance());
            data.addProperty("currentList", list.getClass().getName());
            if (list instanceof SetWrapper) {
                data.addProperty("wrappedList", ((SetWrapper)list).originalSet().getClass().getName());
            }
        }
        catch (ReflectiveOperationException ex2) {}
        return data;
    }
    
    static {
        try {
            (LISTENERS_FIELD = ProxyServer.getInstance().getClass().getDeclaredField("listeners")).setAccessible(true);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to access listeners field.", e);
        }
    }
}
