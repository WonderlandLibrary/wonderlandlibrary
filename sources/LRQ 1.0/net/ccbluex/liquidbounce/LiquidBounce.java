/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.Display
 */
package net.ccbluex.liquidbounce;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ccbluex.liquidbounce.api.Wrapper;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.cape.CapeAPI;
import net.ccbluex.liquidbounce.event.ClientShutdownEvent;
import net.ccbluex.liquidbounce.event.EventManager;
import net.ccbluex.liquidbounce.features.command.CommandManager;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.ccbluex.liquidbounce.features.special.BungeeCordSpoof;
import net.ccbluex.liquidbounce.features.special.DonatorCape;
import net.ccbluex.liquidbounce.file.FileManager;
import net.ccbluex.liquidbounce.manager.CombatManager;
import net.ccbluex.liquidbounce.script.ScriptManager;
import net.ccbluex.liquidbounce.script.remapper.Remapper;
import net.ccbluex.liquidbounce.tabs.BlocksTab;
import net.ccbluex.liquidbounce.tabs.ExploitsTab;
import net.ccbluex.liquidbounce.tabs.HeadsTab;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui;
import net.ccbluex.liquidbounce.ui.client.hud.HUD;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClassUtils;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.ccbluex.liquidbounce.utils.misc.sound.TipSoundManager;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.Display;

public final class LiquidBounce {
    public static final String CLIENT_NAME = "LRQ";
    public static final double CLIENT_VERSION = 1.0;
    public static final String CLIENT_CREATOR = "CCBlueX";
    public static final String MINECRAFT_VERSION = "1.12.2";
    public static final String CLIENT_CLOUD = "https://cloud.liquidbounce.net/LiquidBounce";
    private static boolean isStarting;
    private static long playTimeStart;
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static EventManager eventManager;
    public static FileManager fileManager;
    public static ScriptManager scriptManager;
    public static TipSoundManager tipSoundManager;
    public static CombatManager combatManager;
    public static HUD hud;
    public static ClickGui clickGui;
    private static int latestVersion;
    private static IResourceLocation background;
    public static Wrapper wrapper;
    public static final LiquidBounce INSTANCE;

    public final boolean isStarting() {
        return isStarting;
    }

    public final void setStarting(boolean bl) {
        isStarting = bl;
    }

    public final long getPlayTimeStart() {
        return playTimeStart;
    }

    public final void setPlayTimeStart(long l) {
        playTimeStart = l;
    }

    public final ModuleManager getModuleManager() {
        return moduleManager;
    }

    public final void setModuleManager(ModuleManager moduleManager) {
        LiquidBounce.moduleManager = moduleManager;
    }

    public final CommandManager getCommandManager() {
        return commandManager;
    }

    public final void setCommandManager(CommandManager commandManager) {
        LiquidBounce.commandManager = commandManager;
    }

    public final EventManager getEventManager() {
        return eventManager;
    }

    public final void setEventManager(EventManager eventManager) {
        LiquidBounce.eventManager = eventManager;
    }

    public final FileManager getFileManager() {
        return fileManager;
    }

    public final void setFileManager(FileManager fileManager) {
        LiquidBounce.fileManager = fileManager;
    }

    public final ScriptManager getScriptManager() {
        return scriptManager;
    }

    public final void setScriptManager(ScriptManager scriptManager) {
        LiquidBounce.scriptManager = scriptManager;
    }

    public final TipSoundManager getTipSoundManager() {
        return tipSoundManager;
    }

    public final void setTipSoundManager(TipSoundManager tipSoundManager) {
        LiquidBounce.tipSoundManager = tipSoundManager;
    }

    public final CombatManager getCombatManager() {
        return combatManager;
    }

    public final void setCombatManager(CombatManager combatManager) {
        LiquidBounce.combatManager = combatManager;
    }

    public final HUD getHud() {
        return hud;
    }

    public final void setHud(HUD hUD) {
        hud = hUD;
    }

    public final ClickGui getClickGui() {
        return clickGui;
    }

    public final void setClickGui(ClickGui clickGui) {
        LiquidBounce.clickGui = clickGui;
    }

    public final int getLatestVersion() {
        return latestVersion;
    }

    public final void setLatestVersion(int n) {
        latestVersion = n;
    }

    public final IResourceLocation getBackground() {
        return background;
    }

    public final void setBackground(@Nullable IResourceLocation iResourceLocation) {
        background = iResourceLocation;
    }

    public final Wrapper getWrapper() {
        return wrapper;
    }

    public final void setWrapper(Wrapper wrapper) {
        LiquidBounce.wrapper = wrapper;
    }

    public final void startClient() {
        isStarting = true;
        Display.setTitle((String)"LRQ 1.12 logs");
        ClientUtils.getLogger().info("Starting LRQ b1.0, by CCBlueX");
        fileManager = new FileManager();
        eventManager = new EventManager();
        eventManager.registerListener(new RotationUtils());
        eventManager.registerListener(new AntiForge());
        eventManager.registerListener(new BungeeCordSpoof());
        eventManager.registerListener(new DonatorCape());
        eventManager.registerListener(new InventoryUtils());
        commandManager = new CommandManager();
        Fonts.loadFonts();
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
        tipSoundManager = new TipSoundManager();
        fileManager.loadConfigs(LiquidBounce.fileManager.modulesConfig, LiquidBounce.fileManager.valuesConfig, LiquidBounce.fileManager.accountsConfig, LiquidBounce.fileManager.friendsConfig, LiquidBounce.fileManager.xrayConfig, LiquidBounce.fileManager.shortcutsConfig);
        clickGui = new ClickGui();
        fileManager.loadConfig(LiquidBounce.fileManager.clickGuiConfig);
        if (ClassUtils.INSTANCE.hasForge()) {
            new BlocksTab();
            new ExploitsTab();
            new HeadsTab();
        }
        try {
            CapeAPI.INSTANCE.registerCapeService();
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("Failed to register cape service", throwable);
        }
        hud = HUD.Companion.createDefault();
        fileManager.loadConfig(LiquidBounce.fileManager.hudConfig);
        ClientUtils.disableFastRender();
        try {
            JsonElement jsonObj = new JsonParser().parse(HttpUtils.get("https://cloud.liquidbounce.net/LiquidBounce/versions.json"));
            if (jsonObj instanceof JsonObject && ((JsonObject)jsonObj).has(MINECRAFT_VERSION)) {
                latestVersion = ((JsonObject)jsonObj).get(MINECRAFT_VERSION).getAsInt();
            }
        }
        catch (Throwable exception) {
            ClientUtils.getLogger().error("Failed to check for updates.", exception);
        }
        GuiAltManager.loadGenerators();
        isStarting = false;
    }

    public final void stopClient() {
        eventManager.callEvent(new ClientShutdownEvent());
        fileManager.saveAllConfigs();
    }

    private LiquidBounce() {
    }

    static {
        LiquidBounce liquidBounce;
        INSTANCE = liquidBounce = new LiquidBounce();
    }

    public static final class CLIENT {
    }
}

