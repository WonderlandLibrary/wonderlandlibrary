package me.aquavit.liquidsense;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.utils.forge.BlocksTab;
import me.aquavit.liquidsense.utils.forge.ExploitsTab;
import me.aquavit.liquidsense.utils.forge.HeadsTab;
import me.aquavit.liquidsense.utils.login.UserUtils;
import me.aquavit.liquidsense.utils.mc.ClassUtils;
import me.aquavit.liquidsense.event.events.ClientShutdownEvent;
import me.aquavit.liquidsense.event.EventManager;
import me.aquavit.liquidsense.command.CommandManager;
import me.aquavit.liquidsense.module.ModuleManager;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.Main;
import me.aquavit.liquidsense.ui.client.gui.elements.AntiForge;
import me.aquavit.liquidsense.ui.client.gui.elements.BungeeCordSpoof;
import me.aquavit.liquidsense.file.FileManager;
import me.aquavit.liquidsense.script.ScriptManager;
import me.aquavit.liquidsense.script.remapper.Remapper;
import me.aquavit.liquidsense.ui.client.gui.GuiAltManager;
import me.aquavit.liquidsense.ui.client.hud.HUD;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.client.InventoryUtils;
import me.aquavit.liquidsense.utils.client.RotationUtils;
import me.aquavit.liquidsense.utils.misc.HttpUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.Display;

@SideOnly(Side.CLIENT)
public class LiquidSense {
    public static final String CLIENT_NAME = "LiquidSense";

    public static final String CLIENT_WEB = "minger.club";
    public static final Boolean IN_DEV = true;
    public static final String CLIENT_VERSION = IN_DEV ? "Dev" : "Release";
    public static final String CLIENT_CREATOR = "AquaVit";
    public static final String MINECRAFT_VERSION = "1.8.9";
    public static final String CLIENT_CLOUD = "https://cloud.liquidbounce.net/LiquidBounce";
    public static final String CLIENT_RESOURCE = "https://cdn.jsdelivr.net/gh/BlogResourceRepositories/ClientResource@main/";

    public static boolean isStarting;

    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static EventManager eventManager;
    public static FileManager fileManager;
    public static ScriptManager scriptManager;

    public static HUD hud;

    public static me.aquavit.liquidsense.ui.client.clickgui.neverlose.hud.HUD newhud;

    public static Main neverlose;

    public static int latestVersion;

    public static ResourceLocation background;

    public static void startClient() {
        isStarting = true;

        Display.setTitle(LiquidSense.CLIENT_NAME + " | " +  LiquidSense.CLIENT_VERSION + " | " + LiquidSense.MINECRAFT_VERSION  + " | " + "By " + CLIENT_CREATOR + " | Loading...");
        ClientUtils.getLogger().info("Starting "+CLIENT_NAME+" "+CLIENT_VERSION+", by "+CLIENT_CREATOR);

        fileManager = new FileManager();

        EventManager eventManager = LiquidSense.eventManager = new EventManager();

        eventManager.registerListener(new RotationUtils());
        eventManager.registerListener(new AntiForge());
        eventManager.registerListener(new BungeeCordSpoof());
        eventManager.registerListener(new InventoryUtils());

        commandManager = new CommandManager();

        Fonts.loadFonts();

        moduleManager = new ModuleManager();
        moduleManager.registerModules();

        try {
            Remapper.loadSrg();
            scriptManager = new ScriptManager();
            scriptManager.loadScripts();
            scriptManager.enableScripts();
        } catch (Throwable throwable) {
            ClientUtils.getLogger().error("Failed to load scripts.", throwable);
        }

        commandManager.registerCommands();

        fileManager.loadConfigs(fileManager.modulesConfig, fileManager.valuesConfig, fileManager.accountsConfig,
                fileManager.friendsConfig, fileManager.xrayConfig, fileManager.shortcutsConfig);

        neverlose = new Main();

        if (ClassUtils.hasForge()) {
            new BlocksTab();
            new ExploitsTab();
            new HeadsTab();
        }

        hud = HUD.Companion.createDefault();
        fileManager.loadConfig(fileManager.hudConfig);
        fileManager.loadConfig(fileManager.clickGuiConfig);

        ClientUtils.disableFastRender();

        try {
            // Read versions json from cloud
            JsonObject jsonObj = new JsonParser().parse(HttpUtils.get(CLIENT_CLOUD + "/versions.json")).getAsJsonObject();
            // Check json is valid object and has current minecraft version
            if (jsonObj.isJsonObject() && jsonObj.has(MINECRAFT_VERSION)) {
                // Get official latest client version
                latestVersion = jsonObj.get(MINECRAFT_VERSION).getAsInt();
            }
        } catch (Throwable exception) {
            ClientUtils.getLogger().error("Failed to check for updates.");
        }

        LoadAltManagerSkin();

        GuiAltManager.loadGenerators();

        fileManager.loadConfigs(fileManager.setNameConfig);

        Display.setTitle(LiquidSense.CLIENT_NAME + " | " + LiquidSense.CLIENT_VERSION + " | " + LiquidSense.MINECRAFT_VERSION +" | "+ "By AquaVit");
        ClientUtils.getLogger().info(LiquidSense.CLIENT_NAME + " | " + "By AquaVit" + " | " + "Loading completed");

        isStarting = false;
    }

    public static void stopClient() {
        LiquidSense.eventManager.callEvent(new ClientShutdownEvent());
        LiquidSense.fileManager.saveAllConfigs();
    }

    public static void LoadAltManagerSkin(){

        ClientUtils.getLogger().info("Loading AltManagerSkin.");

        for (int id = 0; id < LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.size(); ++id) {
            if (!LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.get(id).isCracked() && !GuiAltManager.skin.containsKey(id))
                GuiAltManager.skin.put(id, UserUtils.getPlayerSkin(UserUtils.getUUID(LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.get(id).getAccountName())));
        }
    }

    public final ModuleManager getModuleManager() {
        return LiquidSense.moduleManager;
    }

    public final void setModuleManager(ModuleManager moduleManager) {
        LiquidSense.moduleManager = moduleManager;
    }

    public final CommandManager getCommandManager() {
        return LiquidSense.commandManager;
    }

    public final void setCommandManager(CommandManager commandManager) {
        LiquidSense.commandManager = commandManager;
    }

    public final EventManager getEventManager() {
        return LiquidSense.eventManager;
    }

    public final void setEventManager(EventManager eventManager) {
        LiquidSense.eventManager = eventManager;
    }

    public static FileManager getFileManager() {
        return LiquidSense.fileManager;
    }

    public final void setFileManager(FileManager fileManager) {
        LiquidSense.fileManager = fileManager;
    }

    public final ScriptManager getScriptManager() {
        return LiquidSense.scriptManager;
    }

    public final void setScriptManager(ScriptManager scriptManager) {
        LiquidSense.scriptManager = scriptManager;
    }

    public static HUD getHud() {
        return hud;
    }

    public static void setHud(HUD hud) {
        hud = hud;
    }

    public final Main getNeverlose() {
        return neverlose;
    }

    public final void setNeverlose(Main main) {
        neverlose = main;
    }

    public final int getLatestVersion() {
        return latestVersion;
    }

    public final void setLatestVersion(int version) {
        latestVersion = version;
    }

    public static ResourceLocation getBackground() {
        return background;
    }

    public static void setBackground(ResourceLocation resourceLocation) {
        background = resourceLocation;
    }
}
