package net.ccbluex.liquidbounce;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.util.ArrayList;
import java.util.Objects;
import javax.imageio.ImageIO;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.manager.CombatManager;
import me.sound.Sound;
import me.utils.AnimationHandler;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.Wrapper;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.cape.CapeAPI;
import net.ccbluex.liquidbounce.event.ClientShutdownEvent;
import net.ccbluex.liquidbounce.event.EventManager;
import net.ccbluex.liquidbounce.features.command.CommandManager;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.ccbluex.liquidbounce.features.special.BungeeCordSpoof;
import net.ccbluex.liquidbounce.features.special.ClientRichPresence;
import net.ccbluex.liquidbounce.features.special.DonatorCape;
import net.ccbluex.liquidbounce.file.FileConfig;
import net.ccbluex.liquidbounce.file.FileManager;
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
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000¾\n\n\u0000\n\b\n\n\b\n\b\n\u0000\n\n\b\n\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\bÆ\u000020B\b¢J#}0~20202\b0HJ0~J0~R0XT¢\n\u0000R0XT¢\n\u0000R0XT¢\n\u0000R0XT¢\n\u0000R\b0\tXT¢\n\u0000R\n0XT¢\n\u0000R\f0XT¢\n\u0000R!\r00j\b0`¢\b\n\u0000\bR0X.¢\n\u0000\b\"\bR0X¢\n\u0000\b\"\bR0X.¢\n\u0000\b !\"\b\"#R$0%X.¢\n\u0000\b&'\"\b()R*0+X.¢\n\u0000\b,-\"\b./R001X.¢\n\u0000\b23\"\b45R607X.¢\n\u0000\b89\"\b:;R<0=X.¢\n\u0000\b>?\"\b@ARB0CX.¢\n\u0000\bDE\"\bFGRH0X¢\n\u0000\bHI\"\bJKRL0\tX¢\n\u0000\bMN\"\bOPRQ0RX.¢\n\u0000\bST\"\bUVRW0XX.¢\n\u0000\bYZ\"\b[\\R\u001a]0^X.¢\n\u0000\b_`\"\babRc0dX.¢\n\u0000\bef\"\bghRi0jX¢\n\u0000\bkl\"\bmnRo0pX¢\n\u0000\bqr\"\bstRu0¢\b\n\u0000\bvIRw0xX.¢\n\u0000\byz\"\b{|¨"}, d2={"Lnet/ccbluex/liquidbounce/LiquidBounce;", "", "()V", "CLIENT_CLOUD", "", "CLIENT_CREATOR", "CLIENT_NAME", "CLIENT_NAME2", "CLIENT_VERSION", "", "IN_DEV", "", "MINECRAFT_VERSION", "UPDATE_LIST", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "getUPDATE_LIST", "()Ljava/util/ArrayList;", "animationHandler", "Lme/utils/AnimationHandler;", "getAnimationHandler", "()Lme/utils/AnimationHandler;", "setAnimationHandler", "(Lme/utils/AnimationHandler;)V", "background", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "getBackground", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "setBackground", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;)V", "clickGui", "Lnet/ccbluex/liquidbounce/ui/client/clickgui/ClickGui;", "getClickGui", "()Lnet/ccbluex/liquidbounce/ui/client/clickgui/ClickGui;", "setClickGui", "(Lnet/ccbluex/liquidbounce/ui/client/clickgui/ClickGui;)V", "clientRichPresence", "Lnet/ccbluex/liquidbounce/features/special/ClientRichPresence;", "getClientRichPresence", "()Lnet/ccbluex/liquidbounce/features/special/ClientRichPresence;", "setClientRichPresence", "(Lnet/ccbluex/liquidbounce/features/special/ClientRichPresence;)V", "combatManager", "Lme/manager/CombatManager;", "getCombatManager", "()Lme/manager/CombatManager;", "setCombatManager", "(Lme/manager/CombatManager;)V", "commandManager", "Lnet/ccbluex/liquidbounce/features/command/CommandManager;", "getCommandManager", "()Lnet/ccbluex/liquidbounce/features/command/CommandManager;", "setCommandManager", "(Lnet/ccbluex/liquidbounce/features/command/CommandManager;)V", "eventManager", "Lnet/ccbluex/liquidbounce/event/EventManager;", "getEventManager", "()Lnet/ccbluex/liquidbounce/event/EventManager;", "setEventManager", "(Lnet/ccbluex/liquidbounce/event/EventManager;)V", "fileManager", "Lnet/ccbluex/liquidbounce/file/FileManager;", "getFileManager", "()Lnet/ccbluex/liquidbounce/file/FileManager;", "setFileManager", "(Lnet/ccbluex/liquidbounce/file/FileManager;)V", "hud", "Lnet/ccbluex/liquidbounce/ui/client/hud/HUD;", "getHud", "()Lnet/ccbluex/liquidbounce/ui/client/hud/HUD;", "setHud", "(Lnet/ccbluex/liquidbounce/ui/client/hud/HUD;)V", "isStarting", "()Z", "setStarting", "(Z)V", "latestVersion", "getLatestVersion", "()I", "setLatestVersion", "(I)V", "mainMenu", "Lnet/minecraft/client/gui/GuiScreen;", "getMainMenu", "()Lnet/minecraft/client/gui/GuiScreen;", "setMainMenu", "(Lnet/minecraft/client/gui/GuiScreen;)V", "moduleManager", "Lnet/ccbluex/liquidbounce/features/module/ModuleManager;", "getModuleManager", "()Lnet/ccbluex/liquidbounce/features/module/ModuleManager;", "setModuleManager", "(Lnet/ccbluex/liquidbounce/features/module/ModuleManager;)V", "scriptManager", "Lnet/ccbluex/liquidbounce/script/ScriptManager;", "getScriptManager", "()Lnet/ccbluex/liquidbounce/script/ScriptManager;", "setScriptManager", "(Lnet/ccbluex/liquidbounce/script/ScriptManager;)V", "tipSoundManager", "Lnet/ccbluex/liquidbounce/utils/misc/sound/TipSoundManager;", "getTipSoundManager", "()Lnet/ccbluex/liquidbounce/utils/misc/sound/TipSoundManager;", "setTipSoundManager", "(Lnet/ccbluex/liquidbounce/utils/misc/sound/TipSoundManager;)V", "toggleVolume", "", "getToggleVolume", "()F", "setToggleVolume", "(F)V", "trayIcon", "Ljava/awt/TrayIcon;", "getTrayIcon", "()Ljava/awt/TrayIcon;", "setTrayIcon", "(Ljava/awt/TrayIcon;)V", "windows", "getWindows", "wrapper", "Lnet/ccbluex/liquidbounce/api/Wrapper;", "getWrapper", "()Lnet/ccbluex/liquidbounce/api/Wrapper;", "setWrapper", "(Lnet/ccbluex/liquidbounce/api/Wrapper;)V", "displayTray", "", "Title", "Text", "type", "Ljava/awt/TrayIcon$MessageType;", "startClient", "stopClient", "Pride"})
public final class LiquidBounce {
    @NotNull
    public static final String CLIENT_NAME = "Pride";
    @NotNull
    public static final String CLIENT_NAME2 = "RedStar";
    public static final int CLIENT_VERSION = 2;
    public static final boolean IN_DEV = true;
    @NotNull
    public static final String CLIENT_CREATOR = "CCBlueX, YiTong";
    @NotNull
    public static GuiScreen mainMenu;
    @NotNull
    public static final String MINECRAFT_VERSION = "1.12.2";
    @NotNull
    public static final String CLIENT_CLOUD = "https://cloud.liquidbounce.net/LiquidBounce";
    @NotNull
    private static final ArrayList<String> UPDATE_LIST;
    private static boolean isStarting;
    @NotNull
    public static ModuleManager moduleManager;
    @NotNull
    public static CommandManager commandManager;
    @NotNull
    public static EventManager eventManager;
    @NotNull
    public static FileManager fileManager;
    @NotNull
    public static ScriptManager scriptManager;
    @NotNull
    public static CombatManager combatManager;
    @NotNull
    public static TipSoundManager tipSoundManager;
    private static float toggleVolume;
    private static final boolean windows;
    @Nullable
    private static TrayIcon trayIcon;
    @NotNull
    public static HUD hud;
    @NotNull
    public static AnimationHandler animationHandler;
    @NotNull
    public static ClickGui clickGui;
    private static int latestVersion;
    @Nullable
    private static IResourceLocation background;
    @NotNull
    public static ClientRichPresence clientRichPresence;
    @NotNull
    public static Wrapper wrapper;
    public static final LiquidBounce INSTANCE;

