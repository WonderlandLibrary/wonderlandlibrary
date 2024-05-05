/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

public final class FadeState
extends Enum<FadeState> {
    public static final /* enum */ FadeState FRIST;
    public static final /* enum */ FadeState IN;
    public static final /* enum */ FadeState STAY;
    public static final /* enum */ FadeState OUT;
    public static final /* enum */ FadeState END;
    public static final /* enum */ FadeState NO;
    private static final /* synthetic */ FadeState[] $VALUES;

    static {
        FadeState[] fadeStateArray = new FadeState[6];
        FadeState[] fadeStateArray2 = fadeStateArray;
        fadeStateArray[0] = FRIST = new FadeState();
        fadeStateArray[1] = IN = new FadeState();
        fadeStateArray[2] = STAY = new FadeState();
        fadeStateArray[3] = OUT = new FadeState();
        fadeStateArray[4] = END = new FadeState();
        fadeStateArray[5] = NO = new FadeState();
        $VALUES = fadeStateArray;
    }

    public static FadeState[] values() {
        return (FadeState[])$VALUES.clone();
    }

    public static FadeState valueOf(String string) {
        return Enum.valueOf(FadeState.class, string);
    }
}

