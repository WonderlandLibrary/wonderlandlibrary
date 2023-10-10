/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky;

import cc.hyperium.utils.HyperiumFontRenderer;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.thealtening.auth.TheAlteningAuthentication;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JOptionPane;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import tk.rektsky.commands.CommandsManager;
import tk.rektsky.event.EventManager;
import tk.rektsky.gui.UnicodeFontRenderer;
import tk.rektsky.hud.HUD;
import tk.rektsky.main.Auth;
import tk.rektsky.main.HwidUtil;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.render.Notification;
import tk.rektsky.module.specials.flys.VerusDamage;
import tk.rektsky.rektskyapi.user.Role;
import tk.rektsky.rektskyapi.user.User;
import tk.rektsky.script.API;
import tk.rektsky.utils.ExecutorService;
import tk.rektsky.utils.LaunchWrapper;
import tk.rektsky.utils.SessionChanger;
import tk.rektsky.utils.TaskScheduler;
import tk.rektsky.utils.file.FileManager;
import tk.rektsky.utils.network.NetworkUtil;
import tk.rektsky.utils.obf.wrapper.MinecraftWrapper;
import viamcp.ViaMCP;

public class Client {
    public static ExecutorService executorService = new ExecutorService();
    public static SessionChanger sessionManager = SessionChanger.getInstance();
    public static HUD mainhud;
    public static ServerData lastServer;
    public static Minecraft mc;
    public static tk.rektsky.ui.HUD hud;
    public CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList();
    public static boolean finishedSetup;
    public static final Boolean DEBUG;
    public static final String CLIENT_NAME = "RektSky";
    public static final String VERSION = "B5";
    public static final String AUTHORS = "fan87, CemrK, Hackage & Thereallo";
    public static final String CHANNEL = "Private";
    public static final String NAME = "RektSky B5  |  Private";
    public static String userName;
    public static String role;
    public static User user;
    public static String A1;
    public static NetworkUtil networkUtil;
    public static TheAlteningAuthentication theAltening;
    public static TaskScheduler taskScheduler;
    private static HyperiumFontRenderer Font;
    private static HyperiumFontRenderer FontCasper;
    private static HyperiumFontRenderer FontCasperBold;
    private static HyperiumFontRenderer FontBig;
    private static HyperiumFontRenderer HDFont;

    public static void addClientChat(IChatComponent msg) {
        if (Client.mc.thePlayer == null || Client.mc.theWorld == null) {
            return;
        }
        Client.mc.thePlayer.sendMessage(new ChatComponentText((Object)((Object)ChatFormatting.GREEN) + "[" + CLIENT_NAME + "] ").appendSibling(msg));
    }

    public static void addClientChat(Object msg) {
        if (Client.mc.thePlayer == null || Client.mc.theWorld == null) {
            return;
        }
        Client.mc.thePlayer.sendMessage(new ChatComponentText((Object)((Object)ChatFormatting.GREEN) + "[" + CLIENT_NAME + "] ").appendSibling(new ChatComponentText(msg + "")));
    }

    public static void notify(Notification.PopupMessage message) {
        Notification.displayNotification(message);
    }

    public static void notifyWithClassName(Notification.PopupMessage message) {
        Client.notify(message);
    }

    public static HyperiumFontRenderer getFont() {
        if (Font == null) {
            Font = new HyperiumFontRenderer(HyperiumFontRenderer.getDefaultFont(), 18.0f);
        }
        return Font;
    }

    public static HyperiumFontRenderer getCasper() {
        if (FontCasper == null) {
            FontCasper = new HyperiumFontRenderer(HyperiumFontRenderer.getCasper(), 24.0f);
        }
        return FontCasper;
    }

    public static HyperiumFontRenderer getCasperBold() {
        if (FontCasperBold == null) {
            FontCasperBold = new HyperiumFontRenderer(HyperiumFontRenderer.getCasperBold(), 24.0f);
        }
        return FontCasperBold;
    }

    public static HyperiumFontRenderer getFontBig() {
        if (FontBig == null) {
            FontBig = new HyperiumFontRenderer(HyperiumFontRenderer.getDefaultFont(), 24.0f);
        }
        return FontBig;
    }

    public static HyperiumFontRenderer getHDFont() {
        if (HDFont == null) {
            HDFont = new HyperiumFontRenderer(HyperiumFontRenderer.getDefaultFont(), 18.0f);
        }
        return HDFont;
    }

    public static HyperiumFontRenderer getFontWithSize(int fontSize) {
        return new HyperiumFontRenderer(HyperiumFontRenderer.getDefaultFont(), (float)fontSize);
    }

    @Deprecated
    public static UnicodeFontRenderer getFontWithSizeOld(int fontSize) {
        return new UnicodeFontRenderer("Comfortaa", 0, fontSize, 0.0f, 1.0f);
    }

    public static HyperiumFontRenderer getSigmaFontWithSize(int fontSize) {
        return new HyperiumFontRenderer(HyperiumFontRenderer.getSkidmaFont(), (float)fontSize);
    }

    public void init() {
        System.out.println("Hello World!");
        LaunchWrapper.logger.info("Welcome to RektSky B5  |  Private by fan87, CemrK, Hackage & Thereallo.");
        hud = new tk.rektsky.ui.HUD();
        taskScheduler = new TaskScheduler();
        EventManager.EVENT_BUS.register(taskScheduler);
        ModulesManager.inti();
        CommandsManager.init();
        Client.mc.gameSettings.showInventoryAchievementHint = false;
        try {
            ViaMCP.getInstance().start();
        }
        catch (Exception e2) {
            e2.printStackTrace();
            LaunchWrapper.logger.info("ViaMCP failed to initialize");
        }
        Runnable r2 = () -> {
            Runnable r2 = () -> {
                try {
                    try {
                        if (!Auth.gaming(mc)) {
                            throw new BootstrapMethodError();
                        }
                    }
                    catch (Exception e2) {
                        throw new BootstrapMethodError();
                    }
                }
                catch (BootstrapMethodError ex) {
                    try {
                        StringSelection stringSelection = new StringSelection(HwidUtil.getHwid());
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(stringSelection, null);
                        JOptionPane.showMessageDialog(null, "HWID mismatch (0x03) your HWID is: " + HwidUtil.getHwid() + " (copied to clipboard)", "Error", 0);
                        System.exit(-42);
                    }
                    catch (Exception ex2) {
                        JOptionPane.showMessageDialog(null, "Failed to get your HWID (0x02)", "Error", 0);
                        System.exit(-42);
                    }
                }
            };
            r2.run();
        };
        r2.run();
        LaunchWrapper.logger.info("RektSky B5  |  Private Started!");
        VerusDamage.sign.toUpperCase();
        FileManager.init();
        try {
            for (File file : FileManager.MODS_DIR.listFiles()) {
                for (Module m2 : API.loadAPIModules(file)) {
                    ModulesManager.MODULES.add(m2);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        EventManager.EVENT_BUS.register(networkUtil);
        finishedSetup = true;
    }

    static {
        lastServer = null;
        mc = MinecraftWrapper.getMinecraft(Minecraft.class);
        finishedSetup = false;
        DEBUG = false;
        userName = "Unknown";
        role = "User";
        user = new User("Unknown", "834036311110713384", Role.USER, HwidUtil.getHwid(), "");
        A1 = "AAAAAA_0_AAAAAA_0_AAAAAA_0";
        networkUtil = new NetworkUtil();
        theAltening = TheAlteningAuthentication.theAltening();
        Font = null;
        FontCasper = null;
        FontCasperBold = null;
        FontBig = null;
        HDFont = null;
    }
}

