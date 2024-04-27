package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.command.CommandManager;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\n\b\u000020B¢J02\f\b00H¢\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/ShortcutCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "Pride"})
public final class ShortcutCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        if (args.length > 3 && StringsKt.equals(args[1], "add", true)) {
            try {
                CommandManager commandManager = LiquidBounce.INSTANCE.getCommandManager();
                String string = args[2];
                String string2 = StringUtils.toCompleteString(args, 3);
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
        } else if (args.length >= 3 && StringsKt.equals(args[1], "remove", true)) {
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