    @NotNull
    public final GuiScreen getMainMenu() {
        GuiScreen guiScreen = mainMenu;
        if (guiScreen == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mainMenu");
        }
        return guiScreen;
    }

    public final void setMainMenu(@NotNull GuiScreen guiScreen) {
        Intrinsics.checkParameterIsNotNull(guiScreen, "<set-?>");
        mainMenu = guiScreen;
    }

    @NotNull
    public final ArrayList<String> getUPDATE_LIST() {
        return UPDATE_LIST;
    }

    public final boolean isStarting() {
        return isStarting;
    }

    public final void setStarting(boolean bl) {
        isStarting = bl;
    }

    @NotNull
    public final ModuleManager getModuleManager() {
        ModuleManager moduleManager = LiquidBounce.moduleManager;
        if (moduleManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("moduleManager");
        }
        return moduleManager;
    }

    public final void setModuleManager(@NotNull ModuleManager moduleManager) {
        Intrinsics.checkParameterIsNotNull(moduleManager, "<set-?>");
        LiquidBounce.moduleManager = moduleManager;
    }

    @NotNull
    public final CommandManager getCommandManager() {
        CommandManager commandManager = LiquidBounce.commandManager;
        if (commandManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("commandManager");
        }
        return commandManager;
    }

