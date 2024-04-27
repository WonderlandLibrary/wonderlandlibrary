/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important;

import java.util.Locale;
import javax.swing.JOptionPane;
import kotlin.Metadata;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.ClientPresence;
import net.dev.important.event.ClientShutdownEvent;
import net.dev.important.event.EventManager;
import net.dev.important.file.FileConfig;
import net.dev.important.file.FileManager;
import net.dev.important.gui.client.altmanager.GuiAltManager;
import net.dev.important.gui.client.clickgui.ClickGui;
import net.dev.important.gui.client.hud.HUD;
import net.dev.important.gui.font.Fonts;
import net.dev.important.modules.command.CommandManager;
import net.dev.important.modules.command.other.AntiForge;
import net.dev.important.modules.command.other.BungeeCordSpoof;
import net.dev.important.modules.module.Manager;
import net.dev.important.script.ScriptManager;
import net.dev.important.script.remapper.Remapper;
import net.dev.important.utils.CheckUtils;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.InventoryHelper;
import net.dev.important.utils.InventoryUtils;
import net.dev.important.utils.PacketUtils;
import net.dev.important.utils.RotationUtils;
import net.dev.important.utils.SessionUtils;
import net.dev.important.utils.misc.HttpUtils;
import net.dev.important.utils.misc.sound.TipSoundManager;
import net.minecraft.util.ResourceLocation;
import oh.yalan.NativeClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NativeClass
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010L\u001a\u00020MJ\u0006\u0010N\u001a\u00020MR\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u0012X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0017\u001a\u00020\u0018X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001a\u0010\u001d\u001a\u00020\u001eX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001a\u0010#\u001a\u00020$X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b%\u0010&\"\u0004\b'\u0010(R\u001a\u0010)\u001a\u00020*X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\u001a\u0010/\u001a\u000200X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b/\u00101\"\u0004\b2\u00103R\u000e\u00104\u001a\u000205X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u00107\u001a\u000208X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b9\u0010:\"\u0004\b;\u0010<R\u000e\u0010=\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010>\u001a\u00020?X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b@\u0010A\"\u0004\bB\u0010CR\u001a\u0010D\u001a\u00020EX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\bF\u0010G\"\u0004\bH\u0010IR\u000e\u0010J\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010K\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006O"}, d2={"Lnet/dev/important/Client;", "", "()V", "NAME", "", "background", "Lnet/minecraft/util/ResourceLocation;", "getBackground", "()Lnet/minecraft/util/ResourceLocation;", "setBackground", "(Lnet/minecraft/util/ResourceLocation;)V", "clickGui", "Lnet/dev/important/gui/client/clickgui/ClickGui;", "getClickGui", "()Lnet/dev/important/gui/client/clickgui/ClickGui;", "setClickGui", "(Lnet/dev/important/gui/client/clickgui/ClickGui;)V", "clientPresence", "Lnet/dev/important/ClientPresence;", "getClientPresence", "()Lnet/dev/important/ClientPresence;", "setClientPresence", "(Lnet/dev/important/ClientPresence;)V", "commandManager", "Lnet/dev/important/modules/command/CommandManager;", "getCommandManager", "()Lnet/dev/important/modules/command/CommandManager;", "setCommandManager", "(Lnet/dev/important/modules/command/CommandManager;)V", "eventManager", "Lnet/dev/important/event/EventManager;", "getEventManager", "()Lnet/dev/important/event/EventManager;", "setEventManager", "(Lnet/dev/important/event/EventManager;)V", "fileManager", "Lnet/dev/important/file/FileManager;", "getFileManager", "()Lnet/dev/important/file/FileManager;", "setFileManager", "(Lnet/dev/important/file/FileManager;)V", "hud", "Lnet/dev/important/gui/client/hud/HUD;", "getHud", "()Lnet/dev/important/gui/client/hud/HUD;", "setHud", "(Lnet/dev/important/gui/client/hud/HUD;)V", "isStarting", "", "()Z", "setStarting", "(Z)V", "lastTick", "", "libdir", "moduleManager", "Lnet/dev/important/modules/module/Manager;", "getModuleManager", "()Lnet/dev/important/modules/module/Manager;", "setModuleManager", "(Lnet/dev/important/modules/module/Manager;)V", "pw", "scriptManager", "Lnet/dev/important/script/ScriptManager;", "getScriptManager", "()Lnet/dev/important/script/ScriptManager;", "setScriptManager", "(Lnet/dev/important/script/ScriptManager;)V", "tipSoundManager", "Lnet/dev/important/utils/misc/sound/TipSoundManager;", "getTipSoundManager", "()Lnet/dev/important/utils/misc/sound/TipSoundManager;", "setTipSoundManager", "(Lnet/dev/important/utils/misc/sound/TipSoundManager;)V", "ur", "users", "startClient", "", "stopClient", "LiquidBounce"})
public final class Client {
    @NotNull
    public static final Client INSTANCE = new Client();
    @NotNull
    public static final String NAME = "LiquidPlus";
    private static boolean isStarting;
    @NotNull
    private static final String users;
    @NotNull
    private static String libdir;
    @NotNull
    private static String pw;
    @NotNull
    private static String ur;
    public static Manager moduleManager;
    public static CommandManager commandManager;
    public static EventManager eventManager;
    public static FileManager fileManager;
    public static ScriptManager scriptManager;
    public static TipSoundManager tipSoundManager;
    public static HUD hud;
    public static ClickGui clickGui;
    @Nullable
    private static ResourceLocation background;
    public static ClientPresence clientPresence;
    private static long lastTick;

