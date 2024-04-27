/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;

public final class PrefixCommand
extends Command {
    @Override
    public void execute(String[] stringArray) {
        if (stringArray.length <= 1) {
            this.chatSyntax("prefix <character>");
            return;
        }
        String string = stringArray[1];
        if (string.length() > 1) {
            this.chat("\u00a7cPrefix can only be one character long!");
            return;
        }
        LiquidBounce.INSTANCE.getCommandManager().setPrefix(StringsKt.single((CharSequence)string));
        LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().valuesConfig);
        this.chat("Successfully changed command prefix to '\u00a78" + string + "\u00a73'");
    }

    public PrefixCommand() {
        super("prefix", new String[0]);
    }
}

