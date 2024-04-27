package net.ccbluex.liquidbounce.features.command;

import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\n\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n \n\b\b&\u000020B!0\n\b00\"0¢J\f0\r20HJ0\r2\f\b00H¢J0\r20HJ\b0\rHJ0\r2\f\b00H&¢J\b0\rHJ!\b002\f\b00H¢R\n\b00¢\n\n\t\b\bR0¢\b\n\u0000\b\n¨"}, d2={"Lnet/ccbluex/liquidbounce/features/command/Command;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "command", "", "alias", "", "(Ljava/lang/String;[Ljava/lang/String;)V", "getAlias", "()[Ljava/lang/String;", "[Ljava/lang/String;", "getCommand", "()Ljava/lang/String;", "chat", "", "msg", "chatSyntax", "syntaxes", "([Ljava/lang/String;)V", "syntax", "chatSyntaxError", "execute", "args", "playEdit", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "Pride"})
public abstract class Command
extends MinecraftInstance {
    @NotNull
    private final String command;
    @NotNull
    private final String[] alias;

    public abstract void execute(@NotNull String[] var1);

    @NotNull
    public List<String> tabComplete(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        return CollectionsKt.emptyList();
    }

    protected final void chat(@NotNull String msg) {
        Intrinsics.checkParameterIsNotNull(msg, "msg");
        ClientUtils.displayChatMessage("§8[§9§lRedStar§8] §3" + msg);
    }

    protected final void chatSyntax(@NotNull String syntax) {
        Intrinsics.checkParameterIsNotNull(syntax, "syntax");
        ClientUtils.displayChatMessage("§8[§9§lRedStar§8] §3Syntax: §7" + LiquidBounce.INSTANCE.getCommandManager().getPrefix() + syntax);
    }

    protected final void chatSyntax(@NotNull String[] syntaxes) {
        Intrinsics.checkParameterIsNotNull(syntaxes, "syntaxes");
        ClientUtils.displayChatMessage("§8[§9§lRedStar§8] §3Syntax:");
        String[] stringArray = syntaxes;
        int n = stringArray.length;
        for (int i = 0; i < n; ++i) {
            String string;
            String syntax;
            String string2 = syntax = stringArray[i];
            StringBuilder stringBuilder = new StringBuilder().append("§8> §7").append(LiquidBounce.INSTANCE.getCommandManager().getPrefix()).append(this.command).append(' ');
            boolean bl = false;
            String string3 = string2;
            if (string3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            Intrinsics.checkExpressionValueIsNotNull(string3.toLowerCase(), "(this as java.lang.String).toLowerCase()");
            ClientUtils.displayChatMessage(stringBuilder.append(string).toString());
        }
    }

    protected final void chatSyntaxError() {
        ClientUtils.displayChatMessage("§8[§9§lRedStar§8] §3Syntax error");
    }

    protected final void playEdit() {
        MinecraftInstance.mc.getSoundHandler().playSound("random.anvil_use", 1.0f);
    }

    @NotNull
    public final String getCommand() {
        return this.command;
    }

    @NotNull
    public final String[] getAlias() {
        return this.alias;
    }

    public Command(@NotNull String command, String ... alias) {
        Intrinsics.checkParameterIsNotNull(command, "command");
        Intrinsics.checkParameterIsNotNull(alias, "alias");
        this.command = command;
        this.alias = alias;
    }
}
