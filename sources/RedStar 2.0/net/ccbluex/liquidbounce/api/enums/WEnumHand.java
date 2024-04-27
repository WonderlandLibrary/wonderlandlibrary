package net.ccbluex.liquidbounce.api.enums;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\bj\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/enums/WEnumHand;", "", "(Ljava/lang/String;I)V", "MAIN_HAND", "OFF_HAND", "Pride"})
public final class WEnumHand
extends Enum<WEnumHand> {
    public static final WEnumHand MAIN_HAND;
    public static final WEnumHand OFF_HAND;
    private static final WEnumHand[] $VALUES;

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
