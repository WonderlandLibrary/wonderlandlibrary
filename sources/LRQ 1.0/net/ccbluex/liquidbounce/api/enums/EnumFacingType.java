/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.enums;

public final class EnumFacingType
extends Enum<EnumFacingType> {
    public static final /* enum */ EnumFacingType DOWN;
    public static final /* enum */ EnumFacingType UP;
    public static final /* enum */ EnumFacingType NORTH;
    public static final /* enum */ EnumFacingType SOUTH;
    public static final /* enum */ EnumFacingType WEST;
    public static final /* enum */ EnumFacingType EAST;
    private static final /* synthetic */ EnumFacingType[] $VALUES;

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

