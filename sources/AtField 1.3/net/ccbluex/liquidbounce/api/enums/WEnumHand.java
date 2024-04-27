/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.enums;

public final class WEnumHand
extends Enum {
    private static final WEnumHand[] $VALUES;
    public static final /* enum */ WEnumHand MAIN_HAND;
    public static final /* enum */ WEnumHand OFF_HAND;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private WEnumHand() {
        void var2_-1;
        void var1_-1;
    }

    public static WEnumHand[] values() {
        return (WEnumHand[])$VALUES.clone();
    }

    static {
        WEnumHand[] wEnumHandArray = new WEnumHand[2];
        WEnumHand[] wEnumHandArray2 = wEnumHandArray;
        wEnumHandArray[0] = MAIN_HAND = new WEnumHand("MAIN_HAND", 0);
        wEnumHandArray[1] = OFF_HAND = new WEnumHand("OFF_HAND", 1);
        $VALUES = wEnumHandArray;
    }

    public static WEnumHand valueOf(String string) {
        return Enum.valueOf(WEnumHand.class, string);
    }
}

