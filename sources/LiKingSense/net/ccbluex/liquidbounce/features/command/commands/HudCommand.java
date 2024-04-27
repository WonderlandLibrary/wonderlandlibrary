/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.collections.ArraysKt
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.Intrinsics
 *  org.apache.commons.io.FileUtils
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.ui.client.hud.Config;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\bJ!\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n2\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/HudCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "LiKingSense"})
public final class HudCommand
extends Command {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull((Object)args, (String)"args");
        if (args.length < 2) {
            this.chatSyntax("hudconfig < list / save <name> / load <name> / delete <name> >");
            return;
        }
        String command = args[1];
        File dir = LiquidBounce.INSTANCE.getFileManager().hudsDir;
        String string = command;
        int n = -1;
        switch (string.hashCode()) {
            case 3327206: {
                if (!string.equals("load")) return;
                n = 1;
                break;
            }
            case 3522941: {
                if (!string.equals("save")) return;
                n = 2;
                break;
            }
            case 3322014: {
                if (!string.equals("list")) return;
                n = 3;
                break;
            }
            case -1335458389: {
                if (!string.equals("delete")) return;
                n = 4;
            }
        }
        switch (n) {
            case 3: {
                this.chat("Huds :");
                File[] fileArray = dir.listFiles();
                int n2 = fileArray.length;
                int n3 = 0;
                while (n3 < n2) {
                    File listFile;
                    File file = listFile = fileArray[n3];
                    Intrinsics.checkExpressionValueIsNotNull((Object)file, (String)"listFile");
                    String string2 = file.getName();
                    Intrinsics.checkExpressionValueIsNotNull((Object)string2, (String)"listFile.name");
                    this.chat(string2);
                    ++n3;
                }
                return;
            }
            case 4: {
                if (args.length != 3) {
                    this.chatSyntax("hudconfig < list / save <name> / load <name> / delete <name> >");
                    return;
                }
                if (!new File(LiquidBounce.INSTANCE.getFileManager().configsDir, args[2]).exists()) {
                    this.chat("Hud " + args[2] + " not found.");
                    return;
                }
                try {
                    FileUtils.forceDelete((File)new File(LiquidBounce.INSTANCE.getFileManager().hudsDir, args[2]));
                    this.chat("Deleted hud: " + args[2]);
                    return;
                }
                catch (Exception e) {
                    this.chat("Failed to delete hud: " + args[2]);
                    return;
                }
            }
            case 1: {
                if (args.length != 3) {
                    this.chatSyntax("hudconfig < list / save <name> / load <name> / delete <name> >");
                    return;
                }
                if (new File(LiquidBounce.INSTANCE.getFileManager().configsDir, args[2]).exists()) {
                    LiquidBounce.INSTANCE.getHud().clearElements();
                    String string3 = FileUtils.readFileToString((File)new File(LiquidBounce.INSTANCE.getFileManager().hudsDir, args[2]));
                    Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"FileUtils.readFileToStri\u2026anager.hudsDir, args[2]))");
                    LiquidBounce.INSTANCE.setHud(new Config(string3).toHUD());
                    this.chat("Loaded hud: " + args[2]);
                    return;
                }
                this.chat("Hud " + args[2] + " not found.");
                return;
            }
            case 2: {
                if (args.length == 3) {
                    PrintWriter printWriter = new PrintWriter(new FileWriter(new File(LiquidBounce.INSTANCE.getFileManager().hudsDir, args[2])));
                    printWriter.println(new Config(LiquidBounce.INSTANCE.getHud()).toJson());
                    printWriter.close();
                    this.chat("Saved hud " + args[2]);
                    return;
                }
                this.chatSyntax("hudconfig < list / save <name> / load <name> / delete <name> >");
                return;
            }
        }
    }

    @Override
    @NotNull
    public List<String> tabComplete(@NotNull String[] args) {
        List list;
        Intrinsics.checkParameterIsNotNull((Object)args, (String)"args");
        String[] stringArray = args;
        boolean bl = false;
        if (stringArray.length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args.length) {
            case 1: {
                list = ArraysKt.toList((Object[])new String[]{"list", "save", "load", "delete"});
                break;
            }
            case 2: {
                ArrayList<String> array = new ArrayList<String>();
                File[] fileArray = LiquidBounce.INSTANCE.getFileManager().hudsDir.listFiles();
                int n = fileArray.length;
                for (int i = 0; i < n; ++i) {
                    File listFile;
                    File file = listFile = fileArray[i];
                    Intrinsics.checkExpressionValueIsNotNull((Object)file, (String)"listFile");
                    array.add(file.getName());
                }
                list = array;
                break;
            }
            default: {
                list = CollectionsKt.emptyList();
            }
        }
        return list;
    }

    public HudCommand() {
        super("hudconfig", new String[0]);
    }
}

