package net.ccbluex.liquidbounce.features.command.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\n\b\u000020B¢J02\f\b00H¢\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/UsernameCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "Pride"})
public final class UsernameCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        String username = iEntityPlayerSP.getName();
        this.chat("Username: " + username);
        StringSelection stringSelection = new StringSelection(username);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Intrinsics.checkExpressionValueIsNotNull(toolkit, "Toolkit.getDefaultToolkit()");
        toolkit.getSystemClipboard().setContents(stringSelection, stringSelection);
    }

    public UsernameCommand() {
        super("username", "ign");
    }
}