    public final void setCommandManager(@NotNull CommandManager commandManager) {
        Intrinsics.checkParameterIsNotNull(commandManager, "<set-?>");
        LiquidBounce.commandManager = commandManager;
    }

    @NotNull
    public final EventManager getEventManager() {
        EventManager eventManager = LiquidBounce.eventManager;
        if (eventManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("eventManager");
        }
        return eventManager;
    }

    public final void setEventManager(@NotNull EventManager eventManager) {
        Intrinsics.checkParameterIsNotNull(eventManager, "<set-?>");
        LiquidBounce.eventManager = eventManager;
    }

    @NotNull
    public final FileManager getFileManager() {
        FileManager fileManager = LiquidBounce.fileManager;
        if (fileManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fileManager");
        }
        return fileManager;
    }

    public final void setFileManager(@NotNull FileManager fileManager) {
        Intrinsics.checkParameterIsNotNull(fileManager, "<set-?>");
        LiquidBounce.fileManager = fileManager;
    }

    @NotNull
    public final ScriptManager getScriptManager() {
        ScriptManager scriptManager = LiquidBounce.scriptManager;
        if (scriptManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptManager");
        }
        return scriptManager;
    }

    public final void setScriptManager(@NotNull ScriptManager scriptManager) {
        Intrinsics.checkParameterIsNotNull(scriptManager, "<set-?>");
        LiquidBounce.scriptManager = scriptManager;
    }

    @NotNull
    public final CombatManager getCombatManager() {
        CombatManager combatManager = LiquidBounce.combatManager;
        if (combatManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("combatManager");
        }
        return combatManager;
    }

