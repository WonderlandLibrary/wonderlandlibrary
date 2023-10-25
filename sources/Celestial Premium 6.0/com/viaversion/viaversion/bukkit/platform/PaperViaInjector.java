/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 *  net.kyori.adventure.key.Key
 */
package com.viaversion.viaversion.bukkit.platform;

import com.viaversion.viaversion.bukkit.handlers.BukkitChannelInitializer;
import com.viaversion.viaversion.bukkit.platform.BukkitViaInjector;
import io.netty.channel.Channel;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import net.kyori.adventure.key.Key;

public final class PaperViaInjector {
    public static final boolean PAPER_INJECTION_METHOD = PaperViaInjector.hasPaperInjectionMethod();
    public static final boolean PAPER_PROTOCOL_METHOD = PaperViaInjector.hasServerProtocolMethod();

    private PaperViaInjector() {
    }

    public static void setPaperChannelInitializeListener() throws ReflectiveOperationException {
        Class<?> listenerClass = Class.forName("io.papermc.paper.network.ChannelInitializeListener");
        Object channelInitializeListener = Proxy.newProxyInstance(BukkitViaInjector.class.getClassLoader(), new Class[]{listenerClass}, (proxy, method, args) -> {
            if (method.getName().equals("afterInitChannel")) {
                BukkitChannelInitializer.afterChannelInitialize((Channel)args[0]);
                return null;
            }
            return method.invoke(proxy, args);
        });
        Class<?> holderClass = Class.forName("io.papermc.paper.network.ChannelInitializeListenerHolder");
        Method addListenerMethod = holderClass.getDeclaredMethod("addListener", Key.class, listenerClass);
        addListenerMethod.invoke(null, new Object[]{Key.key((String)"viaversion", (String)"injector"), channelInitializeListener});
    }

    public static void removePaperChannelInitializeListener() throws ReflectiveOperationException {
        Class<?> holderClass = Class.forName("io.papermc.paper.network.ChannelInitializeListenerHolder");
        Method addListenerMethod = holderClass.getDeclaredMethod("removeListener", Key.class);
        addListenerMethod.invoke(null, new Object[]{Key.key((String)"viaversion", (String)"injector")});
    }

    private static boolean hasServerProtocolMethod() {
        try {
            Class.forName("org.bukkit.UnsafeValues").getDeclaredMethod("getProtocolVersion", new Class[0]);
            return true;
        }
        catch (ReflectiveOperationException e) {
            return false;
        }
    }

    private static boolean hasPaperInjectionMethod() {
        try {
            Class.forName("io.papermc.paper.network.ChannelInitializeListener");
            return true;
        }
        catch (ReflectiveOperationException e) {
            return false;
        }
    }
}

