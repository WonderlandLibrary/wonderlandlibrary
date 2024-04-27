/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.gui.client.altmanager.GuiAltManager;
import net.dev.important.modules.command.Command;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.ServerUtils;
import net.dev.important.utils.login.MinecraftAccount;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\b\u00a8\u0006\t"}, d2={"Lnet/dev/important/modules/command/commands/LoginCommand;", "Lnet/dev/important/modules/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "LiquidBounce"})
public final class LoginCommand
extends Command {
    public LoginCommand() {
        boolean $i$f$emptyArray = false;
        super("login", new String[0]);
    }

    @Override
    public void execute(@NotNull String[] args2) {
        String string;
        String string2;
        Intrinsics.checkNotNullParameter(args2, "args");
        if (args2.length <= 1) {
            this.chatSyntax("login <username/email> [password]");
            return;
        }
        if (args2.length > 2) {
            string2 = GuiAltManager.login(new MinecraftAccount(args2[1], args2[2]));
            Intrinsics.checkNotNullExpressionValue(string2, "login(MinecraftAccount(args[1], args[2]))");
            string = string2;
        } else {
            string2 = GuiAltManager.login(new MinecraftAccount(args2[1]));
            Intrinsics.checkNotNullExpressionValue(string2, "login(MinecraftAccount(args[1]))");
            string = string2;
        }
        String result = string;
        this.chat(result);
        if (StringsKt.startsWith$default(result, "\u00a7cYour name is now", false, 2, null)) {
            if (MinecraftInstance.mc.func_71387_A()) {
                return;
            }
            MinecraftInstance.mc.field_71441_e.func_72882_A();
            ServerUtils.connectToLastServer();
        }
    }
}

