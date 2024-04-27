package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\n\b\u000020B¢J02\f\b00H¢\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/PrefixCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "Pride"})
public final class PrefixCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        if (args.length <= 1) {
            this.chatSyntax("prefix <character>");
            return;
        }
        String prefix = args[1];
        if (prefix.length() > 1) {
            this.chat("§cPrefix can only be one character long!");
            return;
        }
        LiquidBounce.INSTANCE.getCommandManager().setPrefix(StringsKt.single(prefix));
        LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().valuesConfig);
        this.chat("Successfully changed command prefix to '§8" + prefix + "§3'");
    }

    public PrefixCommand() {
        super("prefix", new String[0]);
    }
}
