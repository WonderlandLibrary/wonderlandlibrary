/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.ThreadFactoryBuilder
 *  io.netty.channel.EventLoop
 *  io.netty.channel.local.LocalEventLoopGroup
 *  org.apache.logging.log4j.LogManager
 */
package de.enzaxd.viaforge;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.viaversion.viaversion.ViaManagerImpl;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import de.enzaxd.viaforge.loader.BackwardsLoader;
import de.enzaxd.viaforge.loader.RewindLoader;
import de.enzaxd.viaforge.platform.Injector;
import de.enzaxd.viaforge.platform.Platform;
import de.enzaxd.viaforge.platform.ProviderLoader;
import de.enzaxd.viaforge.util.JLoggerToLog4j;
import io.netty.channel.EventLoop;
import io.netty.channel.local.LocalEventLoopGroup;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

public class ViaForge {
    public static final int SHARED_VERSION = 47;
    private static final ViaForge instance = new ViaForge();
    private final Logger jLogger = new JLoggerToLog4j(LogManager.getLogger((String)"ViaForge"));
    private final CompletableFuture<Void> initFuture = new CompletableFuture();
    private ExecutorService asyncExecutor;
    private EventLoop eventLoop;
    private File file;
    private int version;
    private String lastServer;

    public static ViaForge getInstance() {
        return instance;
    }

    public void start() {
        ThreadFactory factory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ViaForge-%d").build();
        this.asyncExecutor = Executors.newFixedThreadPool(8, factory);
        this.eventLoop = new LocalEventLoopGroup(1, factory).next();
        this.eventLoop.submit(this.initFuture::join);
        this.setVersion(47);
        this.file = new File("ViaForge");
        if (this.file.mkdir()) {
            this.getjLogger().info("Creating ViaForge Folder");
        }
        Via.init(ViaManagerImpl.builder().injector(new Injector()).loader(new ProviderLoader()).platform(new Platform(this.file)).build());
        MappingDataLoader.enableMappingsCache();
        ((ViaManagerImpl)Via.getManager()).init();
        new BackwardsLoader(this.file);
        new RewindLoader(this.file);
        this.initFuture.complete(null);
    }

    public Logger getjLogger() {
        return this.jLogger;
    }

    public CompletableFuture<Void> getInitFuture() {
        return this.initFuture;
    }

    public ExecutorService getAsyncExecutor() {
        return this.asyncExecutor;
    }

    public EventLoop getEventLoop() {
        return this.eventLoop;
    }

    public File getFile() {
        return this.file;
    }

    public String getLastServer() {
        return this.lastServer;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setLastServer(String lastServer) {
        this.lastServer = lastServer;
    }
}

