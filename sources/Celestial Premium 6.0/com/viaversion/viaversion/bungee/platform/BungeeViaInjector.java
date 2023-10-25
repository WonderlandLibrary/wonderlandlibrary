/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelInitializer
 */
package com.viaversion.viaversion.bungee.platform;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.bungee.handlers.BungeeChannelInitializer;
import com.viaversion.viaversion.compatibility.ForcefulFieldModifier;
import com.viaversion.viaversion.compatibility.unsafe.UnsafeBackedForcefulFieldModifier;
import com.viaversion.viaversion.libs.fastutil.ints.IntLinkedOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSortedSet;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.util.ReflectionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import java.lang.reflect.Field;
import java.util.List;

public class BungeeViaInjector
implements ViaInjector {
    private final ForcefulFieldModifier forcefulFieldModifier;

    public BungeeViaInjector() {
        try {
            this.forcefulFieldModifier = new UnsafeBackedForcefulFieldModifier();
        }
        catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Cannot create a modifier accessor", ex);
        }
    }

    @Override
    public void inject() throws Exception {
        try {
            Class<?> pipelineUtils = Class.forName("net.md_5.bungee.netty.PipelineUtils");
            Field field = pipelineUtils.getDeclaredField("SERVER_CHILD");
            field.setAccessible(true);
            BungeeChannelInitializer newInit = new BungeeChannelInitializer((ChannelInitializer<Channel>)((ChannelInitializer)field.get(null)));
            this.forcefulFieldModifier.setField(field, null, (Object)newInit);
        }
        catch (Exception e) {
            Via.getPlatform().getLogger().severe("Unable to inject ViaVersion, please post these details on our GitHub and ensure you're using a compatible server version.");
            throw e;
        }
    }

    @Override
    public void uninject() {
        Via.getPlatform().getLogger().severe("ViaVersion cannot remove itself from Bungee without a reboot!");
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
        return ReflectionUtil.getStatic(Class.forName("net.md_5.bungee.protocol.ProtocolConstants"), "SUPPORTED_VERSION_IDS", List.class);
    }

    @Override
    public String getEncoderName() {
        return "via-encoder";
    }

    @Override
    public String getDecoderName() {
        return "via-decoder";
    }

    private ChannelInitializer<Channel> getChannelInitializer() throws Exception {
        Class<?> pipelineUtils = Class.forName("net.md_5.bungee.netty.PipelineUtils");
        Field field = pipelineUtils.getDeclaredField("SERVER_CHILD");
        field.setAccessible(true);
        return (ChannelInitializer)field.get(null);
    }

    @Override
    public JsonObject getDump() {
        JsonObject data = new JsonObject();
        try {
            ChannelInitializer<Channel> initializer = this.getChannelInitializer();
            data.addProperty("currentInitializer", initializer.getClass().getName());
            if (initializer instanceof BungeeChannelInitializer) {
                data.addProperty("originalInitializer", ((BungeeChannelInitializer)initializer).getOriginal().getClass().getName());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return data;
    }
}

