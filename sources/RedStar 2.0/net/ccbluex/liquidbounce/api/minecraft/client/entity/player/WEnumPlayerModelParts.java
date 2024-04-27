package net.ccbluex.liquidbounce.api.minecraft.client.entity.player;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\t\b\u00002\b0\u00000B\b¢j\bj\bj\bj\bj\bj\b\bj\b\t¨\n"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/WEnumPlayerModelParts;", "", "(Ljava/lang/String;I)V", "CAPE", "JACKET", "LEFT_SLEEVE", "RIGHT_SLEEVE", "LEFT_PANTS_LEG", "RIGHT_PANTS_LEG", "HAT", "Pride"})
public final class WEnumPlayerModelParts
extends Enum<WEnumPlayerModelParts> {
    public static final WEnumPlayerModelParts CAPE;
    public static final WEnumPlayerModelParts JACKET;
    public static final WEnumPlayerModelParts LEFT_SLEEVE;
    public static final WEnumPlayerModelParts RIGHT_SLEEVE;
    public static final WEnumPlayerModelParts LEFT_PANTS_LEG;
    public static final WEnumPlayerModelParts RIGHT_PANTS_LEG;
    public static final WEnumPlayerModelParts HAT;
    private static final WEnumPlayerModelParts[] $VALUES;

    static {
        WEnumPlayerModelParts[] wEnumPlayerModelPartsArray = new WEnumPlayerModelParts[7];
        WEnumPlayerModelParts[] wEnumPlayerModelPartsArray2 = wEnumPlayerModelPartsArray;
        wEnumPlayerModelPartsArray[0] = CAPE = new WEnumPlayerModelParts();
        wEnumPlayerModelPartsArray[1] = JACKET = new WEnumPlayerModelParts();
        wEnumPlayerModelPartsArray[2] = LEFT_SLEEVE = new WEnumPlayerModelParts();
        wEnumPlayerModelPartsArray[3] = RIGHT_SLEEVE = new WEnumPlayerModelParts();
        wEnumPlayerModelPartsArray[4] = LEFT_PANTS_LEG = new WEnumPlayerModelParts();
        wEnumPlayerModelPartsArray[5] = RIGHT_PANTS_LEG = new WEnumPlayerModelParts();
        wEnumPlayerModelPartsArray[6] = HAT = new WEnumPlayerModelParts();
        $VALUES = wEnumPlayerModelPartsArray;
    }

    public static WEnumPlayerModelParts[] values() {
        return (WEnumPlayerModelParts[])$VALUES.clone();
    }

    public static WEnumPlayerModelParts valueOf(String string) {
        return Enum.valueOf(WEnumPlayerModelParts.class, string);
    }
}
