/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.api.enums;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004\u00a8\u0006\u0005"}, d2={"Lnet/ccbluex/liquidbounce/api/enums/WEnumHand;", "", "(Ljava/lang/String;I)V", "MAIN_HAND", "OFF_HAND", "LiKingSense"})
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

