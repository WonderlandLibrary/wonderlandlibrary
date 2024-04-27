/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.modules.command.Command;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.misc.StringUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\b\u00a8\u0006\t"}, d2={"Lnet/dev/important/modules/command/commands/SayCommand;", "Lnet/dev/important/modules/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "LiquidBounce"})
public final class SayCommand
extends Command {
    public SayCommand() {
        boolean $i$f$emptyArray = false;
        super("say", new String[0]);
    }

    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkNotNullParameter(args2, "args");
        if (args2.length > 1) {
            MinecraftInstance.mc.field_71439_g.func_71165_d(StringUtils.toCompleteString(args2, 1));
            this.chat("Message was sent to the chat.");
            return;
        }
        this.chatSyntax("say <message...>");
    }
}

