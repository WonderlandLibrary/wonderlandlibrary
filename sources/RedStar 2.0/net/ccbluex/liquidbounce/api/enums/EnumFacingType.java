package net.ccbluex.liquidbounce.api.enums;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\b\u00002\b0\u00000B\b¢j\bj\bj\bj\bj\bj\b\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/api/enums/EnumFacingType;", "", "(Ljava/lang/String;I)V", "DOWN", "UP", "NORTH", "SOUTH", "WEST", "EAST", "Pride"})
public final class EnumFacingType
extends Enum<EnumFacingType> {
    public static final EnumFacingType DOWN;
    public static final EnumFacingType UP;
    public static final EnumFacingType NORTH;
    public static final EnumFacingType SOUTH;
    public static final EnumFacingType WEST;
    public static final EnumFacingType EAST;
    private static final EnumFacingType[] $VALUES;

    static {
        EnumFacingType[] enumFacingTypeArray = new EnumFacingType[6];
        EnumFacingType[] enumFacingTypeArray2 = enumFacingTypeArray;
        enumFacingTypeArray[0] = DOWN = new EnumFacingType();
        enumFacingTypeArray[1] = UP = new EnumFacingType();
        enumFacingTypeArray[2] = NORTH = new EnumFacingType();
        enumFacingTypeArray[3] = SOUTH = new EnumFacingType();
        enumFacingTypeArray[4] = WEST = new EnumFacingType();
        enumFacingTypeArray[5] = EAST = new EnumFacingType();
        $VALUES = enumFacingTypeArray;
    }

    public static EnumFacingType[] values() {
        return (EnumFacingType[])$VALUES.clone();
    }

    public static EnumFacingType valueOf(String string) {
        return Enum.valueOf(EnumFacingType.class, string);
    }
}