    public final void setCombatManager(@NotNull CombatManager combatManager) {
        Intrinsics.checkParameterIsNotNull(combatManager, "<set-?>");
        LiquidBounce.combatManager = combatManager;
    }

    @NotNull
    public final TipSoundManager getTipSoundManager() {
        TipSoundManager tipSoundManager = LiquidBounce.tipSoundManager;
        if (tipSoundManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tipSoundManager");
        }
        return tipSoundManager;
    }

    public final void setTipSoundManager(@NotNull TipSoundManager tipSoundManager) {
        Intrinsics.checkParameterIsNotNull(tipSoundManager, "<set-?>");
        LiquidBounce.tipSoundManager = tipSoundManager;
    }

    public final float getToggleVolume() {
        return toggleVolume;
    }

    public final void setToggleVolume(float f) {
        toggleVolume = f;
    }

    public final boolean getWindows() {
        return windows;
    }

    @Nullable
    public final TrayIcon getTrayIcon() {
        return trayIcon;
    }

    public final void setTrayIcon(@Nullable TrayIcon trayIcon) {
        LiquidBounce.trayIcon = trayIcon;
    }

    @NotNull
    public final HUD getHud() {
        HUD hUD = hud;
        if (hUD == null) {
            Intrinsics.throwUninitializedPropertyAccessException("hud");
        }
        return hUD;
    }

    public final void setHud(@NotNull HUD hUD) {
        Intrinsics.checkParameterIsNotNull(hUD, "<set-?>");
        hud = hUD;
    }

    @NotNull
    public final AnimationHandler getAnimationHandler() {
        AnimationHandler animationHandler = LiquidBounce.animationHandler;
        if (animationHandler == null) {
            Intrinsics.throwUninitializedPropertyAccessException("animationHandler");
        }
        return animationHandler;
    }

    public final void setAnimationHandler(@NotNull AnimationHandler animationHandler) {
        Intrinsics.checkParameterIsNotNull(animationHandler, "<set-?>");
        LiquidBounce.animationHandler = animationHandler;
    }

    @NotNull
    public final ClickGui getClickGui() {
        ClickGui clickGui = LiquidBounce.clickGui;
        if (clickGui == null) {
            Intrinsics.throwUninitializedPropertyAccessException("clickGui");
        }
        return clickGui;
    }

    public final void setClickGui(@NotNull ClickGui clickGui) {
        Intrinsics.checkParameterIsNotNull(clickGui, "<set-?>");
        LiquidBounce.clickGui = clickGui;
    }

    public final int getLatestVersion() {
        return latestVersion;
    }

    public final void setLatestVersion(int n) {
        latestVersion = n;
    }

    @Nullable
    public final IResourceLocation getBackground() {
        return background;
    }

    public final void setBackground(@Nullable IResourceLocation iResourceLocation) {
        background = iResourceLocation;
    }

    @NotNull
    public final ClientRichPresence getClientRichPresence() {
        ClientRichPresence clientRichPresence = LiquidBounce.clientRichPresence;
        if (clientRichPresence == null) {
            Intrinsics.throwUninitializedPropertyAccessException("clientRichPresence");
        }
        return clientRichPresence;
    }

    public final void setClientRichPresence(@NotNull ClientRichPresence clientRichPresence) {
        Intrinsics.checkParameterIsNotNull(clientRichPresence, "<set-?>");
        LiquidBounce.clientRichPresence = clientRichPresence;
    }

    @NotNull
    public final Wrapper getWrapper() {
        Wrapper wrapper = LiquidBounce.wrapper;
        if (wrapper == null) {
            Intrinsics.throwUninitializedPropertyAccessException("wrapper");
        }
        return wrapper;
    }

    public final void setWrapper(@NotNull Wrapper wrapper) {
        Intrinsics.checkParameterIsNotNull(wrapper, "<set-?>");
        LiquidBounce.wrapper = wrapper;
    }

