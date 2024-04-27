package net.ccbluex.liquidbounce.features.command.special;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.chat.Client;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.misc.LiquidChat;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000$\n\n\n\b\n\n\u0000\n\n\u0000\n\n\n\b\u000020B¢J02\f\b0\t0\bH¢\nR0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/command/special/PrivateChatCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "lChat", "Lnet/ccbluex/liquidbounce/features/module/modules/misc/LiquidChat;", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "Pride"})
public final class PrivateChatCommand
extends Command {
    private final LiquidChat lChat;

    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        if (args.length > 2) {
            if (!this.lChat.getState()) {
                this.chat("§cError: §7LiquidChat is disabled!");
                return;
            }
            if (!this.lChat.getClient().isConnected()) {
                this.chat("§cError: §LiquidChat is currently not connected to the server!");
                return;
            }
            String target = args[1];
            String message = StringUtils.toCompleteString(args, 2);
            Client client2 = this.lChat.getClient();
            String string = message;
            Intrinsics.checkExpressionValueIsNotNull(string, "message");
            client2.sendPrivateMessage(target, string);
            this.chat("Message was successfully sent.");
        } else {
            this.chatSyntax("pchat <username> <message>");
        }
    }

    public PrivateChatCommand() {
        super("pchat", "privatechat", "lcpm");
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(LiquidChat.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.misc.LiquidChat");
        }
        this.lChat = (LiquidChat)module;
    }
}
