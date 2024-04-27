package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\n\b\u000020B¢J02\f\b00H¢\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/HClipCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "Pride"})
public final class HClipCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        if (args.length > 1) {
            try {
                String string = args[1];
                boolean bl = false;
                MovementUtils.forward(Double.parseDouble(string));
                this.chat("You were teleported.");
            }
            catch (NumberFormatException exception) {
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
