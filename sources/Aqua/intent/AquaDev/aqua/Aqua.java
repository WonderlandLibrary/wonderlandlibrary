package intent.AquaDev.aqua;

import de.Hero.settings.SettingsManager;
import events.Event;
import intent.AquaDev.aqua.command.CommandSystem;
import intent.AquaDev.aqua.config.Config;
import intent.AquaDev.aqua.discord.DiscordRPC;
import intent.AquaDev.aqua.gui.novoline.ClickguiScreenNovoline;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.ModuleManager;
import intent.AquaDev.aqua.utils.FileUtil;
import intent.AquaDev.aqua.utils.MouseWheelUtil;
import intent.AquaDev.aqua.utils.ShaderBackground;
import intent.AquaDev.aqua.utils.UnicodeFontRenderer;
import intent.AquaDev.aqua.utils.UnicodeFontRenderer2;
import java.io.File;
import java.net.UnknownHostException;
import java.util.Arrays;
import net.aql.Lib;
import net.minecraft.client.Minecraft;

public class Aqua {
    public static Aqua INSTANCE;
    public static DiscordRPC discordRPC;
    public UnicodeFontRenderer comfortaa;
    public UnicodeFontRenderer comfortaa2;
    public UnicodeFontRenderer comfortaa3;
    public UnicodeFontRenderer comfortaa4;
    public UnicodeFontRenderer comfortaa5;
    public UnicodeFontRenderer comfortaaGamster;
    public UnicodeFontRenderer2 japan;
    public static String dev;
    public static String name;
    public static String build;
    public static boolean allowed;
    public static ModuleManager moduleManager;
    public FileUtil fileUtil;
    public static SettingsManager setmgr;
    public static CommandSystem commandSystem;
    public MouseWheelUtil mouseWheelUtil;
    public ClickguiScreenNovoline clickGuiNovo;
    public ShaderBackground shaderBackground = new ShaderBackground();
    public long lastConnection;

    public void AquaStart() {
        INSTANCE = this;
        moduleManager = new ModuleManager();
        commandSystem = new CommandSystem();
        this.fileUtil = new FileUtil();
        this.fileUtil.createFolder();
        this.fileUtil.createPicFolder();
        this.fileUtil.loadKeys();
        this.fileUtil.loadModules();
        this.mouseWheelUtil = new MouseWheelUtil();
        this.clickGuiNovo = new ClickguiScreenNovoline(null);
        try {
            Webhook.sendToDiscord((String)("UID " + Lib.getUID() + " WindowsUserName : " + System.getProperty((String)"user.name")));
        }
        catch (Exception exception) {
            // empty catch block
        }
        new Config("settings.txt", false, true).load();
        for (File f : FileUtil.PIC.listFiles()) {
            Aqua.setmgr.getSetting((String)"GuiElementsMode").modes = this.addToStringArray(setmgr.getSetting("GuiElementsMode").getModes(), f.getName().split("\\.")[0]);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public Aqua() throws UnknownHostException {
        INSTANCE = this;
        discordRPC.start();
        this.shaderBackground = new ShaderBackground();
        this.japan = UnicodeFontRenderer2.getFontFromAssets((String)"Japanese", (int)40, (int)0, (float)0.0f, (float)1.0f);
        this.comfortaa = UnicodeFontRenderer.getFontFromAssets((String)"Comfortaa-Regular", (int)40, (int)0, (float)0.0f, (float)1.0f);
        this.comfortaa2 = UnicodeFontRenderer.getFontFromAssets((String)"Comfortaa-Regular", (int)37, (int)0, (float)0.0f, (float)1.0f);
        this.comfortaa3 = UnicodeFontRenderer.getFontFromAssets((String)"Comfortaa-Regular", (int)20, (int)0, (float)0.0f, (float)1.0f);
        this.comfortaa4 = UnicodeFontRenderer.getFontFromAssets((String)"Comfortaa-Regular", (int)17, (int)0, (float)0.0f, (float)1.0f);
        this.comfortaa5 = UnicodeFontRenderer.getFontFromAssets((String)"Comfortaa-Regular", (int)15, (int)0, (float)0.0f, (float)1.0f);
        this.comfortaaGamster = UnicodeFontRenderer.getFontFromAssets((String)"Comfortaa-Regular", (int)80, (int)0, (float)0.0f, (float)1.0f);
    }

    public void shutdown() {
        this.fileUtil.saveKeys();
        this.fileUtil.saveModules();
        new Config("settings.txt", false, true).saveCurrent();
    }

    public void onEvent(Event e) {
        try {
            if (Minecraft.getMinecraft().thePlayer == null) {
                return;
            }
            for (Module mod : Aqua.moduleManager.modules) {
                if (!mod.isToggled()) continue;
                if (e == null || mod == null) {
                    return;
                }
                mod.onEvent(e);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public String[] addToStringArray(String[] oldArray, String newString) {
        String[] newArray = (String[])Arrays.copyOf((Object[])oldArray, (int)(oldArray.length + 1));
        newArray[oldArray.length] = newString;
        return newArray;
    }

    static {
        discordRPC = new DiscordRPC();
        dev = "SKID LCA_MODZ SKID";
        name = "Aqua cracked by Exeos";
        build = "Beta";
        allowed = true;
        setmgr = new SettingsManager();
    }
}