    private Client() {
    }

    public final boolean isStarting() {
        return isStarting;
    }

    public final void setStarting(boolean bl) {
        isStarting = bl;
    }

    @NotNull
    public final Manager getModuleManager() {
        Manager manager = moduleManager;
        if (manager != null) {
            return manager;
        }
        Intrinsics.throwUninitializedPropertyAccessException("moduleManager");
        return null;
    }

    public final void setModuleManager(@NotNull Manager manager) {
        Intrinsics.checkNotNullParameter(manager, "<set-?>");
        moduleManager = manager;
    }

    @NotNull
    public final CommandManager getCommandManager() {
        CommandManager commandManager = Client.commandManager;
        if (commandManager != null) {
            return commandManager;
        }
        Intrinsics.throwUninitializedPropertyAccessException("commandManager");
        return null;
    }

    public final void setCommandManager(@NotNull CommandManager commandManager) {
        Intrinsics.checkNotNullParameter(commandManager, "<set-?>");
        Client.commandManager = commandManager;
    }

    @NotNull
    public final EventManager getEventManager() {
        EventManager eventManager = Client.eventManager;
        if (eventManager != null) {
            return eventManager;
        }
        Intrinsics.throwUninitializedPropertyAccessException("eventManager");
        return null;
    }

    public final void setEventManager(@NotNull EventManager eventManager) {
        Intrinsics.checkNotNullParameter(eventManager, "<set-?>");
        Client.eventManager = eventManager;
    }

    @NotNull
    public final FileManager getFileManager() {
        FileManager fileManager = Client.fileManager;
        if (fileManager != null) {
            return fileManager;
        }
        Intrinsics.throwUninitializedPropertyAccessException("fileManager");
        return null;
    }

    public final void setFileManager(@NotNull FileManager fileManager) {
        Intrinsics.checkNotNullParameter(fileManager, "<set-?>");
        Client.fileManager = fileManager;
    }

    @NotNull
    public final ScriptManager getScriptManager() {
        ScriptManager scriptManager = Client.scriptManager;
        if (scriptManager != null) {
            return scriptManager;
        }
        Intrinsics.throwUninitializedPropertyAccessException("scriptManager");
        return null;
    }

