/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelFuture
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelInitializer
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.PluginDescriptionFile
 */
package com.viaversion.viaversion.bukkit.platform;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.bukkit.handlers.BukkitChannelInitializer;
import com.viaversion.viaversion.bukkit.platform.PaperViaInjector;
import com.viaversion.viaversion.bukkit.util.NMSUtil;
import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.util.ConcurrentList;
import com.viaversion.viaversion.util.ListWrapper;
import com.viaversion.viaversion.util.Pair;
import com.viaversion.viaversion.util.ReflectionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;

public class BukkitViaInjector
implements ViaInjector {
    private final List<ChannelFuture> injectedFutures = new ArrayList<ChannelFuture>();
    private final List<Pair<Field, Object>> injectedLists = new ArrayList<Pair<Field, Object>>();
    private boolean protocolLib;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void inject() throws Exception {
        if (PaperViaInjector.PAPER_INJECTION_METHOD) {
            PaperViaInjector.setPaperChannelInitializeListener();
            return;
        }
        try {
            Object connection = BukkitViaInjector.getServerConnection();
            if (connection == null) {
                throw new Exception("We failed to find the core component 'ServerConnection', please file an issue on our GitHub.");
            }
            for (Field field : connection.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(connection);
                if (!(value instanceof List)) continue;
                ListWrapper wrapper = new ListWrapper((List)value){

                    @Override
                    public void handleAdd(Object o) {
                        if (o instanceof ChannelFuture) {
                            try {
                                BukkitViaInjector.this.injectChannelFuture((ChannelFuture)o);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                this.injectedLists.add(new Pair<Field, Object>(field, connection));
                field.set(connection, wrapper);
                ListWrapper listWrapper = wrapper;
                synchronized (listWrapper) {
                    for (Object o : (List)value) {
                        if (!(o instanceof ChannelFuture)) break;
                        this.injectChannelFuture((ChannelFuture)o);
                    }
                }
            }
        }
        catch (Exception e) {
            Via.getPlatform().getLogger().severe("Unable to inject ViaVersion, please post these details on our GitHub and ensure you're using a compatible server version.");
            throw e;
        }
    }

    private void injectChannelFuture(ChannelFuture future) throws Exception {
        try {
            List names = future.channel().pipeline().names();
            ChannelHandler bootstrapAcceptor = null;
            for (String name : names) {
                ChannelHandler handler = future.channel().pipeline().get(name);
                try {
                    ReflectionUtil.get((Object)handler, "childHandler", ChannelInitializer.class);
                    bootstrapAcceptor = handler;
                }
                catch (Exception exception) {}
            }
            if (bootstrapAcceptor == null) {
                bootstrapAcceptor = future.channel().pipeline().first();
            }
            try {
                ChannelInitializer oldInit = ReflectionUtil.get((Object)bootstrapAcceptor, "childHandler", ChannelInitializer.class);
                BukkitChannelInitializer newInit = new BukkitChannelInitializer((ChannelInitializer<Channel>)oldInit);
                ReflectionUtil.set((Object)bootstrapAcceptor, "childHandler", (Object)newInit);
                this.injectedFutures.add(future);
            }
            catch (NoSuchFieldException e) {
                ClassLoader cl = bootstrapAcceptor.getClass().getClassLoader();
                if (cl.getClass().getName().equals("org.bukkit.plugin.java.PluginClassLoader")) {
                    PluginDescriptionFile yaml = ReflectionUtil.get(cl, "description", PluginDescriptionFile.class);
                    throw new Exception("Unable to inject, due to " + bootstrapAcceptor.getClass().getName() + ", try without the plugin " + yaml.getName() + "?");
                }
                throw new Exception("Unable to find core component 'childHandler', please check your plugins. issue: " + bootstrapAcceptor.getClass().getName());
            }
        }
        catch (Exception e) {
            Via.getPlatform().getLogger().severe("We failed to inject ViaVersion, have you got late-bind enabled with something else?");
            throw e;
        }
    }

    @Override
    public void uninject() throws Exception {
        if (PaperViaInjector.PAPER_INJECTION_METHOD) {
            PaperViaInjector.removePaperChannelInitializeListener();
            return;
        }
        for (ChannelFuture channelFuture : this.injectedFutures) {
            List names = channelFuture.channel().pipeline().names();
            ChannelHandler bootstrapAcceptor = null;
            for (String name : names) {
                ChannelHandler handler = channelFuture.channel().pipeline().get(name);
                try {
                    ChannelInitializer oldInit = ReflectionUtil.get((Object)handler, "childHandler", ChannelInitializer.class);
                    if (!(oldInit instanceof BukkitChannelInitializer)) continue;
                    bootstrapAcceptor = handler;
                }
                catch (Exception exception) {}
            }
            if (bootstrapAcceptor == null) {
                bootstrapAcceptor = channelFuture.channel().pipeline().first();
            }
            try {
                ChannelInitializer oldInit = ReflectionUtil.get((Object)bootstrapAcceptor, "childHandler", ChannelInitializer.class);
                if (!(oldInit instanceof BukkitChannelInitializer)) continue;
                ReflectionUtil.set((Object)bootstrapAcceptor, "childHandler", ((BukkitChannelInitializer)oldInit).getOriginal());
            }
            catch (Exception e) {
                Via.getPlatform().getLogger().severe("Failed to remove injection handler, reload won't work with connections, please reboot!");
            }
        }
        this.injectedFutures.clear();
        for (Pair pair : this.injectedLists) {
            try {
                Object o = ((Field)pair.getKey()).get(pair.getValue());
                if (!(o instanceof ListWrapper)) continue;
                ((Field)pair.getKey()).set(pair.getValue(), ((ListWrapper)o).getOriginalList());
            }
            catch (IllegalAccessException e) {
                Via.getPlatform().getLogger().severe("Failed to remove injection, reload won't work with connections, please reboot!");
            }
        }
        this.injectedLists.clear();
    }

    @Override
    public boolean lateProtocolVersionSetting() {
        return true;
    }

    @Override
    public int getServerProtocolVersion() throws Exception {
        if (PaperViaInjector.PAPER_PROTOCOL_METHOD) {
            return Bukkit.getUnsafe().getProtocolVersion();
        }
        try {
            Class<?> serverClazz = NMSUtil.nms("MinecraftServer", "net.minecraft.server.MinecraftServer");
            Object server = ReflectionUtil.invokeStatic(serverClazz, "getServer");
            Class<?> pingClazz = NMSUtil.nms("ServerPing", "net.minecraft.network.protocol.status.ServerPing");
            Object ping = null;
            for (Field f : serverClazz.getDeclaredFields()) {
                if (f.getType() == null || !f.getType().getSimpleName().equals("ServerPing")) continue;
                f.setAccessible(true);
                ping = f.get(server);
            }
            if (ping != null) {
                Object serverData = null;
                for (Field f : pingClazz.getDeclaredFields()) {
                    if (f.getType() == null || !f.getType().getSimpleName().endsWith("ServerData")) continue;
                    f.setAccessible(true);
                    serverData = f.get(ping);
                }
                if (serverData != null) {
                    int protocolVersion = -1;
                    for (Field f : serverData.getClass().getDeclaredFields()) {
                        if (f.getType() == null || f.getType() != Integer.TYPE) continue;
                        f.setAccessible(true);
                        protocolVersion = (Integer)f.get(serverData);
                    }
                    if (protocolVersion != -1) {
                        return protocolVersion;
                    }
                }
            }
        }
        catch (Exception e) {
            throw new Exception("Failed to get server", e);
        }
        throw new Exception("Failed to get server");
    }

    @Override
    public String getEncoderName() {
        return "encoder";
    }

    @Override
    public String getDecoderName() {
        return this.protocolLib ? "protocol_lib_decoder" : "decoder";
    }

    public static Object getServerConnection() throws Exception {
        Class<?> serverClazz = NMSUtil.nms("MinecraftServer", "net.minecraft.server.MinecraftServer");
        Object server = ReflectionUtil.invokeStatic(serverClazz, "getServer");
        Object connection = null;
        for (Method m : serverClazz.getDeclaredMethods()) {
            if (m.getReturnType() == null || !m.getReturnType().getSimpleName().equals("ServerConnection") || m.getParameterTypes().length != 0) continue;
            connection = m.invoke(server, new Object[0]);
        }
        return connection;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean isBinded() {
        if (PaperViaInjector.PAPER_INJECTION_METHOD) {
            return true;
        }
        try {
            Object connection = BukkitViaInjector.getServerConnection();
            if (connection == null) {
                return false;
            }
            for (Field field : connection.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(connection);
                if (!(value instanceof List)) continue;
                Object object = value;
                synchronized (object) {
                    Object o;
                    Iterator iterator = ((List)value).iterator();
                    if (iterator.hasNext() && (o = iterator.next()) instanceof ChannelFuture) {
                        return true;
                    }
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return false;
    }

    @Override
    public JsonObject getDump() {
        JsonObject data = new JsonObject();
        JsonArray injectedChannelInitializers = new JsonArray();
        for (ChannelFuture cf : this.injectedFutures) {
            JsonObject info = new JsonObject();
            info.addProperty("futureClass", cf.getClass().getName());
            info.addProperty("channelClass", cf.channel().getClass().getName());
            JsonArray pipeline = new JsonArray();
            for (String pipeName : cf.channel().pipeline().names()) {
                JsonObject pipe = new JsonObject();
                pipe.addProperty("name", pipeName);
                if (cf.channel().pipeline().get(pipeName) != null) {
                    pipe.addProperty("class", cf.channel().pipeline().get(pipeName).getClass().getName());
                    try {
                        ChannelInitializer child = ReflectionUtil.get((Object)cf.channel().pipeline().get(pipeName), "childHandler", ChannelInitializer.class);
                        pipe.addProperty("childClass", child.getClass().getName());
                        if (child instanceof BukkitChannelInitializer) {
                            pipe.addProperty("oldInit", ((BukkitChannelInitializer)child).getOriginal().getClass().getName());
                        }
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                pipeline.add(pipe);
            }
            info.add("pipeline", pipeline);
            injectedChannelInitializers.add(info);
        }
        data.add("injectedChannelInitializers", injectedChannelInitializers);
        JsonObject wrappedLists = new JsonObject();
        JsonObject currentLists = new JsonObject();
        try {
            for (Pair<Field, Object> pair : this.injectedLists) {
                Object list = pair.getKey().get(pair.getValue());
                currentLists.addProperty(pair.getKey().getName(), list.getClass().getName());
                if (!(list instanceof ListWrapper)) continue;
                wrappedLists.addProperty(pair.getKey().getName(), ((ListWrapper)list).getOriginalList().getClass().getName());
            }
            data.add("wrappedLists", wrappedLists);
            data.add("currentLists", currentLists);
        }
        catch (Exception exception) {
            // empty catch block
        }
        data.addProperty("binded", BukkitViaInjector.isBinded());
        return data;
    }

    public static void patchLists() throws Exception {
        if (PaperViaInjector.PAPER_INJECTION_METHOD) {
            return;
        }
        Object connection = BukkitViaInjector.getServerConnection();
        if (connection == null) {
            Via.getPlatform().getLogger().warning("We failed to find the core component 'ServerConnection', please file an issue on our GitHub.");
            return;
        }
        for (Field field : connection.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(connection);
            if (!(value instanceof List) || value instanceof ConcurrentList) continue;
            ConcurrentList list = new ConcurrentList();
            list.addAll((Collection)value);
            field.set(connection, list);
        }
    }

    public void setProtocolLib(boolean protocolLib) {
        this.protocolLib = protocolLib;
    }
}

