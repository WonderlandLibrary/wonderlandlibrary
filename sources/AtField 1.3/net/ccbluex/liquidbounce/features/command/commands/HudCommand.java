/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.ArraysKt
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.Intrinsics
 *  org.apache.commons.io.FileUtils
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.ui.client.hud.Config;
import org.apache.commons.io.FileUtils;

public final class HudCommand
extends Command {
    @Override
    public List tabComplete(String[] stringArray) {
        List list;
        Object object = stringArray;
        boolean bl = false;
        if (((String[])object).length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (stringArray.length) {
            case 1: {
                list = ArraysKt.toList((Object[])new String[]{"list", "save", "load", "delete"});
                break;
            }
            case 2: {
                object = new ArrayList();
                File file = LiquidBounce.INSTANCE.getFileManager().hudsDir;
                if (file == null) {
                    Intrinsics.throwNpe();
                }
                for (File file2 : file.listFiles()) {
                    ((ArrayList)object).add(file2.getName());
                }
                list = (List)object;
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

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void execute(String[] stringArray) {
        if (stringArray.length < 2) {
            this.chatSyntax("hudconfig < list / save <name> / load <name> / delete <name> >");
            return;
        }
        String string = stringArray[1];
        File file = LiquidBounce.INSTANCE.getFileManager().hudsDir;
        if (file == null) {
            Intrinsics.throwNpe();
        }
        File file2 = file;
        String string2 = string;
        int n = -1;
        switch (string2.hashCode()) {
            case 3327206: {
                if (!string2.equals("load")) return;
                n = 1;
                break;
            }
            case 3522941: {
                if (!string2.equals("save")) return;
                n = 2;
                break;
            }
            case 3322014: {
                if (!string2.equals("list")) return;
                n = 3;
                break;
            }
            case -1335458389: {
                if (!string2.equals("delete")) return;
                n = 4;
            }
        }
        switch (n) {
            case 3: {
                this.chat("Huds :");
                File[] fileArray = file2.listFiles();
                int n2 = fileArray.length;
                int n3 = 0;
                while (n3 < n2) {
                    File file3 = fileArray[n3];
                    this.chat(file3.getName());
                    ++n3;
                }
                return;
            }
            case 4: {
                if (stringArray.length != 3) {
                    this.chatSyntax("hudconfig < list / save <name> / load <name> / delete <name> >");
                    return;
                }
                if (!new File(LiquidBounce.INSTANCE.getFileManager().configsDir, stringArray[2]).exists()) {
                    this.chat("Hud " + stringArray[2] + " not found.");
                    return;
                }
                try {
                    FileUtils.forceDelete((File)new File(LiquidBounce.INSTANCE.getFileManager().hudsDir, stringArray[2]));
                    this.chat("Deleted hud: " + stringArray[2]);
                    return;
                }
                catch (Exception exception) {
                    this.chat("Failed to delete hud: " + stringArray[2]);
                    return;
                }
            }
            case 1: {
                if (stringArray.length != 3) {
                    this.chatSyntax("hudconfig < list / save <name> / load <name> / delete <name> >");
                    return;
                }
                if (new File(LiquidBounce.INSTANCE.getFileManager().configsDir, stringArray[2]).exists()) {
                    LiquidBounce.INSTANCE.getHud().clearElements();
                    LiquidBounce.INSTANCE.setHud(new Config(FileUtils.readFileToString((File)new File(LiquidBounce.INSTANCE.getFileManager().hudsDir, stringArray[2]))).toHUD());
                    this.chat("Loaded hud: " + stringArray[2]);
                    return;
                }
                this.chat("Hud " + stringArray[2] + " not found.");
                return;
            }
            case 2: {
                if (stringArray.length == 3) {
                    PrintWriter printWriter = new PrintWriter(new FileWriter(new File(LiquidBounce.INSTANCE.getFileManager().hudsDir, stringArray[2])));
                    printWriter.println(new Config(LiquidBounce.INSTANCE.getHud()).toJson());
                    printWriter.close();
                    this.chat("Saved hud " + stringArray[2]);
                    return;
                }
                this.chatSyntax("hudconfig < list / save <name> / load <name> / delete <name> >");
                return;
            }
        }
    }
}

