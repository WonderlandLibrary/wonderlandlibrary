// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.com.viamcp;

import java.util.concurrent.ThreadFactory;
import vip.Resolute.com.viamcp.loader.VRRewindLoader;
import vip.Resolute.com.viamcp.loader.VRBackwardsLoader;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.api.ViaManager;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import vip.Resolute.com.viamcp.platform.VRPlatform;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import vip.Resolute.com.viamcp.loader.VRProviderLoader;
import com.viaversion.viaversion.api.platform.ViaInjector;
import vip.Resolute.com.viamcp.platform.VRInjector;
import com.viaversion.viaversion.ViaManagerImpl;
import io.netty.channel.local.LocalEventLoopGroup;
import java.util.concurrent.Executors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import vip.Resolute.com.viamcp.utils.JLoggerToLog4j;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import io.netty.channel.EventLoop;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class ViaMCP
{
    public static final int PROTOCOL_VERSION = 47;
    private static final ViaMCP instance;
    private final Logger jLogger;
    private final CompletableFuture<Void> INIT_FUTURE;
    private ExecutorService ASYNC_EXEC;
    private EventLoop EVENT_LOOP;
    private File file;
    private int version;
    private String lastServer;
    
    public ViaMCP() {
        this.jLogger = new JLoggerToLog4j(LogManager.getLogger("ViaMCP"));
        this.INIT_FUTURE = new CompletableFuture<Void>();
    }
    
    public static ViaMCP getInstance() {
        return ViaMCP.instance;
    }
    
    public void start() {
        final ThreadFactory factory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ViaMCP-%d").build();
        this.ASYNC_EXEC = Executors.newFixedThreadPool(8, factory);
        (this.EVENT_LOOP = new LocalEventLoopGroup(1, factory).next()).submit(this.INIT_FUTURE::join);
        this.setVersion(47);
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
    
    public void setVersion(final int version) {
        this.version = version;
    }
    
    public void setFile(final File file) {
        this.file = file;
    }
    
    public void setLastServer(final String lastServer) {
        this.lastServer = lastServer;
    }
    
    static {
        instance = new ViaMCP();
    }
}
