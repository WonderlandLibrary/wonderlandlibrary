package net.ccbluex.liquidbounce.features.command.shortcuts;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.command.shortcuts.Token;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\u000020B\r0¢R0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/command/shortcuts/Literal;", "Lnet/ccbluex/liquidbounce/features/command/shortcuts/Token;", "literal", "", "(Ljava/lang/String;)V", "getLiteral", "()Ljava/lang/String;", "Pride"})
public final class Literal
extends Token {
    @NotNull
    private final String literal;

    @NotNull
    public final String getLiteral() {
        return this.literal;
    }

    public Literal(@NotNull String literal) {
        Intrinsics.checkParameterIsNotNull(literal, "literal");
        this.literal = literal;
    }
}
