package net.ccbluex.liquidbounce.features.module.modules.misc;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\b\n\n\b\b\u000020B\b¢R0X¢\n\u0000\b\"\b\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/misc/LiquidChat$Companion;", "", "()V", "jwtToken", "", "getJwtToken", "()Ljava/lang/String;", "setJwtToken", "(Ljava/lang/String;)V", "Pride"})
public static final class LiquidChat$Companion {
    @NotNull
    public final String getJwtToken() {
        return jwtToken;
    }

    public final void setJwtToken(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull(string, "<set-?>");
        jwtToken = string;
    }

    private LiquidChat$Companion() {
    }

    public LiquidChat$Companion(DefaultConstructorMarker $constructor_marker) {
        this();
    }
}
