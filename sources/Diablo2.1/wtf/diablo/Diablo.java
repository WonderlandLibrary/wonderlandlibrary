package wtf.diablo;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import wtf.diablo.commands.CommandManager;
import wtf.diablo.config.ConfigManager;
import wtf.diablo.events.impl.KeyEvent;
import wtf.diablo.gui.notifications.Notification;
import wtf.diablo.gui.notifications.NotificationManager;
import wtf.diablo.module.Module;
import wtf.diablo.module.ModuleManager;
import wtf.diablo.utils.discord.DiscordRPCUtil;
import wtf.diablo.utils.hypixel.HypixelStatus;
import wtf.diablo.utils.logging.LoggingUtil;
import wtf.diablo.utils.render.Base64ImageLocation;

import java.io.File;
import java.util.Objects;

/*
    Brain cancer made by Vince, Kyle, Ai, and Qoft (uwu)

    *hopefully this com doesn't die*
    2022 - Minecraft Cheating LLC :alah-prey:
 */

public class Diablo {
    public static Diablo Instance = new Diablo();

    public static String name;
    public static HypixelStatus hypixelStatus = new HypixelStatus();
    private static File dir;
    public static double version;
    public static String authors;
    public static ClientEnum buildType;
    public static EventBus eventBus;
    public static ModuleManager moduleManager;
    public static ConfigManager configManager;
    private static CommandManager commandManager;
    public static NotificationManager notificationManager;
    public static ResourceLocation profilePicture;
    public static Base64ImageLocation avatarLocation;

    public static void startClient() {
        setupMinecraftVisuals();

        System.out.println("Vince and like likes watching porn");

        dir = new File(Minecraft.getMinecraft().mcDataDir, name);

        if(!Objects.equals(System.getProperty("os.name"), "Linux"))
            DiscordRPCUtil.updateRPC();

        commandManager = new CommandManager();

        notificationManager = new NotificationManager();

        configManager = new ConfigManager(new File(dir, "configs"));

        moduleManager = new ModuleManager();

        LoggingUtil.log("Started " + name + " on build " + version);
        LoggingUtil.infoLog("This is a " + buildType + " build!");

        LoggingUtil.log("Loading Default Config");
        configManager.loadConfig("default",true);
        LoggingUtil.log("Getting profile picture");
//        Diablo.avatarLocation = RenderUtil.getDiscordProfilePicture(intentAccount.discord_id);


    }

    public void setupClient() {
        name = "Diablo";
        version = 2.1;
        authors = "Vince, Kyle, Qoft, Ai";
        buildType = ClientEnum.Release;

        eventBus = new EventBus(name);
        eventBus.register(this);


    }

    public static void setupMinecraftVisuals() {
        Display.setTitle(name + " " + version);
    }

    public void addNotification(Notification notification) {
        notificationManager.notifications.add(notification);
    }

    @Subscribe
    public void onKey(KeyEvent e) {
        ModuleManager.getModules().stream().filter(module -> module.getKey() == e.getKey()).forEach(Module::toggle);
    }

    public ResourceLocation getDiscordPfp() {
        return new ResourceLocation(dir + "/discordPfp.png");
    }
    public static Diablo getInstance() {
        return Instance;
    }

    public static EventBus getEventBus() {
        return eventBus;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public static File getDir() {
        return dir;
    }

    public static String getStringDir() {
        return dir.getName();
    }

    public ResourceLocation getdcpfp() {
        return profilePicture;
    }
}


