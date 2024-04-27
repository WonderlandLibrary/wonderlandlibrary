/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.command.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.modules.command.Command;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.SettingsUtils;
import net.dev.important.utils.misc.StringUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\bJ\u0015\u0010\t\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\u0006H\u0002\u00a2\u0006\u0002\u0010\u000bJ!\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00070\r2\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\u000e\u00a8\u0006\u000f"}, d2={"Lnet/dev/important/modules/command/commands/LocalAutoSettingsCommand;", "Lnet/dev/important/modules/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "getLocalSettings", "Ljava/io/File;", "()[Ljava/io/File;", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "LiquidBounce"})
public final class LocalAutoSettingsCommand
extends Command {
    public LocalAutoSettingsCommand() {
        String[] stringArray = new String[]{"localsetting", "localsettings", "localconfig"};
        super("localautosettings", stringArray);
    }

    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkNotNullParameter(args2, "args");
        if (args2.length > 1) {
            if (StringsKt.equals(args2[1], "load", true)) {
                if (args2.length > 2) {
                    File scriptFile = new File(Client.INSTANCE.getFileManager().settingsDir, args2[2]);
                    if (scriptFile.exists()) {
                        try {
                            this.chat("\u00a79Loading settings...");
                            String settings2 = FilesKt.readText$default(scriptFile, null, 1, null);
                            this.chat("\u00a79Set settings...");
                            SettingsUtils.INSTANCE.executeScript(settings2);
                            this.chat("\u00a76Settings applied successfully.");
                            Client.INSTANCE.getHud().addNotification(new Notification("Updated Settings", Notification.Type.SUCCESS));
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
                    File scriptFile = new File(Client.INSTANCE.getFileManager().settingsDir, args2[2]);
                    try {
                        boolean states;
                        String string;
                        if (scriptFile.exists()) {
                            scriptFile.delete();
                        }
                        scriptFile.createNewFile();
                        if (args2.length > 3) {
                            String string2 = StringUtils.toCompleteString(args2, 3);
                            Intrinsics.checkNotNullExpressionValue(string2, "toCompleteString(args, 3)");
                            String string3 = string2.toLowerCase();
                            Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String).toLowerCase()");
                            string = string3;
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
                        this.chat(Intrinsics.stringPlus("\u00a7cFailed to create local config: \u00a73", throwable.getMessage()));
                        ClientUtils.getLogger().error("Failed to create local config.", throwable);
                    }
                    return;
                }
                this.chatSyntax("localsettings save <name> [all/values/binds/states]...");
                return;
            }
            if (StringsKt.equals(args2[1], "delete", true)) {
                if (args2.length > 2) {
                    File scriptFile = new File(Client.INSTANCE.getFileManager().settingsDir, args2[2]);
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
                this.chat("\u00a7cSettings:");
                File[] fileArray = this.getLocalSettings();
                if (fileArray == null) {
                    return;
                }
                for (File file : fileArray) {
                    this.chat(Intrinsics.stringPlus("> ", file.getName()));
                }
                return;
            }
        }
        this.chatSyntax("localsettings <load/save/list/delete>");
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public List<String> tabComplete(@NotNull String[] args2) {
        List list;
        Intrinsics.checkNotNullParameter(args2, "args");
        if (args2.length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args2.length) {
            case 1: {
                void $this$filterTo$iv$iv;
                String[] stringArray = new String[]{"delete", "list", "load", "save"};
                Iterable $this$filter$iv = CollectionsKt.listOf(stringArray);
                boolean $i$f$filter = false;
                Iterable iterable = $this$filter$iv;
                Collection destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    String it = (String)element$iv$iv;
                    boolean bl = false;
                    if (!StringsKt.startsWith(it, args2[0], true)) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                list = (List)destination$iv$iv;
                break;
            }
            case 2: {
                File[] $this$filterTo$iv$iv = args2[0].toLowerCase();
                Intrinsics.checkNotNullExpressionValue($this$filterTo$iv$iv, "this as java.lang.String).toLowerCase()");
                File[] $this$filter$iv = $this$filterTo$iv$iv;
                if (Intrinsics.areEqual($this$filter$iv, "delete") ? true : Intrinsics.areEqual($this$filter$iv, "load")) {
                    void $this$filterTo$iv$iv2;
                    void $this$filter$iv2;
                    void $this$mapTo$iv$iv;
                    Iterable $this$map$iv;
                    File[] settings2;
                    File[] fileArray = this.getLocalSettings();
                    if (fileArray == null) {
                        return CollectionsKt.emptyList();
                    }
                    $this$filterTo$iv$iv = settings2 = fileArray;
                    boolean $i$f$map = false;
                    void $i$f$filterTo = $this$map$iv;
                    Collection destination$iv$iv = new ArrayList(((void)$this$map$iv).length);
                    boolean $i$f$mapTo = false;
                    for (void item$iv$iv : $this$mapTo$iv$iv) {
                        void it;
                        void var13_34 = item$iv$iv;
                        Collection collection = destination$iv$iv;
                        boolean bl = false;
                        collection.add(it.getName());
                    }
                    $this$map$iv = (List)destination$iv$iv;
                    boolean $i$f$filter = false;
                    $this$mapTo$iv$iv = $this$filter$iv2;
                    destination$iv$iv = new ArrayList();
                    boolean $i$f$filterTo2 = false;
                    for (Object element$iv$iv : $this$filterTo$iv$iv2) {
                        String it = (String)element$iv$iv;
                        boolean bl = false;
                        Intrinsics.checkNotNullExpressionValue(it, "it");
                        if (!StringsKt.startsWith(it, args2[1], true)) continue;
                        destination$iv$iv.add(element$iv$iv);
                    }
                    return (List)destination$iv$iv;
                }
                return CollectionsKt.emptyList();
            }
            case 3: {
                if (StringsKt.equals(args2[0], "save", true)) {
                    void $this$filterTo$iv$iv;
                    Object $this$filter$iv = new String[]{"all", "states", "binds", "values"};
                    $this$filter$iv = CollectionsKt.listOf($this$filter$iv);
                    boolean $i$f$filter = false;
                    Object $this$filter$iv2 = $this$filter$iv;
                    Collection destination$iv$iv = new ArrayList();
                    boolean $i$f$filterTo = false;
                    for (Object element$iv$iv : $this$filterTo$iv$iv) {
                        String it = (String)element$iv$iv;
                        boolean bl = false;
                        if (!StringsKt.startsWith(it, args2[2], true)) continue;
                        destination$iv$iv.add(element$iv$iv);
                    }
                    return (List)destination$iv$iv;
                }
                return CollectionsKt.emptyList();
            }
            default: {
                list = CollectionsKt.emptyList();
            }
        }
        return list;
    }

    private final File[] getLocalSettings() {
        return Client.INSTANCE.getFileManager().settingsDir.listFiles();
    }
}

