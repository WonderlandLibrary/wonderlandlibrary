/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.channel.EventLoop
 *  io.netty.channel.local.LocalEventLoopGroup
 */
package viamcp;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.viaversion.viaversion.ViaManagerImpl;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import io.netty.channel.EventLoop;
import io.netty.channel.local.LocalEventLoopGroup;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import viamcp.loader.VRBackwardsLoader;
import viamcp.loader.VRProviderLoader;
import viamcp.loader.VRRewindLoader;
import viamcp.platform.VRInjector;
import viamcp.platform.VRPlatform;
import viamcp.utils.JLoggerToLog4j;

public class ViaMCP {
    public static final int PROTOCOL_VERSION = 340;
    private static final ViaMCP instance = new ViaMCP();
    private final Logger jLogger = new JLoggerToLog4j(LogManager.getLogger("ViaMCP"));
    private final CompletableFuture<Void> INIT_FUTURE = new CompletableFuture();
    private ExecutorService ASYNC_EXEC;
    private EventLoop EVENT_LOOP;
    private File file;
    private int version;
    private String lastServer;

    public static ViaMCP getInstance() {
        return instance;
    }

    public void start() {
        ThreadFactory factory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ViaMCP-%d").build();
        this.ASYNC_EXEC = Executors.newFixedThreadPool(8, factory);
        this.EVENT_LOOP = new LocalEventLoopGroup(1, factory).next();
        this.EVENT_LOOP.submit(this.INIT_FUTURE::join);
        this.setVersion(340);
        this.file = new File("ViaMCP");
        if (this.file.mkdir()) {
            this.getjLogger().info("Creating ViaMCP Folder");
        }
        Via.init(ViaManagerImpl.builder().injector(new VRInjector()).loader(new VRProviderLoader()).platform(new VRPlatform(this.file)).build());
        MappingDataLoader.enableMappingsCache();
        ((ViaManagerImpl)Via.getManager()).init();
        new VRBackwardsLoader(this.file);
        new VRRewindLoader(this.file);
        this.INIT_FUTURE.complete(null);
    }

    public Logger getjLogger() {
        return this.jLogger;
    }

    public CompletableFuture<Void> getInitFuture() {
        return this.INIT_FUTURE;
    }

    public ExecutorService getAsyncExecutor() {
        return this.ASYNC_EXEC;
    }

    public EventLoop getEventLoop() {
        return this.EVENT_LOOP;
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

