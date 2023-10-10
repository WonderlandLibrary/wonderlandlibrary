// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion;

import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import java.util.Iterator;
import com.viaversion.viaversion.velocity.service.ProtocolDetectorService;
import com.viaversion.viaversion.util.GsonUtil;
import java.util.List;
import com.velocitypowered.api.plugin.PluginContainer;
import com.viaversion.viaversion.dump.PluginInfo;
import java.util.ArrayList;
import com.viaversion.viaversion.libs.gson.JsonObject;
import java.io.File;
import com.viaversion.viaversion.api.configuration.ConfigurationProvider;
import net.kyori.adventure.text.Component;
import java.util.UUID;
import java.util.function.Function;
import com.viaversion.viaversion.velocity.command.VelocityCommandSender;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.velocity.platform.VelocityViaTask;
import java.util.concurrent.TimeUnit;
import com.viaversion.viaversion.api.platform.PlatformTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.api.ViaManager;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.velocity.platform.VelocityViaInjector;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import com.viaversion.viaversion.velocity.platform.VelocityViaLoader;
import com.viaversion.viaversion.commands.ViaCommandHandler;
import com.viaversion.viaversion.velocity.util.LoggerWrapper;
import com.velocitypowered.api.command.Command;
import com.viaversion.viaversion.velocity.command.VelocityCommandHandler;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.viaversion.viaversion.velocity.platform.VelocityViaConfig;
import com.viaversion.viaversion.velocity.platform.VelocityViaAPI;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import java.nio.file.Path;
import org.slf4j.Logger;
import com.google.inject.Inject;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.viaversion.viaversion.api.platform.ViaPlatform;

@Plugin(id = "viaversion", name = "ViaVersion", version = "4.1.2-SNAPSHOT", authors = { "_MylesC", "creeper123123321", "Gerrygames", "kennytv", "Matsv" }, description = "Allow newer Minecraft versions to connect to an older server version.", url = "https://viaversion.com")
public class VelocityPlugin implements ViaPlatform<Player>
{
    public static final LegacyComponentSerializer COMPONENT_SERIALIZER;
    public static ProxyServer PROXY;
    @Inject
    private ProxyServer proxy;
    @Inject
    private Logger loggerslf4j;
    @Inject
    @DataDirectory
    private Path configDir;
    private VelocityViaAPI api;
    private java.util.logging.Logger logger;
    private VelocityViaConfig conf;
    
    @Subscribe
    public void onProxyInit(final ProxyInitializeEvent e) {
        if (!this.hasConnectionEvent()) {
            final Logger logger = this.loggerslf4j;
            logger.error("      / \\");
            logger.error("     /   \\");
            logger.error("    /  |  \\");
            logger.error("   /   |   \\        VELOCITY 3.0.0 IS REQUIRED");
            logger.error("  /         \\   VIAVERSION WILL NOT WORK AS INTENDED");
            logger.error(" /     o     \\");
            logger.error("/_____________\\");
        }
        VelocityPlugin.PROXY = this.proxy;
        final VelocityCommandHandler commandHandler = new VelocityCommandHandler();
        VelocityPlugin.PROXY.getCommandManager().register("viaver", (Command)commandHandler, new String[] { "vvvelocity", "viaversion" });
        this.api = new VelocityViaAPI();
        this.conf = new VelocityViaConfig(this.configDir.toFile());
        this.logger = new LoggerWrapper(this.loggerslf4j);
        Via.init(ViaManagerImpl.builder().platform(this).commandHandler(commandHandler).loader(new VelocityViaLoader()).injector(new VelocityViaInjector()).build());
        if (this.proxy.getPluginManager().getPlugin("viabackwards").isPresent()) {
            MappingDataLoader.enableMappingsCache();
        }
    }
    
    @Subscribe(order = PostOrder.LAST)
    public void onProxyLateInit(final ProxyInitializeEvent e) {
        ((ViaManagerImpl)Via.getManager()).init();
    }
    
