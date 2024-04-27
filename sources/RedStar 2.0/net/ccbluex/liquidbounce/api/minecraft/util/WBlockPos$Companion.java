package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\b\n\b\n\b\n\n\b\b\u000020B\b¢R0¢\b\n\u0000\bR0XD¢\b\n\u0000\b\bR\t0XD¢\b\n\u0000\b\nR0\f¢\b\n\u0000\b\rR0XD¢\b\n\u0000\bR0XD¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos$Companion;", "", "()V", "NUM_X_BITS", "", "getNUM_X_BITS", "()I", "NUM_Y_BITS", "getNUM_Y_BITS", "NUM_Z_BITS", "getNUM_Z_BITS", "ORIGIN", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getORIGIN", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "X_SHIFT", "getX_SHIFT", "Y_SHIFT", "getY_SHIFT", "Pride"})
public static final class WBlockPos$Companion {
    @NotNull
    public final WBlockPos getORIGIN() {
        return ORIGIN;
    }

    public final int getNUM_X_BITS() {
        return NUM_X_BITS;
    }

    public final int getNUM_Z_BITS() {
        return NUM_Z_BITS;
    }

    public final int getNUM_Y_BITS() {
        return NUM_Y_BITS;
    }

    public final int getY_SHIFT() {
        return Y_SHIFT;
    }

    public final int getX_SHIFT() {
        return X_SHIFT;
    }

    private WBlockPos$Companion() {
    }

    public WBlockPos$Companion(DefaultConstructorMarker $constructor_marker) {
        this();
    }
}
