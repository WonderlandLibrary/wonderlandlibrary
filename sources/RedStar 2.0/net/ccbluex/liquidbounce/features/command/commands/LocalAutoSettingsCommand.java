package net.ccbluex.liquidbounce.features.command.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.SettingsUtils;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\n\n\b\n\n\u0000\n\n\n\b\n\n\b\n \n\b\u000020B¢J02\f\b00H¢\bJ\t\n0\n0H¢J!\f\b00\r2\f\b00H¢¨"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/LocalAutoSettingsCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "getLocalSettings", "Ljava/io/File;", "()[Ljava/io/File;", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "Pride"})
public final class LocalAutoSettingsCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        if (args.length > 1) {
            if (StringsKt.equals(args[1], "load", true)) {
                if (args.length > 2) {
                    File scriptFile = new File(LiquidBounce.INSTANCE.getFileManager().settingsDir, args[2]);
                    if (scriptFile.exists()) {
                        try {
                            this.chat("§9Loading settings...");
                            String settings = FilesKt.readText$default(scriptFile, null, 1, null);
                            this.chat("§9Set settings...");
                            SettingsUtils.INSTANCE.executeScript(settings);
                            this.chat("§6Settings applied successfully.");
                            LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Notification", "Updated Settings", NotifyType.INFO, 0, 0, 24, null));
                            this.playEdit();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    this.chat("§cSettings file does not exist!");
                    return;
                }
                this.chatSyntax("localautosettings load <name>");
                return;
            }
            if (StringsKt.equals(args[1], "save", true)) {
                if (args.length > 2) {
                    File scriptFile = new File(LiquidBounce.INSTANCE.getFileManager().settingsDir, args[2]);
                    try {
                        boolean states;
                        String string;
                        if (scriptFile.exists()) {
                            scriptFile.delete();
                        }
                        scriptFile.createNewFile();
                        if (args.length > 3) {
                            String string2 = StringUtils.toCompleteString(args, 3);
                            Intrinsics.checkExpressionValueIsNotNull(string2, "StringUtils.toCompleteString(args, 3)");
                            String string3 = string2;
                            boolean bl = false;
                            String string4 = string3;
                            if (string4 == null) {
                                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                            }
                            String string5 = string4.toLowerCase();
                            string = string5;
                            Intrinsics.checkExpressionValueIsNotNull(string5, "(this as java.lang.String).toLowerCase()");
                        } else {
                            string = "values";
                        }
                        String option = string;
                        boolean values = StringsKt.contains$default((CharSequence)option, "all", false, 2, null) || StringsKt.contains$default((CharSequence)option, "values", false, 2, null);
                        boolean binds = StringsKt.contains$default((CharSequence)option, "all", false, 2, null) || StringsKt.contains$default((CharSequence)option, "binds", false, 2, null);
                        boolean bl = states = StringsKt.contains$default((CharSequence)option, "all", false, 2, null) || StringsKt.contains$default((CharSequence)option, "states", false, 2, null);
                        if (!(values || binds || states)) {
                            this.chatSyntaxError();
                            return;
                        }
                        this.chat("§9Creating settings...");
                        String settingsScript = SettingsUtils.INSTANCE.generateScript(values, binds, states);
                        this.chat("§9Saving settings...");
                        FilesKt.writeText$default(scriptFile, settingsScript, null, 2, null);
                        this.chat("§6Settings saved successfully.");
                    }
                    catch (Throwable throwable) {
                        this.chat("§cFailed to create local config: §3" + throwable.getMessage());
                        ClientUtils.getLogger().error("Failed to create local config.", throwable);
                    }
                    return;
                }
                this.chatSyntax("localsettings save <name> [all/values/binds/states]...");
                return;
            }
            if (StringsKt.equals(args[1], "delete", true)) {
                if (args.length > 2) {
                    File scriptFile = new File(LiquidBounce.INSTANCE.getFileManager().settingsDir, args[2]);
                    if (scriptFile.exists()) {
                        scriptFile.delete();
                        this.chat("§6Settings file deleted successfully.");
                        return;
                    }
                    this.chat("§cSettings file does not exist!");
                    return;
                }
                this.chatSyntax("localsettings delete <name>");
                return;
            }
            if (StringsKt.equals(args[1], "list", true)) {
                File[] settings;
                this.chat("§cSettings:");
                File[] fileArray = this.getLocalSettings();
                if (fileArray == null) {
                    return;
                }
                for (File file : settings = fileArray) {
                    this.chat("> " + file.getName());
                }
                return;
            }
        }
        this.chatSyntax("localsettings <load/save/list/delete>");
    }

    /*
     * Unable to fully structure code
     */
    @Override
    @NotNull
    public List<String> tabComplete(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        var2_2 = args;
        var3_3 = false;
        if (((String[])var2_2).length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args.length) {
            case 1: {
                $this$filter$iv = CollectionsKt.listOf(new String[]{"delete", "list", "load", "save"});
                $i$f$filter = false;
                var4_5 = $this$filter$iv;
                destination$iv$iv = new ArrayList<E>();
                $i$f$filterTo = false;
                for (T element$iv$iv : $this$filterTo$iv$iv) {
                    it = (String)element$iv$iv;
                    $i$a$-filter-LocalAutoSettingsCommand$tabComplete$1 = false;
                    if (!StringsKt.startsWith(it, args[0], true)) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                v0 = (List)destination$iv$iv;
                break;
            }
            case 2: {
                var2_2 = args[0];
                $i$f$filter = false;
                v1 = var2_2;
                if (v1 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                v2 = v1.toLowerCase();
                Intrinsics.checkExpressionValueIsNotNull(v2, "(this as java.lang.String).toLowerCase()");
                var2_2 = v2;
                switch (var2_2.hashCode()) {
                    case 3327206: {
                        if (!var2_2.equals("load")) break;
                        ** GOTO lbl37
                    }
                    case -1335458389: {
                        if (!var2_2.equals("delete")) break;
lbl37:
                        // 2 sources

                        v3 = this.getLocalSettings();
                        if (v3 == null) {
                            return CollectionsKt.emptyList();
                        }
                        $this$map$iv = settings = v3;
                        $i$f$map = false;
                        $i$f$filterTo = $this$map$iv;
                        destination$iv$iv = new ArrayList<E>($this$map$iv.length);
                        $i$f$mapTo = false;
                        var9_16 = $this$mapTo$iv$iv;
                        $i$a$-filter-LocalAutoSettingsCommand$tabComplete$1 = ((void)var9_16).length;
                        for (var11_20 = 0; var11_20 < $i$a$-filter-LocalAutoSettingsCommand$tabComplete$1; ++var11_20) {
                            var13_24 = item$iv$iv = var9_16[var11_20];
                            var15_26 = destination$iv$iv;
                            $i$a$-map-LocalAutoSettingsCommand$tabComplete$2 = false;
                            var16_27 = it.getName();
                            var15_26.add(var16_27);
                        }
                        $this$filter$iv = (List)destination$iv$iv;
                        $i$f$filter = false;
                        $this$mapTo$iv$iv = $this$filter$iv;
                        destination$iv$iv = new ArrayList<E>();
                        $i$f$filterTo = false;
                        for (T element$iv$iv : $this$filterTo$iv$iv) {
                            it = (String)element$iv$iv;
                            $i$a$-filter-LocalAutoSettingsCommand$tabComplete$3 = false;
                            v4 = it;
                            Intrinsics.checkExpressionValueIsNotNull(v4, "it");
                            if (!StringsKt.startsWith(v4, args[1], true)) continue;
                            destination$iv$iv.add(element$iv$iv);
                        }
                        return (List)destination$iv$iv;
                    }
                }
                return CollectionsKt.emptyList();
            }
            default: {
                v0 = CollectionsKt.emptyList();
            }
        }
        return v0;
    }

    private final File[] getLocalSettings() {
        return LiquidBounce.INSTANCE.getFileManager().settingsDir.listFiles();
    }

    public LocalAutoSettingsCommand() {
        super("localautosettings", "localsetting", "localsettings", "localconfig");
    }
}
