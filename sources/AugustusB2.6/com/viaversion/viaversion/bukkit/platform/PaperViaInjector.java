// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.platform;

import java.lang.reflect.Method;
import net.kyori.adventure.key.Key;
import java.lang.reflect.Proxy;
import com.viaversion.viaversion.bukkit.handlers.BukkitChannelInitializer;
import io.netty.channel.Channel;

public final class PaperViaInjector
{
    public static final boolean PAPER_INJECTION_METHOD;
    public static final boolean PAPER_PROTOCOL_METHOD;
    public static final boolean PAPER_PACKET_LIMITER;
    
    private PaperViaInjector() {
    }
    
    public static void setPaperChannelInitializeListener() throws ReflectiveOperationException {
        final Class<?> listenerClass = Class.forName("io.papermc.paper.network.ChannelInitializeListener");
        final Object channelInitializeListener = Proxy.newProxyInstance(BukkitViaInjector.class.getClassLoader(), new Class[] { listenerClass }, (proxy, method, args) -> {
            if (method.getName().equals("afterInitChannel")) {
                BukkitChannelInitializer.afterChannelInitialize((Channel)args[0]);
                return null;
            }
            else {
                return method.invoke(proxy, args);
            }
        });
        final Class<?> holderClass = Class.forName("io.papermc.paper.network.ChannelInitializeListenerHolder");
        final Method addListenerMethod = holderClass.getDeclaredMethod("addListener", Key.class, listenerClass);
        addListenerMethod.invoke(null, Key.key("viaversion", "injector"), channelInitializeListener);
    }
    
    public static void removePaperChannelInitializeListener() throws ReflectiveOperationException {
        final Class<?> holderClass = Class.forName("io.papermc.paper.network.ChannelInitializeListenerHolder");
        final Method addListenerMethod = holderClass.getDeclaredMethod("removeListener", Key.class);
        addListenerMethod.invoke(null, Key.key("viaversion", "injector"));
    }
    
    private static boolean hasServerProtocolMethod() {
        try {
            Class.forName("org.bukkit.UnsafeValues").getDeclaredMethod("getProtocolVersion", (Class<?>[])new Class[0]);
            return true;
        }
        catch (ReflectiveOperationException e) {
            return false;
        }
    }
    
    private static boolean hasPaperInjectionMethod() {
        return hasClass("io.papermc.paper.network.ChannelInitializeListener");
    }
    
    private static boolean hasPacketLimiter() {
        return hasClass("com.destroystokyo.paper.PaperConfig$PacketLimit") || hasClass("io.papermc.paper.PaperConfig$PacketLimit");
    }
    
    private static boolean hasClass(final String className) {
        try {
            Class.forName(className);
            return true;
        }
        catch (ReflectiveOperationException e) {
            return false;
        }
    }
    
    static {
        PAPER_INJECTION_METHOD = hasPaperInjectionMethod();
        PAPER_PROTOCOL_METHOD = hasServerProtocolMethod();
        PAPER_PACKET_LIMITER = hasPacketLimiter();
    }
}
