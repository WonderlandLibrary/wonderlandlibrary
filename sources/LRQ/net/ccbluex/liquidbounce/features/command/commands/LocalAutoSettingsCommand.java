/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.io.FilesKt
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilesKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.SettingsUtils;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;

public final class LocalAutoSettingsCommand
extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length > 1) {
            if (StringsKt.equals((String)args[1], (String)"load", (boolean)true)) {
                if (args.length > 2) {
                    File scriptFile = new File(LiquidBounce.INSTANCE.getFileManager().settingsDir, args[2]);
                    if (scriptFile.exists()) {
                        try {
                            this.chat("\u00a79Loading settings...");
                            String settings2 = FilesKt.readText$default((File)scriptFile, null, (int)1, null);
                            this.chat("\u00a79Set settings...");
                            SettingsUtils.INSTANCE.executeScript(settings2);
                            this.chat("\u00a76Settings applied successfully.");
                            LiquidBounce.INSTANCE.getHud().addNotification(new Notification("LocalAutoSetting", "Updated Settings", NotifyType.INFO, 0, 0, 24, null));
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
            if (StringsKt.equals((String)args[1], (String)"save", (boolean)true)) {
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
                            boolean bl = false;
                            String string3 = string2;
                            if (string3 == null) {
                                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                            }
                            string = string3.toLowerCase();
                        } else {
                            string = "values";
                        }
                        String option = string;
                        boolean values = option.equals("all") || option.equals("values");
                        boolean binds = option.equals("all") || option.equals("binds");
                        boolean bl = states = option.equals("all") || option.equals("states");
                        if (!(values || binds || states)) {
                            this.chatSyntaxError();
                            return;
                        }
                        this.chat("\u00a79Creating settings...");
                        String settingsScript = SettingsUtils.INSTANCE.generateScript(values, binds, states);
                        this.chat("\u00a79Saving settings...");
                        FilesKt.writeText$default((File)scriptFile, (String)settingsScript, null, (int)2, null);
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
            if (StringsKt.equals((String)args[1], (String)"delete", (boolean)true)) {
                if (args.length > 2) {
                    File scriptFile = new File(LiquidBounce.INSTANCE.getFileManager().settingsDir, args[2]);
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
            if (StringsKt.equals((String)args[1], (String)"list", (boolean)true)) {
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
    public List<String> tabComplete(String[] args) {
        var2_2 = args;
        var3_3 = false;
        if (((String[])var2_2).length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args.length) {
            case 1: {
                $this$filter$iv = CollectionsKt.listOf((Object[])new String[]{"delete", "list", "load", "save"});
                $i$f$filter = false;
                var4_5 = $this$filter$iv;
                destination$iv$iv = new ArrayList<E>();
                $i$f$filterTo = false;
                for (T element$iv$iv : $this$filterTo$iv$iv) {
                    it = (String)element$iv$iv;
                    $i$a$-filter-LocalAutoSettingsCommand$tabComplete$1 = false;
                    if (!StringsKt.startsWith((String)it, (String)args[0], (boolean)true)) continue;
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
                var2_2 = v1.toLowerCase();
                switch (var2_2.hashCode()) {
                    case 3327206: {
                        if (!var2_2.equals("load")) break;
                        ** GOTO lbl34
                    }
                    case -1335458389: {
                        if (!var2_2.equals("delete")) break;
lbl34:
                        // 2 sources

                        v2 = this.getLocalSettings();
                        if (v2 == null) {
                            return CollectionsKt.emptyList();
                        }
                        $this$map$iv = settings = v2;
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
                            if (!StringsKt.startsWith((String)it, (String)args[1], (boolean)true)) continue;
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

