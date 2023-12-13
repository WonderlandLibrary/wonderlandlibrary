package vip.radium;

import com.thealtening.auth.service.AlteningServiceType;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.bus.impl.EventBus;
import vip.radium.alt.AltManager;
import vip.radium.command.CommandManager;
import vip.radium.config.ConfigManager;
import vip.radium.event.Event;
import vip.radium.event.impl.game.ClientStartupEvent;
import vip.radium.player.PlayerManager;
import vip.radium.gui.csgo.SkeetUI;
import vip.radium.gui.font.FontManager;
import vip.radium.module.ModuleManager;
import vip.radium.notification.NotificationManager;

import static org.lwjgl.opengl.GL11.*;

/**
 * Stop inv manager from throwing out main sword
 * tabui
 * finish enemy manager implementation (killaura priority etc)
 * basic modules + commands need to be added
 * Fix gui expandable boxes not being able to be clicked if outside module box
 * Config in da gui
 * 2 block step
 * Fix scaffold silent or ghostblock when not jumping
 * southside chams mode
 * Armor dura on target hud and esps
 * add forceground to autopotuion and module checks (fly scaffold etc)
 * child options do not save
 * southside combat mods
 * Auto load config on startup
 * module/property aliases
 */

public final class RadiumClient {
    private static final RadiumClient INSTANCE = new RadiumClient();
    private EventBus<Event> eventBus;
    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private PlayerManager playerManager;
    private NotificationManager notificationManager;
    private CommandManager commandManager;
    private AltManager altManager;
    public static final String NAME = "Radium";
    public static final String VERSION = "v1.4.0";

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public AltManager getAltManager() {
        return altManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CommandManager getCommandHandler() {
        return commandManager;
    }

    private RadiumClient() {
        getEventBus().subscribe(this);
    }

    @EventLink
    public final Listener<ClientStartupEvent> onClientStart = e -> {
        setGLHints();
        FontManager.initTextures();

        configManager = new ConfigManager();

        playerManager = new PlayerManager();

        altManager = new AltManager();

        moduleManager = new ModuleManager();

        SkeetUI.init();

        notificationManager = new NotificationManager();

        commandManager = new CommandManager();

        moduleManager.postInit();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> altManager.getAlteningAuth()
                .updateService(AlteningServiceType.MOJANG)));
    };

    private static void setGLHints() {
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
    }

    public EventBus<Event> getEventBus() {
        if (eventBus == null) {
            eventBus = new EventBus<>();
        }

        return eventBus;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public static RadiumClient getInstance() {
        return INSTANCE;
    }

}
