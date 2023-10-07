/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.text.SimpleDateFormat;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.cape.CapeAPI;
import net.ccbluex.liquidbounce.discord.ClientRichPresence;
import net.ccbluex.liquidbounce.event.ClientShutdownEvent;
import net.ccbluex.liquidbounce.event.EventManager;
import net.ccbluex.liquidbounce.features.command.CommandManager;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.ccbluex.liquidbounce.features.special.BungeeCordSpoof;
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
import net.ccbluex.liquidbounce.utils.misc.SoundFxPlayer;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u008a\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010r\u001a\u00020sJ\u0006\u0010t\u001a\u00020sR\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u0004X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0010\"\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0015\u001a\u00020\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u000e\u0010\u0019\u001a\u00020\u001aX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u001c\u001a\u0004\u0018\u00010\u001dX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u001a\u0010\"\u001a\u00020#X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010%\"\u0004\b&\u0010'R\u000e\u0010(\u001a\u00020)X\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010*\u001a\u00020+X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b,\u0010-\"\u0004\b.\u0010/R\u0019\u00100\u001a\n 1*\u0004\u0018\u00010\u00040\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010\u0010R\u0019\u00103\u001a\n 1*\u0004\u0018\u00010\u00040\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u0010\u0010R\u001a\u00105\u001a\u000206X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b7\u00108\"\u0004\b9\u0010:R\u001a\u0010;\u001a\u00020<X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b=\u0010>\"\u0004\b?\u0010@R\u001a\u0010A\u001a\u00020BX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\bC\u0010D\"\u0004\bE\u0010FR\u001a\u0010G\u001a\u00020\u001aX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bG\u0010H\"\u0004\bI\u0010JR\u001a\u0010K\u001a\u00020LX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bM\u0010N\"\u0004\bO\u0010PR\u001a\u0010Q\u001a\u00020RX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bS\u0010T\"\u0004\bU\u0010VR\u001a\u0010W\u001a\u00020XX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bY\u0010Z\"\u0004\b[\u0010\\R\u001a\u0010]\u001a\u00020\u001aX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b^\u0010H\"\u0004\b_\u0010JR\u001a\u0010`\u001a\u00020aX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\bb\u0010c\"\u0004\bd\u0010eR\u001a\u0010f\u001a\u00020RX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bg\u0010T\"\u0004\bh\u0010VR\u001a\u0010i\u001a\u00020jX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\bk\u0010l\"\u0004\bm\u0010nR\u001c\u0010o\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bp\u0010\u0010\"\u0004\bq\u0010\u0014\u00a8\u0006u"}, d2={"Lnet/ccbluex/liquidbounce/LiquidBounce;", "", "()V", "CLIENT_CHAT", "", "CLIENT_CLOUD", "CLIENT_CLOUD5", "CLIENT_CREATOR1", "CLIENT_CREDITS", "CLIENT_NAME", "CLIENT_PASTENAME", "CLIENT_TEAM", "CLIENT_VERSIONS", "COLORED_NAME", "DATE", "getDATE", "()Ljava/lang/String;", "DATE2", "getDATE2", "setDATE2", "(Ljava/lang/String;)V", "DATE_FORMAT", "Ljava/text/SimpleDateFormat;", "getDATE_FORMAT", "()Ljava/text/SimpleDateFormat;", "IN_DEV", "", "MINECRAFT_VERSION", "background", "Lnet/minecraft/util/ResourceLocation;", "getBackground", "()Lnet/minecraft/util/ResourceLocation;", "setBackground", "(Lnet/minecraft/util/ResourceLocation;)V", "clickGui", "Lnet/ccbluex/liquidbounce/ui/client/clickgui/ClickGui;", "getClickGui", "()Lnet/ccbluex/liquidbounce/ui/client/clickgui/ClickGui;", "setClickGui", "(Lnet/ccbluex/liquidbounce/ui/client/clickgui/ClickGui;)V", "clientRichPresence", "Lnet/ccbluex/liquidbounce/discord/ClientRichPresence;", "commandManager", "Lnet/ccbluex/liquidbounce/features/command/CommandManager;", "getCommandManager", "()Lnet/ccbluex/liquidbounce/features/command/CommandManager;", "setCommandManager", "(Lnet/ccbluex/liquidbounce/features/command/CommandManager;)V", "datee", "kotlin.jvm.PlatformType", "getDatee", "datee2", "getDatee2", "eventManager", "Lnet/ccbluex/liquidbounce/event/EventManager;", "getEventManager", "()Lnet/ccbluex/liquidbounce/event/EventManager;", "setEventManager", "(Lnet/ccbluex/liquidbounce/event/EventManager;)V", "fileManager", "Lnet/ccbluex/liquidbounce/file/FileManager;", "getFileManager", "()Lnet/ccbluex/liquidbounce/file/FileManager;", "setFileManager", "(Lnet/ccbluex/liquidbounce/file/FileManager;)V", "hud", "Lnet/ccbluex/liquidbounce/ui/client/hud/HUD;", "getHud", "()Lnet/ccbluex/liquidbounce/ui/client/hud/HUD;", "setHud", "(Lnet/ccbluex/liquidbounce/ui/client/hud/HUD;)V", "isStarting", "()Z", "setStarting", "(Z)V", "lastTPS", "", "getLastTPS", "()F", "setLastTPS", "(F)V", "lastTick", "", "getLastTick", "()J", "setLastTick", "(J)V", "latestVersion", "", "getLatestVersion", "()I", "setLatestVersion", "(I)V", "mainMenuPrep", "getMainMenuPrep", "setMainMenuPrep", "moduleManager", "Lnet/ccbluex/liquidbounce/features/module/ModuleManager;", "getModuleManager", "()Lnet/ccbluex/liquidbounce/features/module/ModuleManager;", "setModuleManager", "(Lnet/ccbluex/liquidbounce/features/module/ModuleManager;)V", "playTimeStart", "getPlayTimeStart", "setPlayTimeStart", "scriptManager", "Lnet/ccbluex/liquidbounce/script/ScriptManager;", "getScriptManager", "()Lnet/ccbluex/liquidbounce/script/ScriptManager;", "setScriptManager", "(Lnet/ccbluex/liquidbounce/script/ScriptManager;)V", "username", "getUsername", "setUsername", "startClient", "", "stopClient", "KyinoClient"})
public final class LiquidBounce {
    @NotNull
    private static final SimpleDateFormat DATE_FORMAT;
    @NotNull
    public static final String CLIENT_NAME = "LiquidBounce";
    @NotNull
    public static final String CLIENT_PASTENAME = "KyinoClient";
    @NotNull
    public static final String CLIENT_VERSIONS = "17.1";
    @NotNull
    public static final String CLIENT_CHAT = "\u00a7c\u00a7l>> ";
    public static final boolean IN_DEV = false;
    private static final String datee;
    private static final String datee2;
    @NotNull
    public static String DATE2;
    @NotNull
    public static final String COLORED_NAME = "KyinoClient";
    @NotNull
    public static final String CLIENT_CREATOR1 = "Report.";
    @NotNull
    public static final String MINECRAFT_VERSION = "1.8.9";
    @NotNull
    public static final String CLIENT_TEAM = "Report.Team";
    @NotNull
    public static final String CLIENT_CREDITS = "KyinoClient is developed by The Report.Team";
    @NotNull
    public static final String CLIENT_CLOUD = "https://cloud.liquidbounce.net/LiquidBounce";
    @NotNull
    public static final String CLIENT_CLOUD5 = "https://liquidbounceplusreborn.github.io/cloud/";
    private static long lastTick;
    private static long playTimeStart;
    private static boolean isStarting;
    private static boolean mainMenuPrep;
    private static float lastTPS;
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
    public static HUD hud;
    @NotNull
    public static ClickGui clickGui;
    private static int latestVersion;
    @Nullable
    private static ResourceLocation background;
    private static ClientRichPresence clientRichPresence;
    @Nullable
    private static String username;
    public static final LiquidBounce INSTANCE;

