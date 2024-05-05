/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

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

