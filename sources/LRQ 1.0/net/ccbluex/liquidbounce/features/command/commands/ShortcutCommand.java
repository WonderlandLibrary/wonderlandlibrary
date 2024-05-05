/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;

public final class ShortcutCommand
extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length > 3 && StringsKt.equals((String)args[1], (String)"add", (boolean)true)) {
            try {
                LiquidBounce.INSTANCE.getCommandManager().registerShortcut(args[2], StringUtils.toCompleteString(args, 3));
                this.chat("Successfully added shortcut.");
            }
            catch (IllegalArgumentException e) {
                String string = e.getMessage();
                if (string == null) {
                    Intrinsics.throwNpe();
                }
                this.chat(string);
            }
        } else if (args.length >= 3 && StringsKt.equals((String)args[1], (String)"remove", (boolean)true)) {
            if (LiquidBounce.INSTANCE.getCommandManager().unregisterShortcut(args[2])) {
                this.chat("Successfully removed shortcut.");
            } else {
                this.chat("Shortcut does not exist.");
            }
        } else {
            this.chat("shortcut <add <shortcut_name> <script>/remove <shortcut_name>>");
        }
    }

    public ShortcutCommand() {
        super("shortcut", new String[0]);
    }
}

