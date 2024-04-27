/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  qiriyou.verV3Z.Loader
 */
package net.ccbluex.liquidbounce;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.Wrapper;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.event.EventManager;
import net.ccbluex.liquidbounce.features.command.CommandManager;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.file.FileManager;
import net.ccbluex.liquidbounce.management.CombatManager;
import net.ccbluex.liquidbounce.script.ScriptManager;
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui;
import net.ccbluex.liquidbounce.ui.client.hud.HUD;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import qiriyou.verV3Z.Loader;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000|\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010]\u001a\u00020^J\u000e\u0010_\u001a\u00020^2\u0006\u0010`\u001a\u00020\u0004J\u0006\u0010a\u001a\u00020^R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001c\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\u00020\u0014X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u001a\u0010\u0019\u001a\u00020\u001aX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001a\u0010\u001f\u001a\u00020 X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\u001a\u0010%\u001a\u00020&X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b'\u0010(\"\u0004\b)\u0010*R\u001a\u0010+\u001a\u00020,X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b-\u0010.\"\u0004\b/\u00100R\u001a\u00101\u001a\u000202X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b3\u00104\"\u0004\b5\u00106R\u001a\u00107\u001a\u000208X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b7\u00109\"\u0004\b:\u0010;R\u001a\u0010<\u001a\u000208X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b=\u00109\"\u0004\b>\u0010;R\u001a\u0010?\u001a\u00020@X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\bA\u0010B\"\u0004\bC\u0010DR\u001a\u0010E\u001a\u00020FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bG\u0010H\"\u0004\bI\u0010JR\u001a\u0010K\u001a\u00020\u0004X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\bL\u0010\n\"\u0004\bM\u0010\fR\u001a\u0010N\u001a\u00020OX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\bP\u0010Q\"\u0004\bR\u0010SR\u001a\u0010T\u001a\u00020\u0004X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\bU\u0010\n\"\u0004\bV\u0010\fR\u001a\u0010W\u001a\u00020XX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\bY\u0010Z\"\u0004\b[\u0010\\\u00a8\u0006b"}, d2={"Lnet/ccbluex/liquidbounce/LiquidBounce;", "", "()V", "CLIENT_CLOUD", "", "CLIENT_CREATOR", "CLIENT_NAME", "CLIENT_VERSION", "USERNAME", "getUSERNAME", "()Ljava/lang/String;", "setUSERNAME", "(Ljava/lang/String;)V", "background", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "getBackground", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "setBackground", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;)V", "clickGui", "Lnet/ccbluex/liquidbounce/ui/client/clickgui/ClickGui;", "getClickGui", "()Lnet/ccbluex/liquidbounce/ui/client/clickgui/ClickGui;", "setClickGui", "(Lnet/ccbluex/liquidbounce/ui/client/clickgui/ClickGui;)V", "combatManager", "Lnet/ccbluex/liquidbounce/management/CombatManager;", "getCombatManager", "()Lnet/ccbluex/liquidbounce/management/CombatManager;", "setCombatManager", "(Lnet/ccbluex/liquidbounce/management/CombatManager;)V", "commandManager", "Lnet/ccbluex/liquidbounce/features/command/CommandManager;", "getCommandManager", "()Lnet/ccbluex/liquidbounce/features/command/CommandManager;", "setCommandManager", "(Lnet/ccbluex/liquidbounce/features/command/CommandManager;)V", "eventManager", "Lnet/ccbluex/liquidbounce/event/EventManager;", "getEventManager", "()Lnet/ccbluex/liquidbounce/event/EventManager;", "setEventManager", "(Lnet/ccbluex/liquidbounce/event/EventManager;)V", "fileManager", "Lnet/ccbluex/liquidbounce/file/FileManager;", "getFileManager", "()Lnet/ccbluex/liquidbounce/file/FileManager;", "setFileManager", "(Lnet/ccbluex/liquidbounce/file/FileManager;)V", "hud", "Lnet/ccbluex/liquidbounce/ui/client/hud/HUD;", "getHud", "()Lnet/ccbluex/liquidbounce/ui/client/hud/HUD;", "setHud", "(Lnet/ccbluex/liquidbounce/ui/client/hud/HUD;)V", "isStarting", "", "()Z", "setStarting", "(Z)V", "mainMenuPrep", "getMainMenuPrep", "setMainMenuPrep", "moduleManager", "Lnet/ccbluex/liquidbounce/features/module/ModuleManager;", "getModuleManager", "()Lnet/ccbluex/liquidbounce/features/module/ModuleManager;", "setModuleManager", "(Lnet/ccbluex/liquidbounce/features/module/ModuleManager;)V", "playTimeStart", "", "getPlayTimeStart", "()J", "setPlayTimeStart", "(J)V", "qq", "getQq", "setQq", "scriptManager", "Lnet/ccbluex/liquidbounce/script/ScriptManager;", "getScriptManager", "()Lnet/ccbluex/liquidbounce/script/ScriptManager;", "setScriptManager", "(Lnet/ccbluex/liquidbounce/script/ScriptManager;)V", "user", "getUser", "setUser", "wrapper", "Lnet/ccbluex/liquidbounce/api/Wrapper;", "getWrapper", "()Lnet/ccbluex/liquidbounce/api/Wrapper;", "setWrapper", "(Lnet/ccbluex/liquidbounce/api/Wrapper;)V", "initClient", "", "setUsername", "username", "stopClient", "LiKingSense"})
public final class LiquidBounce {
    @NotNull
    public static final String CLIENT_NAME = "LiKingSense";
    @NotNull
    public static final String CLIENT_VERSION = "";
    @NotNull
    public static final String CLIENT_CREATOR = "CCBlueX";
    @NotNull
    public static final String CLIENT_CLOUD = "https://cloud.liquidbounce.net/LiquidBounce";
    private static boolean mainMenuPrep;
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
    public static String user;
    @NotNull
    public static String qq;
    @NotNull
    private static String USERNAME;
    private static long playTimeStart;
    @NotNull
    public static HUD hud;
    @NotNull
    public static ClickGui clickGui;
    @Nullable
    private static IResourceLocation background;
    @NotNull
    public static Wrapper wrapper;
    public static final LiquidBounce INSTANCE;

    public final native boolean getMainMenuPrep();

    public final native void setMainMenuPrep(boolean var1);

    public final native boolean isStarting();

    public final native void setStarting(boolean var1);

    @NotNull
    public final native ModuleManager getModuleManager();

    public final native void setModuleManager(@NotNull ModuleManager var1);

    @NotNull
    public final native CommandManager getCommandManager();

    public final native void setCommandManager(@NotNull CommandManager var1);

    @NotNull
    public final native EventManager getEventManager();

    public final native void setEventManager(@NotNull EventManager var1);

    @NotNull
    public final native FileManager getFileManager();

    public final native void setFileManager(@NotNull FileManager var1);

    @NotNull
    public final native ScriptManager getScriptManager();

    public final native void setScriptManager(@NotNull ScriptManager var1);

    @NotNull
    public final native CombatManager getCombatManager();

    public final native void setCombatManager(@NotNull CombatManager var1);

    @NotNull
    public final native String getUser();

    public final native void setUser(@NotNull String var1);

    @NotNull
    public final native String getQq();

    public final native void setQq(@NotNull String var1);

    @NotNull
    public final native String getUSERNAME();

    public final native void setUSERNAME(@NotNull String var1);

    public final native void setUsername(@NotNull String var1);

    public final native long getPlayTimeStart();

    public final native void setPlayTimeStart(long var1);

    @NotNull
    public final native HUD getHud();

    public final native void setHud(@NotNull HUD var1);

    @NotNull
    public final native ClickGui getClickGui();

    public final native void setClickGui(@NotNull ClickGui var1);

    @Nullable
    public final native IResourceLocation getBackground();

    public final native void setBackground(@Nullable IResourceLocation var1);

    @NotNull
    public final native Wrapper getWrapper();

    public final native void setWrapper(@NotNull Wrapper var1);

    public final native void initClient();

    public final native void stopClient();

    private LiquidBounce() {
    }

    static {
        Loader.registerNativesForClass((int)10, LiquidBounce.class);
        LiquidBounce.$qiriyouLoader();
        LiquidBounce.$qiriyouClinit();
    }

    public static native /* synthetic */ void $qiriyouLoader();

    private static native /* synthetic */ void $qiriyouClinit();
}

