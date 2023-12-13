/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.ThreadFactoryBuilder
 *  io.netty.channel.EventLoop
 *  io.netty.channel.local.LocalEventLoopGroup
 *  org.apache.logging.log4j.LogManager
 *  sun.misc.URLClassPath
 */
package me.wintware.client.viamcp.viafabric;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.EventLoop;
import io.netty.channel.local.LocalEventLoopGroup;
import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;
import me.wintware.client.viamcp.viafabric.platform.VRInjector;
import me.wintware.client.viamcp.viafabric.platform.VRLoader;
import me.wintware.client.viamcp.viafabric.platform.VRPlatform;
import me.wintware.client.viamcp.viafabric.util.JLoggerToLog4j;
import me.wintware.client.viamcp.viamcp.platform.ViaBackwardsPlatformImplementation;
import me.wintware.client.viamcp.viamcp.platform.ViaRewindPlatformImplementation;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import sun.misc.URLClassPath;
import us.myles.ViaVersion.ViaManager;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.data.MappingDataLoader;

public class ViaFabric {
    public static int clientSideVersion = 340;
    public static final Logger JLOGGER = new JLoggerToLog4j(LogManager.getLogger("ViaFabric"));
    public static final ExecutorService ASYNC_EXECUTOR;
    public static final EventLoop EVENT_LOOP;
    public static CompletableFuture<Void> INIT_FUTURE;

    public static String getVersion() {
        return "1.0";
    }

    public void onInitialize() throws IllegalAccessException, NoSuchFieldException, MalformedURLException {
        this.loadVia();
        Via.init(ViaManager.builder().injector(new VRInjector()).loader(new VRLoader()).platform(new VRPlatform()).build());
        MappingDataLoader.enableMappingsCache();
        new ViaBackwardsPlatformImplementation();
        new ViaRewindPlatformImplementation();
        Via.getManager().init();
        INIT_FUTURE.complete(null);
    }

    public void loadVia() throws NoSuchFieldException, IllegalAccessException, MalformedURLException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        Field addUrl = loader.getClass().getDeclaredField("ucp");
        addUrl.setAccessible(true);
        URLClassPath ucp = (URLClassPath)addUrl.get(loader);
        File[] files = new File(Minecraft.getMinecraft().mcDataDir, "mods").listFiles();
        if (files != null) {
            for (File f : files) {
                if (!f.isFile() || !f.getName().startsWith("Via") || !f.getName().toLowerCase().endsWith(".jar")) continue;
                ucp.addURL(f.toURI().toURL());
            }
        }
    }

    static {
        INIT_FUTURE = new CompletableFuture();
        ThreadFactory factory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ViaFabric-%d").build();
        ASYNC_EXECUTOR = Executors.newFixedThreadPool(8, factory);
        EVENT_LOOP = new LocalEventLoopGroup(1, factory).next();
        EVENT_LOOP.submit(INIT_FUTURE::join);
    }
}

