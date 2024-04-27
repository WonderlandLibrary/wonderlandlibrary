/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Ref$ObjectRef
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.Display
 */
package net.ccbluex.liquidbounce;

import dev.sakura_starring.util.safe.EmpowerUtil;
import dev.sakura_starring.util.safe.HWIDUtil;
import dev.sakura_starring.util.safe.QQUtils;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JOptionPane;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Ref;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.Wrapper;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.event.ClientShutdownEvent;
import net.ccbluex.liquidbounce.event.EventManager;
import net.ccbluex.liquidbounce.features.command.CommandManager;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.ccbluex.liquidbounce.features.special.BungeeCordSpoof;
import net.ccbluex.liquidbounce.file.FileManager;
import net.ccbluex.liquidbounce.management.CombatManager;
import net.ccbluex.liquidbounce.management.MemoryManager;
import net.ccbluex.liquidbounce.script.ScriptManager;
import net.ccbluex.liquidbounce.script.remapper.Remapper;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui;
import net.ccbluex.liquidbounce.ui.client.hud.HUD;
import net.ccbluex.liquidbounce.ui.font.FontLoaders;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.Display;

public final class LiquidBounce {
    public static CommandManager commandManager;
    public static final String CLIENT_VERSION;
    public static final String CLIENT_NAME;
    public static HUD hud;
    public static ScriptManager scriptManager;
    public static Wrapper wrapper;
    public static final String CLIENT_CLOUD;
    private static IResourceLocation background;
    public static CombatManager combatManager;
    public static String user;
    public static final LiquidBounce INSTANCE;
    public static final String CLIENT_RESOURCES;
    private static String QQNumber;
    public static final String CLIENT_CREATOR;
    public static EventManager eventManager;
    private static boolean isStarting;
    public static String qq;
    public static FileManager fileManager;
    public static ModuleManager moduleManager;
    public static ClickGui clickGui;

    public final void setHud(HUD hUD) {
        hud = hUD;
    }

    public final Wrapper getWrapper() {
        return wrapper;
    }

