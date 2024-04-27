/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.concurrent.Future
 *  io.netty.util.concurrent.GenericFutureListener
 *  org.apache.logging.log4j.LogManager
 */
package de.enzaxd.viaforge.platform;

import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.configuration.ConfigurationProvider;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import com.viaversion.viaversion.api.platform.PlatformTask;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import de.enzaxd.viaforge.ViaForge;
import de.enzaxd.viaforge.platform.ViaAPI;
import de.enzaxd.viaforge.platform.ViaConfig;
import de.enzaxd.viaforge.util.FutureTaskId;
import de.enzaxd.viaforge.util.JLoggerToLog4j;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.File;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

public class Platform
implements ViaPlatform<UUID> {
    private final Logger logger = new JLoggerToLog4j(LogManager.getLogger((String)"ViaVersion"));
    private final ViaConfig config;
    private final File dataFolder;
    private final com.viaversion.viaversion.api.ViaAPI api;

    public Platform(File dataFolder) {
        Path configDir = dataFolder.toPath().resolve("ViaVersion");
        this.config = new ViaConfig(configDir.resolve("viaversion.yml").toFile());
        this.dataFolder = configDir.toFile();
        this.api = new ViaAPI();
    }

    public static String legacyToJson(String legacy) {
        return (String)GsonComponentSerializer.gson().serialize(LegacyComponentSerializer.legacySection().deserialize(legacy));
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public String getPlatformName() {
        return "ViaForge";
    }

    @Override
    public String getPlatformVersion() {
        return "47";
    }

    @Override
    public String getPluginVersion() {
        return "4.0.0";
    }

    @Override
    public FutureTaskId runAsync(Runnable runnable) {
        return new FutureTaskId((java.util.concurrent.Future<?>)((Object)CompletableFuture.runAsync(runnable, ViaForge.getInstance().getAsyncExecutor()).exceptionally(throwable -> {
            if (!(throwable instanceof CancellationException)) {
                throwable.printStackTrace();
            }
            return null;
        })));
    }

    @Override
    public FutureTaskId runSync(Runnable runnable) {
        return new FutureTaskId((java.util.concurrent.Future<?>)ViaForge.getInstance().getEventLoop().submit(runnable).addListener(this.errorLogger()));
    }

    @Override
    public PlatformTask runSync(Runnable runnable, long ticks) {
        return new FutureTaskId((java.util.concurrent.Future<?>)ViaForge.getInstance().getEventLoop().schedule(() -> this.runSync(runnable), ticks * 50L, TimeUnit.MILLISECONDS).addListener(this.errorLogger()));
    }

    @Override
    public PlatformTask runRepeatingSync(Runnable runnable, long ticks) {
        return new FutureTaskId((java.util.concurrent.Future<?>)ViaForge.getInstance().getEventLoop().scheduleAtFixedRate(() -> this.runSync(runnable), 0L, ticks * 50L, TimeUnit.MILLISECONDS).addListener(this.errorLogger()));
    }

    private <T extends Future<?>> GenericFutureListener<T> errorLogger() {
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
    public void sendMessage(UUID uuid, String s) {
    }

    @Override
    public boolean kickPlayer(UUID uuid, String s) {
        return false;
    }

    @Override
    public boolean isPluginEnabled() {
        return true;
    }

    @Override
    public com.viaversion.viaversion.api.ViaAPI getApi() {
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
    }

    @Override
    public JsonObject getDump() {
        JsonObject platformSpecific = new JsonObject();
        return platformSpecific;
    }

    @Override
    public boolean isOldClientsAllowed() {
        return true;
    }
}

