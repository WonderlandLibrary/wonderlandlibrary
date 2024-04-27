package net.ccbluex.liquidbounce.api.enums;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\bj\bj\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/enums/MaterialType;", "", "(Ljava/lang/String;I)V", "AIR", "WATER", "LAVA", "Pride"})
public final class MaterialType
extends Enum<MaterialType> {
    public static final MaterialType AIR;
    public static final MaterialType WATER;
    public static final MaterialType LAVA;
    private static final MaterialType[] $VALUES;

    static {
        MaterialType[] materialTypeArray = new MaterialType[3];
        MaterialType[] materialTypeArray2 = materialTypeArray;
        materialTypeArray[0] = AIR = new MaterialType();
        materialTypeArray[1] = WATER = new MaterialType();
        materialTypeArray[2] = LAVA = new MaterialType();
        $VALUES = materialTypeArray;
    }

    public static MaterialType[] values() {
        return (MaterialType[])$VALUES.clone();
    }

    public static MaterialType valueOf(String string) {
        return Enum.valueOf(MaterialType.class, string);
    }
}
