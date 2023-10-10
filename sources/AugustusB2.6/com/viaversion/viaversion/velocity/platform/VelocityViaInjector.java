// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.velocity.platform;

import com.viaversion.viaversion.libs.gson.JsonObject;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import com.velocitypowered.api.network.ProtocolVersion;
import com.viaversion.viaversion.libs.fastutil.ints.IntLinkedOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSortedSet;
import com.viaversion.viaversion.velocity.handlers.VelocityChannelInitializer;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.util.ReflectionUtil;
import com.viaversion.viaversion.VelocityPlugin;
import io.netty.channel.ChannelInitializer;
import java.lang.reflect.Method;
import com.viaversion.viaversion.api.platform.ViaInjector;

public class VelocityViaInjector implements ViaInjector
{
    public static Method getPlayerInfoForwardingMode;
    
    private ChannelInitializer getInitializer() throws Exception {
        final Object connectionManager = ReflectionUtil.get(VelocityPlugin.PROXY, "cm", Object.class);
        final Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getServerChannelInitializer");
        return (ChannelInitializer)ReflectionUtil.invoke(channelInitializerHolder, "get");
    }
    
    private ChannelInitializer getBackendInitializer() throws Exception {
        final Object connectionManager = ReflectionUtil.get(VelocityPlugin.PROXY, "cm", Object.class);
        final Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getBackendChannelInitializer");
        return (ChannelInitializer)ReflectionUtil.invoke(channelInitializerHolder, "get");
    }
    
    @Override
    public void inject() throws Exception {
        Via.getPlatform().getLogger().info("Replacing channel initializers; you can safely ignore the following two warnings.");
        final Object connectionManager = ReflectionUtil.get(VelocityPlugin.PROXY, "cm", Object.class);
        final Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getServerChannelInitializer");
        final ChannelInitializer originalInitializer = this.getInitializer();
        channelInitializerHolder.getClass().getMethod("set", ChannelInitializer.class).invoke(channelInitializerHolder, new VelocityChannelInitializer(originalInitializer, false));
        final Object backendInitializerHolder = ReflectionUtil.invoke(connectionManager, "getBackendChannelInitializer");
        final ChannelInitializer backendInitializer = this.getBackendInitializer();
        backendInitializerHolder.getClass().getMethod("set", ChannelInitializer.class).invoke(backendInitializerHolder, new VelocityChannelInitializer(backendInitializer, true));
    }
    
    @Override
    public void uninject() {
        Via.getPlatform().getLogger().severe("ViaVersion cannot remove itself from Velocity without a reboot!");
    }
    
    @Override
    public int getServerProtocolVersion() throws Exception {
        return getLowestSupportedProtocolVersion();
    }
    
    @Override
    public IntSortedSet getServerProtocolVersions() throws Exception {
        final int lowestSupportedProtocolVersion = getLowestSupportedProtocolVersion();
        final IntSortedSet set = new IntLinkedOpenHashSet();
        for (final ProtocolVersion version : ProtocolVersion.SUPPORTED_VERSIONS) {
            if (version.getProtocol() >= lowestSupportedProtocolVersion) {
                set.add(version.getProtocol());
            }
        }
        return set;
    }
    
    public static int getLowestSupportedProtocolVersion() {
        try {
            if (VelocityViaInjector.getPlayerInfoForwardingMode != null && ((Enum)VelocityViaInjector.getPlayerInfoForwardingMode.invoke(VelocityPlugin.PROXY.getConfiguration(), new Object[0])).name().equals("MODERN")) {
                return com.viaversion.viaversion.api.protocol.version.ProtocolVersion.v1_13.getVersion();
            }
        }
        catch (IllegalAccessException ex) {}
        catch (InvocationTargetException ex2) {}
        return ProtocolVersion.MINIMUM_VERSION.getProtocol();
    }
    
    @Override
    public JsonObject getDump() {
        final JsonObject data = new JsonObject();
        try {
            data.addProperty("currentInitializer", this.getInitializer().getClass().getName());
        }
        catch (Exception ex) {}
        return data;
    }
    
    static {
        try {
            VelocityViaInjector.getPlayerInfoForwardingMode = Class.forName("com.velocitypowered.proxy.config.VelocityConfiguration").getMethod("getPlayerInfoForwardingMode", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException | ClassNotFoundException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            e.printStackTrace();
        }
    }
}
