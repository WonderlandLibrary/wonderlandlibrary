// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion;

import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import org.spongepowered.plugin.metadata.PluginMetadata;
import com.viaversion.viaversion.util.GsonUtil;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import org.spongepowered.plugin.metadata.model.PluginContributor;
import java.util.List;
import com.viaversion.viaversion.dump.PluginInfo;
import java.util.ArrayList;
import com.viaversion.viaversion.libs.gson.JsonObject;
import java.io.File;
import com.viaversion.viaversion.api.configuration.ConfigurationProvider;
import net.kyori.adventure.text.Component;
import java.util.UUID;
import java.util.Iterator;
import java.util.Collection;
import com.viaversion.viaversion.sponge.commands.SpongePlayer;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import org.spongepowered.api.util.Ticks;
import com.viaversion.viaversion.sponge.platform.SpongeViaTask;
import org.spongepowered.api.scheduler.Task;
import com.viaversion.viaversion.api.platform.PlatformTask;
import org.spongepowered.api.Platform;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.registrar.CommandRegistrar;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.event.Listener;
import com.viaversion.viaversion.api.ViaManager;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import com.viaversion.viaversion.sponge.platform.SpongeViaLoader;
import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.sponge.platform.SpongeViaInjector;
import com.viaversion.viaversion.commands.ViaCommandHandler;
import com.viaversion.viaversion.sponge.commands.SpongeCommandHandler;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import com.viaversion.viaversion.sponge.util.LoggerWrapper;
import org.spongepowered.api.config.ConfigDir;
import com.google.inject.Inject;
import java.nio.file.Path;
import com.viaversion.viaversion.sponge.platform.SpongeViaConfig;
import java.util.logging.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.plugin.PluginContainer;
import com.viaversion.viaversion.sponge.platform.SpongeViaAPI;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.spongepowered.plugin.builtin.jvm.Plugin;
import org.spongepowered.api.entity.living.player.Player;
import com.viaversion.viaversion.api.platform.ViaPlatform;

@Plugin("viaversion")
public class SpongePlugin implements ViaPlatform<Player>
{
    public static final LegacyComponentSerializer LEGACY_SERIALIZER;
    private final SpongeViaAPI api;
    private final PluginContainer container;
    private final Game game;
    private final Logger logger;
    private SpongeViaConfig conf;
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;
    
    @Inject
    SpongePlugin(final PluginContainer container, final Game game, final org.apache.logging.log4j.Logger logger) {
        this.api = new SpongeViaAPI();
        this.container = container;
        this.game = game;
        this.logger = new LoggerWrapper(logger);
    }
    
    @Listener
    public void constructPlugin(final ConstructPluginEvent event) {
        this.conf = new SpongeViaConfig(this.configDir.toFile());
        this.logger.info("ViaVersion " + this.getPluginVersion() + " is now loaded!");
        Via.init(ViaManagerImpl.builder().platform(this).commandHandler(new SpongeCommandHandler()).injector(new SpongeViaInjector()).loader(new SpongeViaLoader(this)).build());
    }
    
    @Listener
    public void onServerStart(final StartingEngineEvent<Server> event) {
        Sponge.server().commandManager().registrar((Class)Command.Raw.class).get().register(this.container, (Object)Via.getManager().getCommandHandler(), "viaversion", new String[] { "viaver", "vvsponge" });
        if (this.game.pluginManager().plugin("viabackwards").isPresent()) {
            MappingDataLoader.enableMappingsCache();
        }
        this.logger.info("ViaVersion is injecting!");
        ((ViaManagerImpl)Via.getManager()).init();
    }
    
    @Listener
    public void onServerStop(final StoppingEngineEvent<Server> event) {
        ((ViaManagerImpl)Via.getManager()).destroy();
    }
    
    @Override
    public String getPlatformName() {
        return this.game.platform().container(Platform.Component.IMPLEMENTATION).metadata().name().orElse("unknown");
    }
    
    @Override
    public String getPlatformVersion() {
        return this.game.platform().container(Platform.Component.IMPLEMENTATION).metadata().version().toString();
    }
    
    @Override
    public String getPluginVersion() {
        return this.container.metadata().version().toString();
    }
    
    @Override
    public PlatformTask runAsync(final Runnable runnable) {
        final Task task = Task.builder().plugin(this.container).execute(runnable).build();
        return new SpongeViaTask(this.game.asyncScheduler().submit(task));
    }
    
    @Override
    public PlatformTask runSync(final Runnable runnable) {
        final Task task = Task.builder().plugin(this.container).execute(runnable).build();
        return new SpongeViaTask(this.game.server().scheduler().submit(task));
    }
    
    @Override
    public PlatformTask runSync(final Runnable runnable, final long ticks) {
        final Task task = Task.builder().plugin(this.container).execute(runnable).delay(Ticks.of(ticks)).build();
        return new SpongeViaTask(this.game.server().scheduler().submit(task));
    }
    
    @Override
    public PlatformTask runRepeatingSync(final Runnable runnable, final long ticks) {
        final Task task = Task.builder().plugin(this.container).execute(runnable).interval(Ticks.of(ticks)).build();
        return new SpongeViaTask(this.game.server().scheduler().submit(task));
    }
    
    @Override
    public ViaCommandSender[] getOnlinePlayers() {
        final Collection<ServerPlayer> players = this.game.server().onlinePlayers();
        final ViaCommandSender[] array = new ViaCommandSender[players.size()];
        int i = 0;
        for (final ServerPlayer player : players) {
            array[i++] = new SpongePlayer(player);
        }
        return array;
    }
    
    @Override
    public void sendMessage(final UUID uuid, final String message) {
        this.game.server().player(uuid).ifPresent(player -> player.sendMessage((Component)SpongePlugin.LEGACY_SERIALIZER.deserialize(message)));
    }
    
    @Override
    public boolean kickPlayer(final UUID uuid, final String message) {
        return this.game.server().player(uuid).map(player -> {
            player.kick((Component)LegacyComponentSerializer.legacySection().deserialize(message));
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
    public void onReload() {
        this.logger.severe("ViaVersion is already loaded, this should work fine. If you get any console errors, try rebooting.");
    }
    
    @Override
    public JsonObject getDump() {
        final JsonObject platformSpecific = new JsonObject();
        final List<PluginInfo> plugins = new ArrayList<PluginInfo>();
        for (final PluginContainer plugin : this.game.pluginManager().plugins()) {
            final PluginMetadata metadata = plugin.metadata();
            plugins.add(new PluginInfo(true, metadata.name().orElse("Unknown"), metadata.version().toString(), plugin.instance().getClass().getCanonicalName(), (List<String>)metadata.contributors().stream().map(PluginContributor::name).collect(Collectors.toList())));
        }
        platformSpecific.add("plugins", GsonUtil.getGson().toJsonTree(plugins));
        return platformSpecific;
    }
    
    @Override
    public boolean isOldClientsAllowed() {
        return true;
    }
    
    @Override
    public boolean hasPlugin(final String name) {
        return this.game.pluginManager().plugin(name).isPresent();
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
    
    public PluginContainer container() {
        return this.container;
    }
    
    static {
        LEGACY_SERIALIZER = LegacyComponentSerializer.builder().extractUrls().build();
    }
}
