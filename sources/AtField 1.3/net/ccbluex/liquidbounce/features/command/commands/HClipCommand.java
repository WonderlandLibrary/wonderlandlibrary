/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class HClipCommand
extends Command {
    @Override
    public void execute(String[] stringArray) {
        if (stringArray.length > 1) {
            try {
                String string = stringArray[1];
                boolean bl = false;
                MovementUtils.forward(Double.parseDouble(string));
                this.chat("You were teleported.");
            }
            catch (NumberFormatException numberFormatException) {
                this.chatSyntaxError();
            }
            return;
        }
        this.chatSyntax("hclip <value>");
    }

    public HClipCommand() {
        super("hclip", new String[0]);
    }
}

