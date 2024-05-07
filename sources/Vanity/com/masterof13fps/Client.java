package com.masterof13fps;

import com.masterof13fps.features.commands.CommandManager;
import com.masterof13fps.features.commands.impl.Friend;
import com.masterof13fps.features.modules.ModuleManager;
import com.masterof13fps.features.modules.impl.exploits.Crasher;
import com.masterof13fps.features.ui.guiscreens.GuiFirstUse;
import com.masterof13fps.manager.altmanager.AltManager;
import com.masterof13fps.manager.clickguimanager.ClickGui;
import com.masterof13fps.manager.fontmanager.FontManager;
import com.masterof13fps.manager.notificationmanager.NotificationManager;
import com.masterof13fps.manager.particlemanager.FBP;
import com.masterof13fps.manager.settingsmanager.SettingsManager;
import com.masterof13fps.utils.LoginUtil;
import com.masterof13fps.utils.render.Shader;
import net.minecraft.client.Minecraft;

import java.io.File;

public class Client implements Wrapper, Methods {

    private static Client instance = new Client();
    public long initTime = System.currentTimeMillis();
    private Minecraft mc = Minecraft.mc();
    private String clientName = "Vanity";
    private double clientVersion = 1.2;
    private String clientCoder = "CrazyMemeCoke";
    private String clientPrefix = ".";
    private String clientPrefixWorded = "Punkt";
    private String fakeVer = "OptiFine 1.8.8 HD_I7";
    private String clientBackground = "textures/client/background.jpg";
    private String clientIcon = "textures/client/icon.png";
    private String wurstWatermark = "textures/client/wurst.png";
    private String ambienWatermark = "textures/client/ambien-logo.png";
    private String shaderLoc = "textures/client/shader/";
    private String clientChangelog = "https://github.com/RealFantaCoke/minecraft-client/commits/master";
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private SettingsManager setmgr;
    private ClickGui clickgui;
    private FontManager fontManager;
    private File clientDir = new File(Minecraft.mc().mcDataDir + "/" + getClientName());
    private Friend friend;
    private Shader shader;
    private AltManager altManager;

    public static Client main() {
        return instance;
    }

    public static Client getInstance() {
        return instance;
    }

    public void startClient() {
        if (!clientDir.exists()) {
            try {
                clientDir.mkdir();
                notify.debug("Client-Ordner wurde erstellt!");
            } catch (Exception e) {
                notify.debug("Client-Ordner konnte nicht erstellt werden!");
            }

            mc.displayGuiScreen(new GuiFirstUse());
            notify.debug("GuiScreen 'FirstUse' wurde aufgerufen!");
        }
        getLoginUtil();
        notify.debug("LoginUtil geladen!");
        getEventManager();
        notify.debug("EventManager geladen!");
        fontManager = new FontManager();
        notify.debug("FontManager geladen!");
        fontManager.initFonts();
        notify.debug("Schriftarten initialisiert!");
        setmgr = new SettingsManager();
        notify.debug("SettingsManager geladen!");
        setmgr.loadSettings();
        notify.debug("Einstellungen geladen!");
        moduleManager = new ModuleManager();
        notify.debug("ModuleManager geladen!");
        moduleManager.loadModules();
        notify.debug("Modules geladen!");
        moduleManager.loadBinds();
        notify.debug("Keybinds geladen!");
        commandManager = new CommandManager();
        notify.debug("CommandManager geladen!");
        AltManager.loadAlts();
        notify.debug("Accounts geladen!");
        clickgui = new ClickGui();
        notify.debug("ClickGUI geladen!");
        new FBP().onStart();
        notify.debug("Partikelsystem geladen!");
        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));
        notify.debug("ShutdownHook geladen!");

        notify.debug("CLIENT VOLLSTÄNDIG GELADEN!");
    }

    public void onShutdown() {
        setmgr.saveSettings();
        moduleManager.saveModules();
        moduleManager.saveBinds();
        AltManager.saveAlts();

        Client.main().modMgr().getModule(Crasher.class).setState(false);

        NotificationManager.setPendingNotifications(null);
        NotificationManager.setCurrentNotification(null);
    }

    public File getClientDir() {
        return clientDir;
    }

    public ModuleManager modMgr() {
        return moduleManager;
    }

    public String getClientName() {
        return clientName;
    }

    public double getClientVersion() {
        return clientVersion;
    }

    public String getClientCoder() {
        return clientCoder;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public Minecraft getMc() {
        return mc;
    }

    public SettingsManager setMgr() {
        return setmgr;
    }

    public ClickGui getClickGui() {
        return clickgui;
    }

    public String getClientPrefix() {
        return clientPrefix;
    }

    public FontManager fontMgr() {
        return fontManager;
    }

    public String getClientChangelog() {
        return clientChangelog;
    }

    public String getClientBackground() {
        return clientBackground;
    }

    public Friend getFriend() {
        return friend;
    }

    public AltManager getAltManager() {
        return altManager;
    }

    public String getClientIcon() {
        return clientIcon;
    }

    public String getShaderLoc() {
        return shaderLoc;
    }

    public Shader getShader() {
        return shader;
    }

    public String getWurstWatermark() {
        return wurstWatermark;
    }

    public String getAmbienWatermark() {
        return ambienWatermark;
    }

    public long getInitTime() {
        return initTime;
    }

    public String getFakeVer() {
        return fakeVer;
    }

    public String getClientPrefixWorded() {
        return clientPrefixWorded;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public SettingsManager getSetmgr() {
        return setmgr;
    }

    public ClickGui getClickgui() {
        return clickgui;
    }

    public FontManager getFontManager() {
        return fontManager;
    }

    public LoginUtil getLoginUtil() {
        return loginUtil;
    }
}
