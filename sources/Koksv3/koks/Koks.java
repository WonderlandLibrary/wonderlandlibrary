package koks;

import koks.api.interfaces.Wrapper;
import koks.api.util.DiscordUtil;
import koks.api.util.TrayUtil;
import koks.manager.changelog.ChangelogManager;
import koks.manager.command.CommandManager;
import koks.manager.config.ConfigSystem;
import koks.manager.file.FileManager;
import koks.manager.friends.FriendManager;
import koks.gui.clickgui.periodic.ClickGUIPSE;
import koks.gui.tabgui.TabGUI;
import koks.gui.clickgui.normal.ClickGUI;
import koks.api.settings.SettingsManager;

import java.awt.*;

import koks.manager.cl.CLManager;
import koks.manager.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.Main;
import org.lwjgl.opengl.Display;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 19:20
 */
public class Koks implements Wrapper {

    private static final Koks KOKS;

    public final String NAME = "Koks";
    public final String PREFIX = "§c" + NAME + " §7>> ";
    public final String VERSION = "3.0.0";
    public final String[] AUTHORS = new String[]{"DasDirt", "Deleteboys","Kroko", "Phantom"};
    public long initTime = System.currentTimeMillis();

    public String alteningApiKey = "";

    static {
        KOKS = new Koks();
    }

    public Color clientColor = Color.ORANGE;

    public boolean isNew = false, isUsingProxy = false;

    public String currentProxy;
    public int currentProxyPort;

    public static Koks getKoks() {
        return KOKS;
    }

    public SettingsManager settingsManager;
    public volatile Minecraft mc = Minecraft.getMinecraft();

    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public FileManager fileManager;
    public TabGUI tabGUI;
    public ConfigSystem configSystem;
    public CLManager CLManager;
    public FriendManager friendManager;
    public ChangelogManager changelogManager;

    public ClickGUI clickGUI;
    public ClickGUIPSE clickGUIPE;

    public DiscordUtil discordUtil;
    public TrayUtil trayUtil;

    public void startClient() {
        CLManager = new CLManager(Main.clName);
        logger.log("Client Launcher Name: " + Main.clName);
        settingsManager = new SettingsManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        clickGUI = new ClickGUI();
        clickGUIPE = new ClickGUIPSE();
        fileManager = new FileManager();
        tabGUI = new TabGUI();
        fileManager.readAllFiles();
        friendManager = new FriendManager();
        configSystem = new ConfigSystem();
        changelogManager = new ChangelogManager();

        new DiscordUtil();
        discordUtil = DiscordUtil.getSingleton();
        discordUtil.setupRPC("780938591278202880");

        trayUtil = new TrayUtil();

        StringBuilder author = new StringBuilder();
        for (int i = 0; i < KOKS.AUTHORS.length; i++) {
            author.append(KOKS.AUTHORS[i]).append(", ");
        }

        author = new StringBuilder(author.substring(0, author.length() - 2));

        Display.setTitle(NAME + " v" + VERSION + " | by " + author + " | Minecraft 1.8.8");
    }

    public void stopClient() {
        fileManager.writeAllFiles();
    }
}
