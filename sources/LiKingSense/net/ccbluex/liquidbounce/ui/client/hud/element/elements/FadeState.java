/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/FadeState;", "", "(Ljava/lang/String;I)V", "IN", "STAY", "OUT", "END", "LiKingSense"})
public final class FadeState
extends Enum<FadeState> {
    public static final /* enum */ FadeState IN;
    public static final /* enum */ FadeState STAY;
    public static final /* enum */ FadeState OUT;
    public static final /* enum */ FadeState END;
    private static final /* synthetic */ FadeState[] $VALUES;

    static {
        FadeState[] fadeStateArray = new FadeState[4];
        FadeState[] fadeStateArray2 = fadeStateArray;
        fadeStateArray[0] = IN = new FadeState();
        fadeStateArray[1] = STAY = new FadeState();
        fadeStateArray[2] = OUT = new FadeState();
        fadeStateArray[3] = END = new FadeState();
        $VALUES = fadeStateArray;
    }

    public static FadeState[] values() {
        return (FadeState[])$VALUES.clone();
    }

    public static FadeState valueOf(String string) {
        return Enum.valueOf(FadeState.class, string);
    }
}

