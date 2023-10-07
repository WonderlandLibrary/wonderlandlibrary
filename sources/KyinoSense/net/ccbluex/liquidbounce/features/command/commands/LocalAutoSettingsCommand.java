/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
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
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.SettingsUtils;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\bJ\u0015\u0010\t\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\u0006H\u0002\u00a2\u0006\u0002\u0010\u000bJ!\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00070\r2\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\u000e\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/LocalAutoSettingsCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "getLocalSettings", "Ljava/io/File;", "()[Ljava/io/File;", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "KyinoClient"})
public final class LocalAutoSettingsCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkParameterIsNotNull(args2, "args");
        if (args2.length > 1) {
            if (StringsKt.equals(args2[1], "load", true)) {
                if (args2.length > 2) {
                    File scriptFile = new File(LiquidBounce.INSTANCE.getFileManager().settingsDir, args2[2]);
                    if (scriptFile.exists()) {
                        try {
                            this.chat("\u00a79Loading settings...");
                            String settings2 = FilesKt.readText$default(scriptFile, null, 1, null);
                            this.chat("\u00a79Set settings...");
                            SettingsUtils.INSTANCE.executeScript(settings2);
                            this.chat("\u00a76Settings applied successfully.");
                            LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Updated Settings"));
                            this.playEdit();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    this.chat("\u00a7cSettings file does not exist!");
                    return;
                }
                this.chatSyntax("localautosettings load <name>");
                return;
            }
            if (StringsKt.equals(args2[1], "save", true)) {
                if (args2.length > 2) {
                    File scriptFile = new File(LiquidBounce.INSTANCE.getFileManager().settingsDir, args2[2]);
                    try {
                        boolean states;
                        String string;
                        if (scriptFile.exists()) {
                            scriptFile.delete();
                        }
                        scriptFile.createNewFile();
                        if (args2.length > 3) {
                            String string2 = StringUtils.toCompleteString(args2, 3);
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
                        boolean values2 = StringsKt.contains$default((CharSequence)option, "all", false, 2, null) || StringsKt.contains$default((CharSequence)option, "values", false, 2, null);
                        boolean binds = StringsKt.contains$default((CharSequence)option, "all", false, 2, null) || StringsKt.contains$default((CharSequence)option, "binds", false, 2, null);
                        boolean bl = states = StringsKt.contains$default((CharSequence)option, "all", false, 2, null) || StringsKt.contains$default((CharSequence)option, "states", false, 2, null);
                        if (!(values2 || binds || states)) {
                            this.chatSyntaxError();
                            return;
                        }
                        this.chat("\u00a79Creating settings...");
                        String settingsScript = SettingsUtils.INSTANCE.generateScript(values2, binds, states);
                        this.chat("\u00a79Saving settings...");
                        FilesKt.writeText$default(scriptFile, settingsScript, null, 2, null);
                        this.chat("\u00a76Settings saved successfully.");
                    }
                    catch (Throwable throwable) {
                        this.chat("\u00a7cFailed to create local config: \u00a73" + throwable.getMessage());
                        ClientUtils.getLogger().error("Failed to create local config.", throwable);
                    }
                    return;
                }
                this.chatSyntax("localsettings save <name> [all/values/binds/states]...");
                return;
            }
            if (StringsKt.equals(args2[1], "delete", true)) {
                if (args2.length > 2) {
                    File scriptFile = new File(LiquidBounce.INSTANCE.getFileManager().settingsDir, args2[2]);
                    if (scriptFile.exists()) {
                        scriptFile.delete();
                        this.chat("\u00a76Settings file deleted successfully.");
                        return;
                    }
                    this.chat("\u00a7cSettings file does not exist!");
                    return;
                }
                this.chatSyntax("localsettings delete <name>");
                return;
            }
            if (StringsKt.equals(args2[1], "list", true)) {
                File[] settings3;
                this.chat("\u00a7cSettings:");
                File[] fileArray = this.getLocalSettings();
                if (fileArray == null) {
                    return;
                }
                for (File file : settings3 = fileArray) {
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

