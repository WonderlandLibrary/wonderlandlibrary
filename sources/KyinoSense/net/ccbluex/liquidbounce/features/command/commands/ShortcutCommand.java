/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.command.CommandManager;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\b\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/ShortcutCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "KyinoClient"})
public final class ShortcutCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkParameterIsNotNull(args2, "args");
        if (args2.length > 3 && StringsKt.equals(args2[1], "add", true)) {
            try {
                CommandManager commandManager = LiquidBounce.INSTANCE.getCommandManager();
                String string = args2[2];
                String string2 = StringUtils.toCompleteString(args2, 3);
                Intrinsics.checkExpressionValueIsNotNull(string2, "StringUtils.toCompleteString(args, 3)");
                commandManager.registerShortcut(string, string2);
                this.chat("Successfully added shortcut.");
            }
            catch (IllegalArgumentException e) {
                String string = e.getMessage();
                if (string == null) {
                    Intrinsics.throwNpe();
                }
                this.chat(string);
            }
        } else if (args2.length >= 3 && StringsKt.equals(args2[1], "remove", true)) {
            if (LiquidBounce.INSTANCE.getCommandManager().unregisterShortcut(args2[2])) {
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