    public final void setScriptManager(@NotNull ScriptManager scriptManager) {
        Intrinsics.checkNotNullParameter(scriptManager, "<set-?>");
        Client.scriptManager = scriptManager;
    }

    @NotNull
    public final TipSoundManager getTipSoundManager() {
        TipSoundManager tipSoundManager = Client.tipSoundManager;
        if (tipSoundManager != null) {
            return tipSoundManager;
        }
        Intrinsics.throwUninitializedPropertyAccessException("tipSoundManager");
        return null;
    }

    public final void setTipSoundManager(@NotNull TipSoundManager tipSoundManager) {
        Intrinsics.checkNotNullParameter(tipSoundManager, "<set-?>");
        Client.tipSoundManager = tipSoundManager;
    }

    @NotNull
    public final HUD getHud() {
        HUD hUD = hud;
        if (hUD != null) {
            return hUD;
        }
        Intrinsics.throwUninitializedPropertyAccessException("hud");
        return null;
    }

    public final void setHud(@NotNull HUD hUD) {
        Intrinsics.checkNotNullParameter(hUD, "<set-?>");
        hud = hUD;
    }

    @NotNull
    public final ClickGui getClickGui() {
        ClickGui clickGui = Client.clickGui;
        if (clickGui != null) {
            return clickGui;
        }
        Intrinsics.throwUninitializedPropertyAccessException("clickGui");
        return null;
    }

    public final void setClickGui(@NotNull ClickGui clickGui) {
        Intrinsics.checkNotNullParameter((Object)clickGui, "<set-?>");
        Client.clickGui = clickGui;
    }

    @Nullable
    public final ResourceLocation getBackground() {
        return background;
    }

    public final void setBackground(@Nullable ResourceLocation resourceLocation) {
        background = resourceLocation;
    }

    @NotNull
    public final ClientPresence getClientPresence() {
        ClientPresence clientPresence = Client.clientPresence;
        if (clientPresence != null) {
            return clientPresence;
        }
        Intrinsics.throwUninitializedPropertyAccessException("clientPresence");
        return null;
    }

    public final void setClientPresence(@NotNull ClientPresence clientPresence) {
        Intrinsics.checkNotNullParameter(clientPresence, "<set-?>");
        Client.clientPresence = clientPresence;
    }

