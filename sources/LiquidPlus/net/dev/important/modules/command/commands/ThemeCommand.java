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
import net.dev.important.gui.client.hud.Config;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.modules.command.Command;
import net.dev.important.utils.ClientUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\bJ\u0015\u0010\t\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\u0006H\u0002\u00a2\u0006\u0002\u0010\u000bJ!\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00070\r2\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\u000e\u00a8\u0006\u000f"}, d2={"Lnet/dev/important/modules/command/commands/ThemeCommand;", "Lnet/dev/important/modules/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "getLocalThemes", "Ljava/io/File;", "()[Ljava/io/File;", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "LiquidBounce"})
public final class ThemeCommand
extends Command {
    public ThemeCommand() {
        String[] stringArray = new String[]{"thememanager", "tm", "themes"};
        super("theme", stringArray);
    }

    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkNotNullParameter(args2, "args");
        if (args2.length > 1) {
            if (StringsKt.equals(args2[1], "load", true)) {
                if (args2.length > 2) {
                    File themeFile = new File(Client.INSTANCE.getFileManager().themesDir, args2[2]);
                    if (themeFile.exists()) {
                        try {
                            this.chat("\u00a79Loading theme...");
                            String theme = FilesKt.readText$default(themeFile, null, 1, null);
                            this.chat("\u00a79Set theme settings...");
                            Client.INSTANCE.setStarting(true);
                            Client.INSTANCE.getHud().clearElements();
                            Client.INSTANCE.setHud(new Config(theme).toHUD());
                            Client.INSTANCE.setStarting(false);
                            this.chat("\u00a76Theme applied successfully.");
                            Client.INSTANCE.getHud().addNotification(new Notification("Updated HUD Theme.", Notification.Type.SUCCESS));
                            this.playEdit();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    this.chat("\u00a7cTheme file does not exist!");
                    return;
                }
                this.chatSyntax("theme load <name>");
                return;
            }
            if (StringsKt.equals(args2[1], "save", true)) {
                if (args2.length > 2) {
                    File themeFile = new File(Client.INSTANCE.getFileManager().themesDir, args2[2]);
                    try {
                        if (themeFile.exists()) {
                            themeFile.delete();
                        }
                        themeFile.createNewFile();
                        this.chat("\u00a79Creating theme settings...");
                        String settingsTheme = new Config(Client.INSTANCE.getHud()).toJson();
                        this.chat("\u00a79Saving theme...");
                        FilesKt.writeText$default(themeFile, settingsTheme, null, 2, null);
                        this.chat("\u00a76Theme saved successfully.");
                    }
                    catch (Throwable throwable) {
                        this.chat(Intrinsics.stringPlus("\u00a7cFailed to create local theme config: \u00a73", throwable.getMessage()));
                        ClientUtils.getLogger().error("Failed to create local theme config.", throwable);
                    }
                    return;
                }
                this.chatSyntax("theme save <name>");
                return;
            }
            if (StringsKt.equals(args2[1], "delete", true)) {
                if (args2.length > 2) {
                    File themeFile = new File(Client.INSTANCE.getFileManager().themesDir, args2[2]);
                    if (themeFile.exists()) {
                        themeFile.delete();
                        this.chat("\u00a76Theme file deleted successfully.");
                        return;
                    }
                    this.chat("\u00a7cTheme file does not exist!");
                    return;
                }
                this.chatSyntax("theme delete <name>");
                return;
            }
            if (StringsKt.equals(args2[1], "list", true)) {
                this.chat("\u00a7cThemes:");
                File[] fileArray = this.getLocalThemes();
                if (fileArray == null) {
                    return;
                }
                for (File file : fileArray) {
                    this.chat(Intrinsics.stringPlus("> ", file.getName()));
                }
                return;
            }
        }
        this.chatSyntax("theme <load/save/list/delete>");
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
                File[] fileArray = $this$filterTo$iv$iv;
                if (Intrinsics.areEqual(fileArray, "delete") ? true : Intrinsics.areEqual(fileArray, "load")) {
                    void $this$filterTo$iv$iv2;
                    void $this$filter$iv;
                    void $this$mapTo$iv$iv;
                    Iterable $this$map$iv;
                    File[] settings2;
                    File[] fileArray2 = this.getLocalThemes();
                    if (fileArray2 == null) {
                        return CollectionsKt.emptyList();
                    }
                    $this$filterTo$iv$iv = settings2 = fileArray2;
                    boolean $i$f$map = false;
                    void $i$f$filterTo = $this$map$iv;
                    Collection destination$iv$iv = new ArrayList(((void)$this$map$iv).length);
                    boolean $i$f$mapTo = false;
                    for (void item$iv$iv : $this$mapTo$iv$iv) {
                        void it;
                        void var13_25 = item$iv$iv;
                        Collection collection = destination$iv$iv;
                        boolean bl = false;
                        collection.add(it.getName());
                    }
                    $this$map$iv = (List)destination$iv$iv;
                    boolean $i$f$filter = false;
                    $this$mapTo$iv$iv = $this$filter$iv;
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
            default: {
                list = CollectionsKt.emptyList();
            }
        }
        return list;
    }

    private final File[] getLocalThemes() {
        return Client.INSTANCE.getFileManager().themesDir.listFiles();
    }
}

