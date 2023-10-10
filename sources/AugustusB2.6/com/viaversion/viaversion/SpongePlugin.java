// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion;

import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import com.viaversion.viaversion.util.GsonUtil;
import java.util.List;
import com.viaversion.viaversion.dump.PluginInfo;
import java.util.ArrayList;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.api.configuration.ConfigurationProvider;
import org.spongepowered.api.text.serializer.TextSerializers;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import java.util.UUID;
import java.util.Iterator;
import org.spongepowered.api.command.CommandSource;
import com.viaversion.viaversion.sponge.commands.SpongeCommandSender;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.sponge.platform.SpongeViaTask;
import org.spongepowered.api.scheduler.Task;
import com.viaversion.viaversion.api.platform.PlatformTask;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.Listener;
import com.viaversion.viaversion.api.ViaManager;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import com.viaversion.viaversion.sponge.platform.SpongeViaLoader;
import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.sponge.platform.SpongeViaInjector;
import com.viaversion.viaversion.commands.ViaCommandHandler;
import org.spongepowered.api.command.CommandCallable;
import com.viaversion.viaversion.sponge.commands.SpongeCommandHandler;
import com.viaversion.viaversion.sponge.util.LoggerWrapper;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import java.util.logging.Logger;
import com.viaversion.viaversion.sponge.platform.SpongeViaConfig;
import com.viaversion.viaversion.sponge.platform.SpongeViaAPI;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.spongepowered.api.config.DefaultConfig;
import java.io.File;
import org.spongepowered.api.plugin.PluginContainer;
import com.google.inject.Inject;
import org.spongepowered.api.Game;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.entity.living.player.Player;
import com.viaversion.viaversion.api.platform.ViaPlatform;

@Plugin(id = "viaversion", name = "ViaVersion", version = "4.1.2-SNAPSHOT", authors = { "_MylesC", "creeper123123321", "Gerrygames", "kennytv", "Matsv" }, description = "Allow newer Minecraft versions to connect to an older server version.")
public class SpongePlugin implements ViaPlatform<Player>
{
    @Inject
    private Game game;
    @Inject
    private PluginContainer container;
    @Inject
    @DefaultConfig(sharedRoot = false)
    private File spongeConfig;
    public static final LegacyComponentSerializer COMPONENT_SERIALIZER;
    private final SpongeViaAPI api;
    private SpongeViaConfig conf;
    private Logger logger;
    
    public SpongePlugin() {
        this.api = new SpongeViaAPI();
    }
    
    @Listener
    public void onGameStart(final GameInitializationEvent event) {
        this.logger = new LoggerWrapper(this.container.getLogger());
        this.conf = new SpongeViaConfig(this.container, this.spongeConfig.getParentFile());
        final SpongeCommandHandler commandHandler = new SpongeCommandHandler();
        this.game.getCommandManager().register((Object)this, (CommandCallable)commandHandler, new String[] { "viaversion", "viaver", "vvsponge" });
        this.logger.info("ViaVersion " + this.getPluginVersion() + " is now loaded!");
        Via.init(ViaManagerImpl.builder().platform(this).commandHandler(commandHandler).injector(new SpongeViaInjector()).loader(new SpongeViaLoader(this)).build());
    }
    
    @Listener
    public void onServerStart(final GameAboutToStartServerEvent event) {
        if (this.game.getPluginManager().getPlugin("viabackwards").isPresent()) {
            MappingDataLoader.enableMappingsCache();
        }
        this.logger.info("ViaVersion is injecting!");
        ((ViaManagerImpl)Via.getManager()).init();
    }
    
    @Listener
    public void onServerStop(final GameStoppingServerEvent event) {
        ((ViaManagerImpl)Via.getManager()).destroy();
    }
    
    @Override
    public String getPlatformName() {
        return this.game.getPlatform().getImplementation().getName();
    }
    
    @Override
    public String getPlatformVersion() {
        return this.game.getPlatform().getImplementation().getVersion().orElse("Unknown Version");
    }
    
    @Override
    public String getPluginVersion() {
        return this.container.getVersion().orElse("Unknown Version");
    }
    
    @Override
    public PlatformTask runAsync(final Runnable runnable) {
        return new SpongeViaTask(Task.builder().execute(runnable).async().submit((Object)this));
    }
    
    @Override
    public PlatformTask runSync(final Runnable runnable) {
        return new SpongeViaTask(Task.builder().execute(runnable).submit((Object)this));
    }
    
    @Override
    public PlatformTask runSync(final Runnable runnable, final long ticks) {
        return new SpongeViaTask(Task.builder().execute(runnable).delayTicks(ticks).submit((Object)this));
    }
    
    @Override
    public PlatformTask runRepeatingSync(final Runnable runnable, final long ticks) {
        return new SpongeViaTask(Task.builder().execute(runnable).intervalTicks(ticks).submit((Object)this));
    }
    
    @Override
    public ViaCommandSender[] getOnlinePlayers() {
        final ViaCommandSender[] array = new ViaCommandSender[this.game.getServer().getOnlinePlayers().size()];
        int i = 0;
        for (final Player player : this.game.getServer().getOnlinePlayers()) {
            array[i++] = new SpongeCommandSender((CommandSource)player);
        }
        return array;
    }
    
    @Override
    public void sendMessage(final UUID uuid, final String message) {
        final String serialized = SpongePlugin.COMPONENT_SERIALIZER.serialize((Component)SpongePlugin.COMPONENT_SERIALIZER.deserialize(message));
        this.game.getServer().getPlayer(uuid).ifPresent(player -> player.sendMessage(TextSerializers.JSON.deserialize(serialized)));
    }
    
    @Override
    public boolean kickPlayer(final UUID uuid, final String message) {
        return this.game.getServer().getPlayer(uuid).map(player -> {
            player.kick(TextSerializers.formattingCode('§').deserialize(message));
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
        return this.spongeConfig.getParentFile();
    }
    
    @Override
    public void onReload() {
        this.getLogger().severe("ViaVersion is already loaded, this should work fine. If you get any console errors, try rebooting.");
    }
    
    @Override
    public JsonObject getDump() {
        final JsonObject platformSpecific = new JsonObject();
        final List<PluginInfo> plugins = new ArrayList<PluginInfo>();
        for (final PluginContainer p : this.game.getPluginManager().getPlugins()) {
            plugins.add(new PluginInfo(true, p.getName(), p.getVersion().orElse("Unknown Version"), p.getInstance().isPresent() ? p.getInstance().get().getClass().getCanonicalName() : "Unknown", p.getAuthors()));
        }
        platformSpecific.add("plugins", GsonUtil.getGson().toJsonTree(plugins));
        return platformSpecific;
    }
    
    @Override
    public boolean isOldClientsAllowed() {
        return true;
    }
    
    @Override
    public SpongeViaAPI getApi() {
        return this.api;
    }
    
    @Override
    public SpongeViaConfig getConf() {
        return this.conf;
    }
    
    @Override
    public Logger getLogger() {
        return this.logger;
    }
    
    static {
        COMPONENT_SERIALIZER = LegacyComponentSerializer.builder().character('§').extractUrls().build();
    }
}
