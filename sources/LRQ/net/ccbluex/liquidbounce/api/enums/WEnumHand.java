/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.enums;

public final class WEnumHand
extends Enum<WEnumHand> {
    public static final /* enum */ WEnumHand MAIN_HAND;
    public static final /* enum */ WEnumHand OFF_HAND;
    private static final /* synthetic */ WEnumHand[] $VALUES;

    static {
        WEnumHand[] wEnumHandArray = new WEnumHand[2];
        WEnumHand[] wEnumHandArray2 = wEnumHandArray;
        wEnumHandArray[0] = MAIN_HAND = new WEnumHand();
        wEnumHandArray[1] = OFF_HAND = new WEnumHand();
        $VALUES = wEnumHandArray;
    }

    public static WEnumHand[] values() {
        return (WEnumHand[])$VALUES.clone();
    }

    public static WEnumHand valueOf(String string) {
        return Enum.valueOf(WEnumHand.class, string);
    }
}

