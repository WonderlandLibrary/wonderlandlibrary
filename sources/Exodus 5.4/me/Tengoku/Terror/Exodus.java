/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 */
package me.Tengoku.Terror;

import ClickGUIs.Lemon.clickgui.ClickGui;
import ClickGUIs.NiceGUI.NiceGui;
import ClickGUIs.intellij.Intellij;
import ClickGUIs.recode.Recode;
import ClickGUIs.skeet.Skeet;
import de.Hero.Clickgui;
import de.Hero.clickgui.ClickGUI;
import de.Hero.settings.SettingsManager;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import me.Tengoku.Terror.API.ExodusAPI;
import me.Tengoku.Terror.API.impl.Hwid;
import me.Tengoku.Terror.Value.ValueManager;
import me.Tengoku.Terror.command.CommandManager;
import me.Tengoku.Terror.config.SaveLoad;
import me.Tengoku.Terror.event.Event;
import me.Tengoku.Terror.event.EventManager;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventChat;
import me.Tengoku.Terror.event.events.EventKey;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.module.ModuleManager;
import me.Tengoku.Terror.ui.AltManager;
import me.Tengoku.Terror.ui.CustomIngameGui;
import me.Tengoku.Terror.ui.GuiClientLogin;
import me.Tengoku.Terror.util.HWIDUtils;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.opengl.Display;

public class Exodus {
    public CustomIngameGui HUD;
    public EventManager eventManager;
    public static GuiClientLogin mainGUI;
    public static CommandManager commandManager;
    public ValueManager valueManager;
    public static boolean isPremium;
    public Clickgui clickguisecond;
    public ModuleManager moduleManager;
    public ClickGui LemonGUI;
    public SaveLoad configManager;
    public NiceGui niceGUI;
    public SettingsManager settingsManager;
    public Recode recode;
    public Skeet skeet;
    public HWIDUtils hwid;
    public FontUtil fontRenderer;
    public static Exodus INSTANCE;
    public String name = "Exodus";
    public Intellij clickGuiIntellij;
    public ClickGUI clickGui;
    public AltManager altManager;

    static {
        INSTANCE = new Exodus();
    }

    public void startClient() {
        FontUtil.bootstrap();
        this.settingsManager = new SettingsManager();
        this.eventManager = new EventManager();
        this.altManager = new AltManager();
        commandManager = new CommandManager();
        this.valueManager = new ValueManager();
        this.moduleManager = new ModuleManager();
        this.clickGui = new ClickGUI();
        this.niceGUI = new NiceGui();
        this.recode = new Recode();
        this.clickguisecond = new Clickgui();
        this.clickGuiIntellij = new Intellij();
        this.configManager = new SaveLoad();
        this.skeet = new Skeet();
        this.LemonGUI = new ClickGui();
        this.HUD = new CustomIngameGui();
        EventManager.register(this);
        Display.setTitle((String)this.name);
    }

    public SettingsManager getSettingsManager() {
        return this.settingsManager;
    }

    public <T extends Module> T getModule(Class clazz) {
        return (T)((Module)this.getModuleManager().getModules().stream().filter(module -> module.getClass() == clazz).findFirst().orElse(null));
    }

    public static void grabHWID() {
        try {
            System.out.println(Hwid.getHWID2());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            try {
                noSuchAlgorithmException.printStackTrace();
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
    }

    public static void addChatMessage(String string) {
        string = "\ufffd9 Exodus\ufffd7: " + string;
        Minecraft.getMinecraft();
        Minecraft.thePlayer.addChatMessage(new ChatComponentText(string));
    }

    @EventTarget
    public void onKey(EventKey eventKey) {
        this.moduleManager.getModules().stream().filter(module -> module.getKey() == eventKey.getKey()).forEach(module -> module.toggle());
    }

    public void shutdown() {
        if (Exodus.INSTANCE.configManager != null) {
            Exodus.INSTANCE.configManager.save();
        }
        EventManager.unregister(this);
    }

    public ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    public static void startChecking() {
        mainGUI = new GuiClientLogin(mainGUI);
        ExodusAPI.authAutoHWID();
        if (ExodusAPI.authAutoHWID()) {
            isPremium = true;
            System.out.println("Login Complete! User: " + ExodusAPI.getUserName() + " |  Rank: " + ExodusAPI.getRole() + " | HWID: " + ExodusAPI.getHWID());
            Display.setTitle((String)Exodus.INSTANCE.name);
        } else {
            System.out.println("HWID doesn't match! Aborting...");
            isPremium = false;
            try {
                Minecraft.getMinecraft().renderGlobal = null;
            }
            catch (IOException | NoSuchAlgorithmException exception) {
                isPremium = false;
                Minecraft.getMinecraft().displayGuiScreen(mainGUI);
                System.out.println("Login ERROR");
                exception.printStackTrace();
            }
        }
    }

    public static void onEvent(Event event) {
        if (event instanceof EventChat) {
            commandManager.handleChat((EventChat)event);
        }
        for (Module module : Exodus.INSTANCE.moduleManager.getModules()) {
            if (!module.isToggled()) continue;
            module.onEvent(event);
        }
    }
}