    public final String wight(@Nullable String string) {
        URLConnection uRLConnection = new URL(string).openConnection();
        if (uRLConnection == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.net.HttpURLConnection");
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRLConnection;
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        Ref.ObjectRef objectRef = new Ref.ObjectRef();
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            String string2 = bufferedReader.readLine();
            boolean bl = false;
            boolean bl2 = false;
            String string3 = string2;
            boolean bl3 = false;
            objectRef.element = string3;
            if (string2 == null) break;
            stringBuilder.append((String)objectRef.element);
            stringBuilder.append("\n");
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    public final void setEventManager(EventManager eventManager) {
        LiquidBounce.eventManager = eventManager;
    }

    private LiquidBounce() {
    }

    public final void setBackground(@Nullable IResourceLocation iResourceLocation) {
        background = iResourceLocation;
    }

    public final boolean isStarting() {
        return isStarting;
    }

    public final ScriptManager getScriptManager() {
        return scriptManager;
    }

    static {
        LiquidBounce liquidBounce;
        CLIENT_CLOUD = "https://cloud.liquidbounce.net/LiquidBounce";
        CLIENT_CREATOR = "Sakura\u00b7StarRing";
        CLIENT_NAME = "AtField";
        CLIENT_RESOURCES = "atfield";
        CLIENT_VERSION = "v1.3";
        INSTANCE = liquidBounce = new LiquidBounce();
        QQNumber = "dawdaw";
    }

    public final String getQQNumber() {
        return QQNumber;
    }

    public final EventManager getEventManager() {
        return eventManager;
    }

    public final void setWrapper(Wrapper wrapper) {
        LiquidBounce.wrapper = wrapper;
    }

    public final void displayTray(String string, String string2, @Nullable TrayIcon.MessageType messageType) {
        SystemTray systemTray = SystemTray.getSystemTray();
        Object object = "/assets/minecraft/more/icon_32x32.png";
        Class<?> clazz = this.getClass();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        boolean bl = false;
        String string3 = ((String)object).toLowerCase();
        Image image2 = toolkit.createImage(clazz.getResource(string3));
        object = new TrayIcon(image2, "Tray Demo");
        ((TrayIcon)object).setImageAutoSize(true);
        ((TrayIcon)object).setToolTip("AtField v1.3");
        systemTray.add((TrayIcon)object);
        ((TrayIcon)object).displayMessage(string, string2, messageType);
    }

    public final void setQq(String string) {
        qq = string;
    }

    public final void setStarting(boolean bl) {
        isStarting = bl;
    }

    public final void setCommandManager(CommandManager commandManager) {
        LiquidBounce.commandManager = commandManager;
    }

    public final void setUser(String string) {
        user = string;
    }

    public final void stopClient() {
        eventManager.callEvent(new ClientShutdownEvent());
        fileManager.saveAllConfigs();
    }

    public final void setQQNumber(String string) {
        QQNumber = string;
    }

    public final CombatManager getCombatManager() {
        return combatManager;
    }

    public final ModuleManager getModuleManager() {
        return moduleManager;
    }

    public final ClickGui getClickGui() {
        return clickGui;
    }

    public final IResourceLocation getBackground() {
        return background;
    }

    public final HUD getHud() {
        return hud;
    }

    public final String getQq() {
        return qq;
    }

    public final void setCombatManager(CombatManager combatManager) {
        LiquidBounce.combatManager = combatManager;
    }

    public final FileManager getFileManager() {
        return fileManager;
    }

    public final void startClient() {
        if (!EmpowerUtil.verification()) {
            JOptionPane.showInputDialog(null, "\u6b64\u6c34\u5f71\u672a\u6b63\u7248\u6388\u6743", "\u5df2\u786e\u5b9a");
            Minecraft.func_71410_x().func_71400_g();
        }
        HWIDUtil.newVerification();
        QQUtils.getLoginQQList();
        QQUtils.verification();
        QQUtils.qqName = QQUtils.getQQNick();
        isStarting = true;
        startClient.1 var1_1 = startClient.1.INSTANCE;
        startClient.2 var2_2 = startClient.2.INSTANCE;
        startClient.3 var3_3 = startClient.3.INSTANCE;
        var3_3.invoke();
        Display.setTitle((String)"1.12.2 Logs");
        long l = System.currentTimeMillis();
        ClientUtils.getLogger().info("Starting AtField v1.3r, by Sakura\u00b7StarRing");
        fileManager = new FileManager();
        eventManager = new EventManager();
        combatManager = new CombatManager();
        eventManager.registerListener(new RotationUtils());
        eventManager.registerListener(new AntiForge());
        eventManager.registerListener(new BungeeCordSpoof());
        eventManager.registerListener(new InventoryUtils());
        eventManager.registerListener(combatManager);
        eventManager.registerListener(new MemoryManager());
        commandManager = new CommandManager();
        Fonts.loadFonts();
        FontLoaders.initFonts();
        moduleManager = new ModuleManager();
        moduleManager.registerModules();
        try {
            Remapper.INSTANCE.loadSrg();
            scriptManager = new ScriptManager();
            scriptManager.loadScripts();
            scriptManager.enableScripts();
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("Failed to load scripts.", throwable);
        }
        commandManager.registerCommands();
        fileManager.loadConfigs(LiquidBounce.fileManager.valuesConfig, LiquidBounce.fileManager.accountsConfig, LiquidBounce.fileManager.friendsConfig, LiquidBounce.fileManager.xrayConfig);
        clickGui = new ClickGui();
        fileManager.loadConfig(LiquidBounce.fileManager.clickGuiConfig);
        hud = HUD.Companion.createDefault();
        fileManager.loadConfig(LiquidBounce.fileManager.hudConfig);
        ClientUtils.disableFastRender();
        GuiAltManager.loadGenerators();
        isStarting = false;
        ClientUtils.getLogger().info("Loaded client in " + (System.currentTimeMillis() - l) + " ms.");
        try {
            Display.setTitle((String)"AtField v1.3");
        }
        catch (Throwable throwable) {
            Display.setTitle((String)"AtField v1.3");
        }
    }

    public final void setModuleManager(ModuleManager moduleManager) {
        LiquidBounce.moduleManager = moduleManager;
    }

    public final String getUser() {
        return user;
    }

    public final CommandManager getCommandManager() {
        return commandManager;
    }

    public final void setFileManager(FileManager fileManager) {
        LiquidBounce.fileManager = fileManager;
    }

    public final void setScriptManager(ScriptManager scriptManager) {
        LiquidBounce.scriptManager = scriptManager;
    }

    public final void setClickGui(ClickGui clickGui) {
        LiquidBounce.clickGui = clickGui;
    }
}

