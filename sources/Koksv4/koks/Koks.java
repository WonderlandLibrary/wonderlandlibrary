package koks;

import de.liquiddev.ircclient.api.IrcClient;
import de.liquiddev.ircclient.api.SimpleIrcApi;
import de.liquiddev.ircclient.client.ClientType;
import de.liquiddev.ircclient.client.IrcClientFactory;
import koks.api.ConfigSystem;
import koks.api.clickgui.periodic.PeriodicClickGUI;
import koks.api.clickgui.sigma.SigmaClickGUI;
import koks.api.event.EventHandling;
import koks.api.font.Fonts;
import koks.api.manager.cl.CLManager;
import koks.api.manager.friend.FriendManager;
import koks.api.manager.mainmenu.MainMenuManager;
import koks.api.manager.particle.ParticleManager;
import koks.api.manager.proxy.ProxyManager;
import koks.api.registry.RegistryHelper;
import koks.api.registry.file.FileRegistry;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.DiscordUtil;
import koks.api.utils.Kernel32;
import koks.api.utils.Logger;
import koks.irc.events.IrcCustomDataEvent;
import koks.module.misc.IRC;
import koks.script.ScriptLoader;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.File;
import java.util.Calendar;

/**
 * Copyright 2021, Koks Team
 * Please don't use the code
 */

public class Koks {

    private static Koks KOKS;

    public final File DIR;

    public CLManager clManager;
    public FriendManager friendManager;
    public ConfigSystem configSystem;
    public ProxyManager proxyManager;
    public ParticleManager particleManager;

    public PeriodicClickGUI periodicClickGUI;
    public SigmaClickGUI sigmaClickGUI;

    @Getter
    private static boolean loaded = false;

    public final boolean isApril;

    public final long initTime = System.currentTimeMillis();

    public String commandPrefix = ".";
    public static String clName = "";

    public static String name = "Koks";
    public static String version = "4.1.0";
    public static final String snapshot = "22w02a";
    public static String[] authors = new String[]{"DasDirt", "E-Sound", "Felix1337", "Juli15", "Kroko", "!ez.h"};
    public static String prefix = "§c" + name + " §7>> ";

    public Color clientColor = new Color(7, 224, 37, 255);
    public String alteningApiKey;

    public IrcClient irc;
    public ScriptLoader scriptLoader;

    public Koks() {
        this.DIR = new File(Minecraft.getMinecraft().mcDataDir, "Koks");
        final Calendar calendar = Calendar.getInstance();
        isApril = calendar.get(Calendar.DAY_OF_MONTH) == 1 && calendar.get(Calendar.MONTH) == Calendar.APRIL;
        if (isApril) {
            name = "Wurst";
            version = "7";
            authors = new String[]{"Alexander01998"};
            prefix = "§c[§6Wurst§c] §f";
        }
        KOKS = this;
    }

    public static boolean offlineMode = false;

    public void onStartup() {
        if (Kernel32.INSTANCE.IsDebuggerPresent())
            return;
        System.out.println("""

                ██╗  ██╗ ██████╗ ██╗  ██╗███████╗
                ██║ ██╔╝██╔═══██╗██║ ██╔╝██╔════╝
                █████╔╝ ██║   ██║█████╔╝ ███████╗
                ██╔═██╗ ██║   ██║██╔═██╗ ╚════██║
                ██║  ██╗╚██████╔╝██║  ██╗███████║
                ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝
                                                \s""");

        if (!DIR.exists())
            DIR.mkdirs();

        Logger logger = Logger.getInstance();

        if (!isDeveloperMode()) {
            offlineMode = false;
        }

        try {
            if (!offlineMode) {
                irc = IrcClientFactory.getDefault().createIrcClient(ClientType.KOKS, "rgXEQLjqJdPY4QXa", clName, version);
                irc.setExtra(Koks.clName);

                irc.getApiManager().registerApi(new SimpleIrcApi() {
                    @Override
                    public void addChat(String s) {
                        if (Minecraft.getMinecraft().thePlayer != null)
                            if (ModuleRegistry.getModule(IRC.class).isToggled())
                                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(s.replace("�", "»").replace("Koks", "§4KOKS§7")));
                    }
                });
                irc.getApiManager().registerCustomDataListener(new IrcCustomDataEvent());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (isDeveloperMode())
                offlineMode = true;
        }

        clManager = new CLManager(clName);
        logger.log("CL Name: " + clName);

        if (!offlineMode)
            if (clName.equalsIgnoreCase("haze"))
                irc.sendCustomData("cape", "hase".getBytes());
            else
                irc.sendCustomData("cape", clName.getBytes());

        new RegistryHelper().initialize();

        EventHandling.init();

        logger.log("Registered all Executors");

        configSystem = new ConfigSystem();
        friendManager = FriendManager.getInstance();
        proxyManager = new ProxyManager();
        particleManager = new ParticleManager();

        Fonts.loadFonts();

        sigmaClickGUI = new SigmaClickGUI();
        periodicClickGUI = new PeriodicClickGUI();

        MainMenuManager.init();

        try {
            new DiscordUtil().setupRPC("842470849013743677");
        } catch (Exception ignored) {
        }

        scriptLoader = new ScriptLoader(DIR);
        /*scriptLoader.loadScript("test", ScriptLoader.Type.JAVA);*/

        resetTitle();

        loaded = true;

        /* Thx to eiken */
        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));
    }

    public void resetTitle() {
        String author = "";
        for (String auth : authors)
            author += auth + ", ";
        author = author.substring(0, author.length() - 2);
        Display.setTitle(name + " v" + version + (snapshot != null ? " " + snapshot : "") + " | by " + author + " | Minecraft 1.8.9" + (isDeveloperMode() ? " | Dev Build #" + System.currentTimeMillis() : ""));
    }

    public static boolean isDeveloperMode() {
        return System.getProperty("java.class.path").contains("idea_rt.jar");
    }

    public void onShutdown() {
        FileRegistry.writeAll();
    }

    public static Koks getKoks() {
        return KOKS;
    }
}
