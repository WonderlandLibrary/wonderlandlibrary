// 
// Decompiled by Procyon v0.5.36
// 

package viamcp.platform;

import com.viaversion.viaversion.libs.kyori.adventure.text.TextComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.ComponentSerializer;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.api.configuration.ConfigurationProvider;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.concurrent.TimeUnit;
import com.viaversion.viaversion.api.platform.PlatformTask;
import java.util.concurrent.Future;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.CompletableFuture;
import viamcp.ViaMCP;
import viamcp.utils.FutureTaskId;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import java.nio.file.Path;
import viamcp.utils.JLoggerToLog4j;
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
        this.api = new MCPViaAPI();
    }
    
    public static String legacyToJson(final String legacy) {
        return ((ComponentSerializer<TextComponent, O, String>)GsonComponentSerializer.gson()).serialize(LegacyComponentSerializer.legacySection().deserialize(legacy));
    }
    
    @Override
    public Logger getLogger() {
        return this.logger;
    }
    
    @Override
    public String getPlatformName() {
        return "ViaMCP";
    }
    
    @Override
    public String getPlatformVersion() {
        return String.valueOf(47);
    }
    
    @Override
    public String getPluginVersion() {
        return "4.1.1";
    }
    
    @Override
    public FutureTaskId runAsync(final Runnable runnable) {
        return new FutureTaskId(CompletableFuture.runAsync(runnable, ViaMCP.getInstance().getAsyncExecutor()).exceptionally(throwable -> {
            if (!(throwable instanceof CancellationException)) {
                throwable.printStackTrace();
            }
            return null;
        }));
    }
    
    @Override
    public FutureTaskId runSync(final Runnable runnable) {
        return new FutureTaskId(ViaMCP.getInstance().getEventLoop().submit(runnable).addListener(this.errorLogger()));
    }
    
    @Override
    public PlatformTask runSync(final Runnable runnable, final long ticks) {
        return new FutureTaskId(ViaMCP.getInstance().getEventLoop().schedule(() -> this.runSync(runnable), ticks * 50L, TimeUnit.MILLISECONDS).addListener(this.errorLogger()));
    }
    
    @Override
    public PlatformTask runRepeatingSync(final Runnable runnable, final long ticks) {
        return new FutureTaskId(ViaMCP.getInstance().getEventLoop().scheduleAtFixedRate(() -> this.runSync(runnable), 0L, ticks * 50L, TimeUnit.MILLISECONDS).addListener(this.errorLogger()));
    }
    
    private <T extends io.netty.util.concurrent.Future<?>> GenericFutureListener<T> errorLogger() {
        return future -> {
            if (!future.isCancelled() && future.cause() != null) {
                future.cause().printStackTrace();
            }
        };
    }
    
    @Override
    public ViaCommandSender[] getOnlinePlayers() {
        return new ViaCommandSender[1337];
    }
    
    private ViaCommandSender[] getServerPlayers() {
        return new ViaCommandSender[1337];
    }
    
    @Override
    public void sendMessage(final UUID uuid, final String s) {
    }
    
    @Override
    public boolean kickPlayer(final UUID uuid, final String s) {
        return false;
    }
    
    @Override
    public boolean isPluginEnabled() {
        return true;
    }
    
    @Override
    public ViaAPI<UUID> getApi() {
        return this.api;
    }
    
    @Override
    public ViaVersionConfig getConf() {
        return this.config;
    }
    
    @Override
    public ConfigurationProvider getConfigurationProvider() {
        return this.config;
    }
    
    @Override
    public File getDataFolder() {
        return this.dataFolder;
    }
    
    @Override
    public void onReload() {
        this.logger.info("ViaVersion was reloaded? (How did that happen)");
    }
    
    @Override
    public JsonObject getDump() {
        final JsonObject platformSpecific = new JsonObject();
        return platformSpecific;
    }
    
    @Override
    public boolean isOldClientsAllowed() {
        return true;
    }
}
