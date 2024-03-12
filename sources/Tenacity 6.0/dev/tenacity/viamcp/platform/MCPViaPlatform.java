// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.viamcp.platform;

import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.api.configuration.ConfigurationProvider;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import java.util.concurrent.TimeUnit;
import com.viaversion.viaversion.api.platform.PlatformTask;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.concurrent.Future;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.CompletableFuture;
import dev.tenacity.viamcp.ViaMCP;
import dev.tenacity.viamcp.utils.FutureTaskId;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import java.nio.file.Path;
import dev.tenacity.viamcp.utils.JLoggerToLog4j;
import org.apache.logging.log4j.LogManager;
import com.viaversion.viaversion.api.ViaAPI;
import java.io.File;
import java.util.logging.Logger;
import java.util.UUID;
import com.viaversion.viaversion.api.platform.ViaPlatform;

public class MCPViaPlatform implements ViaPlatform<UUID>
{
    private final Logger logger;
    private final MCPViaConfig config;
    private final File dataFolder;
    private final ViaAPI<UUID> api;
    
    public MCPViaPlatform(final File dataFolder) {
        this.logger = new JLoggerToLog4j(LogManager.getLogger("ViaVersion"));
        final Path configDir = dataFolder.toPath().resolve("ViaVersion");
        this.config = new MCPViaConfig(configDir.resolve("viaversion.yml").toFile());
        this.dataFolder = configDir.toFile();
        this.api = (ViaAPI<UUID>)new MCPViaAPI();
    }
    
    public static String legacyToJson(final String legacy) {
        return (String)GsonComponentSerializer.gson().serialize((Component)LegacyComponentSerializer.legacySection().deserialize(legacy));
    }
    
    public Logger getLogger() {
        return this.logger;
    }
    
    public String getPlatformName() {
        return "ViaMCP";
    }
    
    public String getPlatformVersion() {
        return String.valueOf(47);
    }
    
    public String getPluginVersion() {
        return "4.4.2";
    }
    
    public FutureTaskId runAsync(final Runnable runnable) {
        return new FutureTaskId(CompletableFuture.runAsync(runnable, ViaMCP.getInstance().getAsyncExecutor()).exceptionally(throwable -> {
            if (!(throwable instanceof CancellationException)) {
                throwable.printStackTrace();
            }
            return null;
        }));
    }
    
    public FutureTaskId runSync(final Runnable runnable) {
        return new FutureTaskId((Future<?>)ViaMCP.getInstance().getEventLoop().submit(runnable).addListener((GenericFutureListener)this.errorLogger()));
    }
    
    public PlatformTask runSync(final Runnable runnable, final long ticks) {
        return (PlatformTask)new FutureTaskId((Future<?>)ViaMCP.getInstance().getEventLoop().schedule(() -> this.runSync(runnable), ticks * 50L, TimeUnit.MILLISECONDS).addListener((GenericFutureListener)this.errorLogger()));
    }
    
    public PlatformTask runRepeatingSync(final Runnable runnable, final long ticks) {
        return (PlatformTask)new FutureTaskId((Future<?>)ViaMCP.getInstance().getEventLoop().scheduleAtFixedRate(() -> this.runSync(runnable), 0L, ticks * 50L, TimeUnit.MILLISECONDS).addListener((GenericFutureListener)this.errorLogger()));
    }
    
    private <T extends io.netty.util.concurrent.Future<?>> GenericFutureListener<T> errorLogger() {
        return (GenericFutureListener<T>)(future -> {
            if (!future.isCancelled() && future.cause() != null) {
                future.cause().printStackTrace();
            }
        });
    }
    
    public ViaCommandSender[] getOnlinePlayers() {
        return new ViaCommandSender[1337];
    }
    
    private ViaCommandSender[] getServerPlayers() {
        return new ViaCommandSender[1337];
    }
    
    public void sendMessage(final UUID uuid, final String s) {
    }
    
    public boolean kickPlayer(final UUID uuid, final String s) {
        return false;
    }
    
    public boolean isPluginEnabled() {
        return true;
    }
    
    public ViaAPI<UUID> getApi() {
        return this.api;
    }
    
    public ViaVersionConfig getConf() {
        return (ViaVersionConfig)this.config;
    }
    
    public ConfigurationProvider getConfigurationProvider() {
        return (ConfigurationProvider)this.config;
    }
    
    public File getDataFolder() {
        return this.dataFolder;
    }
    
    public void onReload() {
        this.logger.info("ViaVersion was reloaded? (How did that happen)");
    }
    
    public JsonObject getDump() {
        return new JsonObject();
    }
    
    public boolean isOldClientsAllowed() {
        return true;
    }
    
    public boolean hasPlugin(final String name) {
        return false;
    }
}
