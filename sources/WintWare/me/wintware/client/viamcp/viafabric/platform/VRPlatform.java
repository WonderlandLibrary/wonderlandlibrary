/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.util.concurrent.Future
 *  io.netty.util.concurrent.GenericFutureListener
 *  org.apache.logging.log4j.LogManager
 */
package me.wintware.client.viamcp.viafabric.platform;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.File;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import me.wintware.client.viamcp.viafabric.ViaFabric;
import me.wintware.client.viamcp.viafabric.platform.VRConnectionManager;
import me.wintware.client.viamcp.viafabric.platform.VRViaAPI;
import me.wintware.client.viamcp.viafabric.platform.VRViaConfig;
import me.wintware.client.viamcp.viafabric.util.FutureTaskId;
import me.wintware.client.viamcp.viafabric.util.JLoggerToLog4j;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import us.myles.ViaVersion.api.ViaAPI;
import us.myles.ViaVersion.api.ViaVersionConfig;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import us.myles.ViaVersion.api.configuration.ConfigurationProvider;
import us.myles.ViaVersion.api.platform.TaskId;
import us.myles.ViaVersion.api.platform.ViaConnectionManager;
import us.myles.ViaVersion.api.platform.ViaPlatform;
import us.myles.viaversion.libs.bungeecordchat.api.chat.TextComponent;
import us.myles.viaversion.libs.bungeecordchat.chat.ComponentSerializer;
import us.myles.viaversion.libs.gson.JsonObject;

public class VRPlatform
implements ViaPlatform<UUID> {
    private final Logger logger = new JLoggerToLog4j(LogManager.getLogger("WintWare"));
    private final VRViaConfig config;
    private final File dataFolder;
    private final ViaConnectionManager connectionManager;
    private final ViaAPI<UUID> api;

    public VRPlatform() {
        Path configDir = Minecraft.getMinecraft().mcDataDir.toPath().resolve("WintWare/WintMcp");
        this.config = new VRViaConfig(configDir.resolve("wintmcp.yml").toFile());
        this.dataFolder = configDir.toFile();
        this.connectionManager = new VRConnectionManager();
        this.api = new VRViaAPI();
    }

    public static MinecraftServer getServer() {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning()) {
            return null;
        }
        return MinecraftServer.instance.getServer();
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public String getPlatformName() {
        return "WintWare";
    }

    @Override
    public String getPlatformVersion() {
        return ViaFabric.getVersion();
    }

    @Override
    public String getPluginVersion() {
        return "3.3.0";
    }

    @Override
    public TaskId runAsync(Runnable runnable) {
        return new FutureTaskId((java.util.concurrent.Future<?>) CompletableFuture.runAsync(runnable, ViaFabric.ASYNC_EXECUTOR).exceptionally(throwable -> {
            if (!(throwable instanceof CancellationException)) {
                throwable.printStackTrace();
            }
            return null;
        }));
    }

    @Override
    public TaskId runSync(Runnable runnable) {
        if (VRPlatform.getServer() != null) {
            return this.runServerSync(runnable);
        }
        return this.runEventLoop(runnable);
    }

    private TaskId runServerSync(Runnable runnable) {
        return new FutureTaskId(CompletableFuture.runAsync(runnable, it -> VRPlatform.getServer().callFromMainThread(() -> {
            it.run();
            return null;
        })));
    }

    private TaskId runEventLoop(Runnable runnable) {
        return new FutureTaskId(ViaFabric.EVENT_LOOP.submit(runnable).addListener(this.errorLogger()));
    }

    @Override
    public TaskId runSync(Runnable runnable, Long ticks) {
        return new FutureTaskId(ViaFabric.EVENT_LOOP.schedule(() -> this.runSync(runnable), ticks * 50L, TimeUnit.MILLISECONDS).addListener(this.errorLogger()));
    }

    @Override
    public TaskId runRepeatingSync(Runnable runnable, Long ticks) {
        return new FutureTaskId(ViaFabric.EVENT_LOOP.scheduleAtFixedRate(() -> this.runSync(runnable), 0L, ticks * 50L, TimeUnit.MILLISECONDS).addListener(this.errorLogger()));
    }

    private <T extends Future<?>> GenericFutureListener<T> errorLogger() {
        return future -> {
            if (!future.isCancelled() && future.cause() != null) {
                future.cause().printStackTrace();
            }
        };
    }

    @Override
    public void cancelTask(TaskId taskId) {
        if (taskId instanceof FutureTaskId) {
            ((FutureTaskId)taskId).getObject().cancel(false);
        }
    }

    @Override
    public ViaCommandSender[] getOnlinePlayers() {
        return new ViaCommandSender[0];
    }

    @Override
    public void sendMessage(UUID uuid, String s) {
    }

    @Override
    public boolean kickPlayer(UUID uuid, String s) {
        return this.kickServer(uuid, s);
    }

    private boolean kickServer(UUID uuid, String s) {
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
    }

    @Override
    public JsonObject getDump() {
        return new JsonObject();
    }

    @Override
    public boolean isOldClientsAllowed() {
        return true;
    }

    @Override
    public ViaConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

    private String legacyToJson(String legacy) {
        return ComponentSerializer.toString(TextComponent.fromLegacyText(legacy));
    }
}

