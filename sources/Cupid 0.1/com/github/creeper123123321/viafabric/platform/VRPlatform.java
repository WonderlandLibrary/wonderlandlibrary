package com.github.creeper123123321.viafabric.platform;

import com.github.creeper123123321.viafabric.ViaFabric;
import com.github.creeper123123321.viafabric.util.FutureTaskId;
import com.github.creeper123123321.viafabric.util.JLoggerToLog4j;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.File;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
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

public class VRPlatform implements ViaPlatform<UUID> {
  private final Logger logger = (Logger)new JLoggerToLog4j(LogManager.getLogger("ViaVersion"));
  
  private final VRViaConfig config;
  
  private final File dataFolder;
  
  private final ViaConnectionManager connectionManager;
  
  private final ViaAPI<UUID> api;
  
  public VRPlatform() {
    Path configDir = (Minecraft.getMinecraft()).mcDataDir.toPath().resolve("ViaFabric");
    this.config = new VRViaConfig(configDir.resolve("viaversion.yml").toFile());
    this.dataFolder = configDir.toFile();
    this.connectionManager = new VRConnectionManager();
    this.api = new VRViaAPI();
  }
  
  public static MinecraftServer getServer() {
    if (!Minecraft.getMinecraft().isIntegratedServerRunning())
      return null; 
    return MinecraftServer.getServer();
  }
  
  public Logger getLogger() {
    return this.logger;
  }
  
  public String getPlatformName() {
    return "ViaFabric";
  }
  
  public String getPlatformVersion() {
    return ViaFabric.getVersion();
  }
  
  public String getPluginVersion() {
    return "3.3.0";
  }
  
  public TaskId runAsync(Runnable runnable) {
    return (TaskId)new FutureTaskId(
        CompletableFuture.runAsync(runnable, ViaFabric.ASYNC_EXECUTOR)
        .exceptionally(throwable -> {
            if (!(throwable instanceof java.util.concurrent.CancellationException))
              throwable.printStackTrace(); 
            return null;
          }));
  }
  
  public TaskId runSync(Runnable runnable) {
    if (getServer() != null)
      return runServerSync(runnable); 
    return runEventLoop(runnable);
  }
  
  private TaskId runServerSync(Runnable runnable) {
    return (TaskId)new FutureTaskId(CompletableFuture.runAsync(runnable, it -> getServer().callFromMainThread(())));
  }
  
  private TaskId runEventLoop(Runnable runnable) {
    return (TaskId)new FutureTaskId((Future)ViaFabric.EVENT_LOOP
        
        .submit(runnable)
        .addListener(errorLogger()));
  }
  
  public TaskId runSync(Runnable runnable, Long ticks) {
    return (TaskId)new FutureTaskId((Future)ViaFabric.EVENT_LOOP
        
        .schedule(() -> runSync(runnable), ticks.longValue() * 50L, TimeUnit.MILLISECONDS)
        .addListener(errorLogger()));
  }
  
  public TaskId runRepeatingSync(Runnable runnable, Long ticks) {
    return (TaskId)new FutureTaskId((Future)ViaFabric.EVENT_LOOP
        
        .scheduleAtFixedRate(() -> runSync(runnable), 0L, ticks.longValue() * 50L, TimeUnit.MILLISECONDS)
        .addListener(errorLogger()));
  }
  
  private <T extends Future<?>> GenericFutureListener<T> errorLogger() {
    return future -> {
        if (!future.isCancelled() && future.cause() != null)
          future.cause().printStackTrace(); 
      };
  }
  
  public void cancelTask(TaskId taskId) {
    if (taskId instanceof FutureTaskId)
      ((FutureTaskId)taskId).getObject().cancel(false); 
  }
  
  public ViaCommandSender[] getOnlinePlayers() {
    return new ViaCommandSender[0];
  }
  
  public void sendMessage(UUID uuid, String s) {}
  
  public boolean kickPlayer(UUID uuid, String s) {
    return kickServer(uuid, s);
  }
  
  private boolean kickServer(UUID uuid, String s) {
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
  
  public void onReload() {}
  
  public JsonObject getDump() {
    return new JsonObject();
  }
  
  public boolean isOldClientsAllowed() {
    return true;
  }
  
  public ViaConnectionManager getConnectionManager() {
    return this.connectionManager;
  }
  
  private String legacyToJson(String legacy) {
    return ComponentSerializer.toString(TextComponent.fromLegacyText(legacy));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\github\creeper123123321\viafabric\platform\VRPlatform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */