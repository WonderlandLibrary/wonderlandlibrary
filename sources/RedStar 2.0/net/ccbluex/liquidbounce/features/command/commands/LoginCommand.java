package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.ccbluex.liquidbounce.utils.login.MinecraftAccount;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\n\b\u000020B¢J02\f\b00H¢\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/LoginCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "Pride"})
public final class LoginCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        String string;
        Intrinsics.checkParameterIsNotNull(args, "args");
        if (args.length <= 1) {
            this.chatSyntax("login <username/email> [password]");
            return;
        }
        if (args.length > 2) {
            String string2 = GuiAltManager.login(new MinecraftAccount(args[1], args[2]));
            string = string2;
            Intrinsics.checkExpressionValueIsNotNull(string2, "GuiAltManager.login(Mine…ccount(args[1], args[2]))");
        } else {
            String string3 = GuiAltManager.login(new MinecraftAccount(args[1]));
            string = string3;
            Intrinsics.checkExpressionValueIsNotNull(string3, "GuiAltManager.login(MinecraftAccount(args[1]))");
        }
        String result = string;
        this.chat(result);
        if (StringsKt.startsWith$default(result, "§cYour name is now", false, 2, null)) {
            if (MinecraftInstance.mc.isIntegratedServerRunning()) {
                return;
            }
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            iWorldClient.sendQuittingDisconnectingPacket();
            ServerUtils.connectToLastServer();
        }
    }

    public LoginCommand() {
        super("login", new String[0]);
    }
}