    @NotNull
    public final SimpleDateFormat getDATE_FORMAT() {
        return DATE_FORMAT;
    }

    public final String getDatee() {
        return datee;
    }

    @Nullable
    public final String getDATE() {
        return datee;
    }

    public final String getDatee2() {
        return datee2;
    }

    @NotNull
    public final String getDATE2() {
        String string = DATE2;
        if (string == null) {
            Intrinsics.throwUninitializedPropertyAccessException("DATE2");
        }
        return string;
    }

    public final void setDATE2(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull(string, "<set-?>");
        DATE2 = string;
    }

    public final long getLastTick() {
        return lastTick;
    }

    public final void setLastTick(long l) {
        lastTick = l;
    }

    public final long getPlayTimeStart() {
        return playTimeStart;
    }

    public final void setPlayTimeStart(long l) {
        playTimeStart = l;
    }

    public final boolean isStarting() {
        return isStarting;
    }

    public final void setStarting(boolean bl) {
        isStarting = bl;
    }

    public final boolean getMainMenuPrep() {
        return mainMenuPrep;
    }

    public final void setMainMenuPrep(boolean bl) {
        mainMenuPrep = bl;
    }

    public final float getLastTPS() {
        return lastTPS;
    }

    public final void setLastTPS(float f) {
        lastTPS = f;
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
    public final ClickGui getClickGui() {
        ClickGui clickGui = LiquidBounce.clickGui;
        if (clickGui == null) {
            Intrinsics.throwUninitializedPropertyAccessException("clickGui");
        }
        return clickGui;
    }

    public final void setClickGui(@NotNull ClickGui clickGui) {
        Intrinsics.checkParameterIsNotNull((Object)clickGui, "<set-?>");
        LiquidBounce.clickGui = clickGui;
    }

    public final int getLatestVersion() {
        return latestVersion;
    }

    public final void setLatestVersion(int n) {
        latestVersion = n;
    }

    @Nullable
    public final ResourceLocation getBackground() {
        return background;
    }

    public final void setBackground(@Nullable ResourceLocation resourceLocation) {
        background = resourceLocation;
    }

    public final void startClient() {
        isStarting = true;
        lastTick = System.currentTimeMillis();
        ClientUtils.getLogger().info("Finished loading KyinoClient in " + (System.currentTimeMillis() - lastTick) + "ms. b17.1, by: Report.");
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
        commandManager = new CommandManager();
        Fonts.loadFonts();
        ModuleManager moduleManager = LiquidBounce.moduleManager = new ModuleManager();
        if (moduleManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("moduleManager");
        }
        moduleManager.registerModules();
        try {
            Remapper.INSTANCE.loadSrg();
            ScriptManager scriptManager = LiquidBounce.scriptManager = new ScriptManager();
            if (scriptManager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("scriptManager");
            }
            scriptManager.enableScripts();
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
        try {
            ClientRichPresence clientRichPresence = LiquidBounce.clientRichPresence = new ClientRichPresence();
            if (clientRichPresence == null) {
                Intrinsics.throwUninitializedPropertyAccessException("clientRichPresence");
            }
            clientRichPresence.setup();
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("Failed to setup Discord RPC.", throwable);
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
        new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.SPECIAL, 8.0f);
        GuiAltManager.loadGenerators();
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

    @Nullable
    public final String getUsername() {
        return username;
    }

    public final void setUsername(@Nullable String string) {
        username = string;
    }

    private LiquidBounce() {
    }

    static {
        LiquidBounce liquidBounce;
        INSTANCE = liquidBounce = new LiquidBounce();
        DATE_FORMAT = new SimpleDateFormat("yyyy MM dd");
        datee = DATE_FORMAT.format(System.currentTimeMillis());
        datee2 = DATE_FORMAT.format(System.currentTimeMillis());
        lastTPS = 20.0f;
    }
}