    public final void startClient() {
        FileConfig[] fileConfigArray = JOptionPane.showInputDialog(null, "\u8f93\u5165\u8d26\u53f7", "Info", -1);
        Intrinsics.checkNotNullExpressionValue(fileConfigArray, "showInputDialog(null,\"\u8f93\u5165\u2026OptionPane.PLAIN_MESSAGE)");
        ur = fileConfigArray;
        fileConfigArray = JOptionPane.showInputDialog(null, "\u8f93\u5165\u5bc6\u7801", "Info", -1);
        Intrinsics.checkNotNullExpressionValue(fileConfigArray, "showInputDialog(null,\"\u8f93\u5165\u2026OptionPane.PLAIN_MESSAGE)");
        pw = fileConfigArray;
        Locale locale = Locale.getDefault();
        Intrinsics.checkNotNullExpressionValue(locale, "getDefault()");
        String string = libdir.toLowerCase(locale);
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase(locale)");
        CharSequence charSequence = string;
        StringBuilder stringBuilder = new StringBuilder();
        locale = Locale.getDefault();
        Intrinsics.checkNotNullExpressionValue(locale, "getDefault()");
        string = ur.toLowerCase(locale);
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase(locale)");
        if (StringsKt.contains$default(charSequence, stringBuilder.append(string).append(':').append(pw).append(':').append(users).toString(), false, 2, null)) {
            JOptionPane.showMessageDialog(null, "\u767b\u5f55\u6210\u529f!");
        } else if (StringsKt.contains$default((CharSequence)libdir, ur + ':' + pw + ':' + users, false, 2, null)) {
            JOptionPane.showMessageDialog(null, "\u767b\u5f55\u6210\u529f!");
        } else {
            locale = Locale.getDefault();
            Intrinsics.checkNotNullExpressionValue(locale, "getDefault()");
            string = libdir.toUpperCase(locale);
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toUpperCase(locale)");
            CharSequence charSequence2 = string;
            StringBuilder stringBuilder2 = new StringBuilder();
            locale = Locale.getDefault();
            Intrinsics.checkNotNullExpressionValue(locale, "getDefault()");
            string = ur.toUpperCase(locale);
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toUpperCase(locale)");
            if (StringsKt.contains$default(charSequence2, stringBuilder2.append(string).append(':').append(pw).append(':').append(users).toString(), false, 2, null)) {
                JOptionPane.showMessageDialog(null, "\u767b\u5f55\u6210\u529f!");
            } else {
                JOptionPane.showMessageDialog(null, "\u767b\u5f55\u5931\u8d25,\u7ed1\u5b9a\u8bf7\u52a0\u7fa4:964715445", "failed", 0);
                CheckUtils.setSysClipboardText(users);
                throw new RuntimeException("HCheckFail");
            }
        }
        if (!StringsKt.contains$default((CharSequence)libdir, users, false, 2, null)) {
            throw new NullPointerException("\u8d85\u5e02\u6211");
        }
        isStarting = true;
        lastTick = System.currentTimeMillis();
        this.setFileManager(new FileManager());
        this.setEventManager(new EventManager());
        this.getEventManager().registerListener(new RotationUtils());
        this.getEventManager().registerListener(new AntiForge());
        this.getEventManager().registerListener(new BungeeCordSpoof());
        this.getEventManager().registerListener(new InventoryUtils());
        this.getEventManager().registerListener(InventoryHelper.INSTANCE);
        this.getEventManager().registerListener(new PacketUtils());
        this.getEventManager().registerListener(new SessionUtils());
        this.setClientPresence(new ClientPresence());
        this.setCommandManager(new CommandManager());
        Fonts.loadFonts();
        this.setTipSoundManager(new TipSoundManager());
        this.setModuleManager(new Manager());
        this.getModuleManager().registerModules();
        try {
            Remapper.INSTANCE.loadSrg();
            this.setScriptManager(new ScriptManager());
            this.getScriptManager().loadScripts();
            this.getScriptManager().enableScripts();
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("Failed to load scripts.", throwable);
        }
        this.getCommandManager().registerCommands();
        fileConfigArray = new FileConfig[]{this.getFileManager().modulesConfig, this.getFileManager().valuesConfig, this.getFileManager().accountsConfig, this.getFileManager().friendsConfig, this.getFileManager().xrayConfig};
        this.getFileManager().loadConfigs(fileConfigArray);
        this.setClickGui(new ClickGui());
        this.getFileManager().loadConfig(this.getFileManager().clickGuiConfig);
        this.setHud(HUD.Companion.createDefault());
        this.getFileManager().loadConfig(this.getFileManager().hudConfig);
        GuiAltManager.loadGenerators();
        if (this.getClientPresence().getShowRichPresenceValue()) {
            ThreadsKt.thread$default(false, false, null, null, 0, startClient.1.INSTANCE, 31, null);
        }
        ClientUtils.getLogger().info("Finished loading LiquidBounce in " + (System.currentTimeMillis() - lastTick) + "ms.");
        isStarting = false;
    }

    public final void stopClient() {
        this.getEventManager().callEvent(new ClientShutdownEvent());
        this.getFileManager().saveAllConfigs();
        this.getClientPresence().shutdown();
    }

    static {
        String string = CheckUtils.getHWID();
        Intrinsics.checkNotNullExpressionValue(string, "getHWID()");
        users = string;
        libdir = HttpUtils.get("https://esuteams.coding.net/p/hwid/d/hw1d/git/raw/master/hw1d1?download=false");
        pw = "";
        ur = "";
    }
}