    @Override
    public String getPlatformName() {
        final String proxyImpl = ProxyServer.class.getPackage().getImplementationTitle();
        return (proxyImpl != null) ? proxyImpl : "Velocity";
    }
    
    @Override
    public String getPlatformVersion() {
        final String version = ProxyServer.class.getPackage().getImplementationVersion();
        return (version != null) ? version : "Unknown";
    }
    
    @Override
    public boolean isProxy() {
        return true;
    }
    
    @Override
    public String getPluginVersion() {
        return "4.1.2-SNAPSHOT";
    }
    
    @Override
    public PlatformTask runAsync(final Runnable runnable) {
        return this.runSync(runnable);
    }
    
    @Override
    public PlatformTask runSync(final Runnable runnable) {
        return this.runSync(runnable, 0L);
    }
    
    @Override
    public PlatformTask runSync(final Runnable runnable, final long ticks) {
        return new VelocityViaTask(VelocityPlugin.PROXY.getScheduler().buildTask((Object)this, runnable).delay(ticks * 50L, TimeUnit.MILLISECONDS).schedule());
    }
    
    @Override
    public PlatformTask runRepeatingSync(final Runnable runnable, final long ticks) {
        return new VelocityViaTask(VelocityPlugin.PROXY.getScheduler().buildTask((Object)this, runnable).repeat(ticks * 50L, TimeUnit.MILLISECONDS).schedule());
    }
    
    @Override
    public ViaCommandSender[] getOnlinePlayers() {
        return (ViaCommandSender[])VelocityPlugin.PROXY.getAllPlayers().stream().map(VelocityCommandSender::new).toArray(ViaCommandSender[]::new);
    }
    
    @Override
    public void sendMessage(final UUID uuid, final String message) {
        VelocityPlugin.PROXY.getPlayer(uuid).ifPresent(player -> player.sendMessage((Component)VelocityPlugin.COMPONENT_SERIALIZER.deserialize(message)));
    }
    
    @Override
    public boolean kickPlayer(final UUID uuid, final String message) {
        return VelocityPlugin.PROXY.getPlayer(uuid).map(it -> {
            it.disconnect((Component)LegacyComponentSerializer.legacySection().deserialize(message));
            return true;
        }).orElse(false);
    }
    
    @Override
    public boolean isPluginEnabled() {
        return true;
    }
    
    @Override
    public ConfigurationProvider getConfigurationProvider() {
        return this.conf;
    }
    
    @Override
    public File getDataFolder() {
        return this.configDir.toFile();
    }
    
    @Override
    public VelocityViaAPI getApi() {
        return this.api;
    }
    
    @Override
    public VelocityViaConfig getConf() {
        return this.conf;
    }
    
    @Override
    public void onReload() {
    }
    
    @Override
    public JsonObject getDump() {
        final JsonObject extra = new JsonObject();
        final List<PluginInfo> plugins = new ArrayList<PluginInfo>();
        for (final PluginContainer p : VelocityPlugin.PROXY.getPluginManager().getPlugins()) {
            plugins.add(new PluginInfo(true, p.getDescription().getName().orElse(p.getDescription().getId()), p.getDescription().getVersion().orElse("Unknown Version"), p.getInstance().isPresent() ? p.getInstance().get().getClass().getCanonicalName() : "Unknown", p.getDescription().getAuthors()));
        }
        extra.add("plugins", GsonUtil.getGson().toJsonTree(plugins));
        extra.add("servers", GsonUtil.getGson().toJsonTree(ProtocolDetectorService.getDetectedIds()));
        return extra;
    }
    
    @Override
    public boolean isOldClientsAllowed() {
        return true;
    }
    
    @Override
    public java.util.logging.Logger getLogger() {
        return this.logger;
    }
    
    private boolean hasConnectionEvent() {
        try {
            Class.forName("com.velocitypowered.proxy.protocol.VelocityConnectionEvent");
            return true;
        }
        catch (ClassNotFoundException ignored) {
            return false;
        }
    }
    
    static {
        COMPONENT_SERIALIZER = LegacyComponentSerializer.builder().character('§').extractUrls().build();
    }
}
