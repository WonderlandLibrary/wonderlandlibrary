/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

public final class FadeState
extends Enum {
    public static final /* enum */ FadeState STAY;
    public static final /* enum */ FadeState END;
    public static final /* enum */ FadeState IN;
    private static final FadeState[] $VALUES;
    public static final /* enum */ FadeState OUT;

    static {
        FadeState[] fadeStateArray = new FadeState[4];
        FadeState[] fadeStateArray2 = fadeStateArray;
        fadeStateArray[0] = IN = new FadeState("IN", 0);
        fadeStateArray[1] = STAY = new FadeState("STAY", 1);
        fadeStateArray[2] = OUT = new FadeState("OUT", 2);
        fadeStateArray[3] = END = new FadeState("END", 3);
        $VALUES = fadeStateArray;
    }

    public static FadeState valueOf(String string) {
        return Enum.valueOf(FadeState.class, string);
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private FadeState() {
        void var2_-1;
        void var1_-1;
    }

    public static FadeState[] values() {
        return (FadeState[])$VALUES.clone();
    }
}