    private final void displayTray(String Title, String Text, TrayIcon.MessageType type) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);
        trayIcon.displayMessage(Title, Text, type);
    }

    public final void startClient() {
        isStarting = true;
        ClientUtils.getLogger().info("Starting RedStar B2, by CCBlueX, YiTong");
        fileManager = new FileManager();
        EventManager eventManager = LiquidBounce.eventManager = new EventManager();
        if (eventManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("eventManager");
        }
        eventManager.registerListener(new RotationUtils());
        EventManager eventManager2 = LiquidBounce.eventManager;
        if (eventManager2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("eventManager");
        }
        eventManager2.registerListener(new AntiForge());
        EventManager eventManager3 = LiquidBounce.eventManager;
        if (eventManager3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("eventManager");
        }
        eventManager3.registerListener(new BungeeCordSpoof());
        EventManager eventManager4 = LiquidBounce.eventManager;
        if (eventManager4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("eventManager");
        }
        eventManager4.registerListener(new DonatorCape());
        EventManager eventManager5 = LiquidBounce.eventManager;
        if (eventManager5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("eventManager");
        }
        eventManager5.registerListener(new InventoryUtils());
        tipSoundManager = new TipSoundManager();
        clientRichPresence = new ClientRichPresence();
        commandManager = new CommandManager();
        Fonts.loadFonts();
        ModuleManager moduleManager = LiquidBounce.moduleManager = new ModuleManager();
        if (moduleManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("moduleManager");
        }
        moduleManager.registerModules();
        animationHandler = new AnimationHandler();
        try {
            Remapper.INSTANCE.loadSrg();
            ScriptManager scriptManager = LiquidBounce.scriptManager = new ScriptManager();
            if (scriptManager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("scriptManager");
            }
            scriptManager.loadScripts();
            ScriptManager scriptManager2 = LiquidBounce.scriptManager;
            if (scriptManager2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("scriptManager");
            }
            scriptManager2.enableScripts();
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("Failed to load scripts.", throwable);
        }
        CommandManager commandManager = LiquidBounce.commandManager;
        if (commandManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("commandManager");
        }
        commandManager.registerCommands();
        FileManager fileManager = LiquidBounce.fileManager;
        if (fileManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fileManager");
        }
        FileConfig[] fileConfigArray = new FileConfig[6];
        FileManager fileManager2 = LiquidBounce.fileManager;
        if (fileManager2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fileManager");
        }
        fileConfigArray[0] = fileManager2.modulesConfig;
        FileManager fileManager3 = LiquidBounce.fileManager;
        if (fileManager3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fileManager");
        }
        fileConfigArray[1] = fileManager3.valuesConfig;
        FileManager fileManager4 = LiquidBounce.fileManager;
        if (fileManager4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fileManager");
        }
        fileConfigArray[2] = fileManager4.accountsConfig;
        FileManager fileManager5 = LiquidBounce.fileManager;
        if (fileManager5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fileManager");
        }
        fileConfigArray[3] = fileManager5.friendsConfig;
        FileManager fileManager6 = LiquidBounce.fileManager;
        if (fileManager6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fileManager");
        }
        fileConfigArray[4] = fileManager6.xrayConfig;
        FileManager fileManager7 = LiquidBounce.fileManager;
        if (fileManager7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fileManager");
        }
        fileConfigArray[5] = fileManager7.shortcutsConfig;
        fileManager.loadConfigs(fileConfigArray);
        clickGui = new ClickGui();
        FileManager fileManager8 = LiquidBounce.fileManager;
        if (fileManager8 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fileManager");
        }
        FileManager fileManager9 = LiquidBounce.fileManager;
        if (fileManager9 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fileManager");
        }
        fileManager8.loadConfig(fileManager9.clickGuiConfig);
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
        FileManager fileManager10 = LiquidBounce.fileManager;
        if (fileManager10 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fileManager");
        }
        FileManager fileManager11 = LiquidBounce.fileManager;
        if (fileManager11 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fileManager");
        }
        fileManager10.loadConfig(fileManager11.hudConfig);
        ClientUtils.disableFastRender();
        try {
            JsonElement jsonObj = new JsonParser().parse(HttpUtils.get("https://cloud.liquidbounce.net/LiquidBounce/versions.json"));
            if (jsonObj instanceof JsonObject && ((JsonObject)jsonObj).has(MINECRAFT_VERSION)) {
                JsonElement jsonElement = ((JsonObject)jsonObj).get(MINECRAFT_VERSION);
                Intrinsics.checkExpressionValueIsNotNull(jsonElement, "jsonObj[MINECRAFT_VERSION]");
                latestVersion = jsonElement.getAsInt();
            }
        }
        catch (Throwable exception) {
            ClientUtils.getLogger().error("Failed to check for updates.", exception);
        }
        GuiAltManager.loadGenerators();
        if (windows && SystemTray.isSupported()) {
            try {
                trayIcon = new TrayIcon(ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/assets/minecraft/pride/icon128.png"))));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            TrayIcon trayIcon = LiquidBounce.trayIcon;
            if (trayIcon != null) {
                trayIcon.setImageAutoSize(true);
            }
            TrayIcon trayIcon2 = LiquidBounce.trayIcon;
            if (trayIcon2 != null) {
                trayIcon2.setToolTip(CLIENT_NAME2);
            }
            try {
                SystemTray.getSystemTray().add(LiquidBounce.trayIcon);
            }
            catch (AWTException aWTException) {
                // empty catch block
            }
            TrayIcon trayIcon3 = LiquidBounce.trayIcon;
            if (trayIcon3 != null) {
                trayIcon3.displayMessage(CLIENT_NAME2, "感谢你对RedStar的支持", TrayIcon.MessageType.NONE);
            }
            Sound.notificationsAllowed(true);
        }
        new Sound();
        ClientRichPresence clientRichPresence = LiquidBounce.clientRichPresence;
        if (clientRichPresence == null) {
            Intrinsics.throwUninitializedPropertyAccessException("clientRichPresence");
        }
        if (clientRichPresence.getShowRichPresenceValue()) {
            ThreadsKt.thread$default(false, false, null, null, 0, startClient.1.INSTANCE, 31, null);
        }
        isStarting = false;
    }

    public final void stopClient() {
        EventManager eventManager = LiquidBounce.eventManager;
        if (eventManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("eventManager");
        }
        eventManager.callEvent(new ClientShutdownEvent());
        FileManager fileManager = LiquidBounce.fileManager;
        if (fileManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fileManager");
        }
        fileManager.saveAllConfigs();
        ClientRichPresence clientRichPresence = LiquidBounce.clientRichPresence;
        if (clientRichPresence == null) {
            Intrinsics.throwUninitializedPropertyAccessException("clientRichPresence");
        }
        clientRichPresence.shutdown();
    }

    private LiquidBounce() {
    }

    static {
        LiquidBounce liquidBounce;
        INSTANCE = liquidBounce = new LiquidBounce();
        UPDATE_LIST = CollectionsKt.arrayListOf("QQ Group 462907867 ", "Update Logs : ", "Build 30", "1.Fix KillAura AutoBlock", "2. add KillAura AttackEffect", "3.add AttackEffects", "4.add OldHitting mode ", "5.add KillAura Rotations", "6.Better Visual", "Build 31", "Better Visula");
        String string = System.getProperties().getProperty("os.name");
        Intrinsics.checkExpressionValueIsNotNull(string, "System.getProperties().getProperty(\"os.name\")");
        String string2 = string;
        boolean bl = false;
        String string3 = string2;
        if (string3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string4 = string3.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.String).toLowerCase()");
        windows = StringsKt.contains$default((CharSequence)string4, "windows", false, 2, null);
    }
}
