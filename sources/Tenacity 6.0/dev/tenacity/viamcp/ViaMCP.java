// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.viamcp;

import java.util.concurrent.ThreadFactory;
import dev.tenacity.viamcp.loader.MCPRewindLoader;
import dev.tenacity.viamcp.loader.MCPBackwardsLoader;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.api.ViaManager;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import dev.tenacity.viamcp.platform.MCPViaPlatform;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import dev.tenacity.viamcp.loader.MCPViaLoader;
import com.viaversion.viaversion.api.platform.ViaInjector;
import dev.tenacity.viamcp.platform.MCPViaInjector;
import com.viaversion.viaversion.ViaManagerImpl;
import java.util.concurrent.Callable;
import io.netty.channel.local.LocalEventLoopGroup;
import java.util.concurrent.Executors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.tenacity.viamcp.utils.JLoggerToLog4j;
import org.apache.logging.log4j.LogManager;
import dev.tenacity.viamcp.gui.AsyncVersionSlider;
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
    public AsyncVersionSlider asyncSlider;
    
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
        (this.EVENT_LOOP = new LocalEventLoopGroup(1, factory).next()).submit((Callable)this.INIT_FUTURE::join);
        this.setVersion(47);
        this.file = new File("ViaMCP");
        if (this.file.mkdir()) {
            this.getjLogger().info("Creating ViaMCP Folder");
        }
        Via.init((ViaManager)ViaManagerImpl.builder().injector((ViaInjector)new MCPViaInjector()).loader((ViaPlatformLoader)new MCPViaLoader()).platform((ViaPlatform)new MCPViaPlatform(this.file)).build());
        MappingDataLoader.enableMappingsCache();
        ((ViaManagerImpl)Via.getManager()).init();
        new MCPBackwardsLoader(this.file);
        new MCPRewindLoader(this.file);
        this.INIT_FUTURE.complete(null);
    }
    
    public void initAsyncSlider() {
        this.asyncSlider = new AsyncVersionSlider(-1, 5, 5, 110, 20);
    }
    
    public void initAsyncSlider(final int x, final int y, final int width, final int height) {
        this.asyncSlider = new AsyncVersionSlider(-1, x, y, Math.max(width, 110), height);
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
