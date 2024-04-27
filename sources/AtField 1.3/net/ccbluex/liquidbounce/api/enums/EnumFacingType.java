/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.enums;

public final class EnumFacingType
extends Enum {
    public static final /* enum */ EnumFacingType UP;
    public static final /* enum */ EnumFacingType NORTH;
    public static final /* enum */ EnumFacingType SOUTH;
    public static final /* enum */ EnumFacingType WEST;
    public static final /* enum */ EnumFacingType EAST;
    public static final /* enum */ EnumFacingType DOWN;
    private static final EnumFacingType[] $VALUES;

    public static EnumFacingType[] values() {
        return (EnumFacingType[])$VALUES.clone();
    }

    static {
        EnumFacingType[] enumFacingTypeArray = new EnumFacingType[6];
        EnumFacingType[] enumFacingTypeArray2 = enumFacingTypeArray;
        enumFacingTypeArray[0] = DOWN = new EnumFacingType("DOWN", 0);
        enumFacingTypeArray[1] = UP = new EnumFacingType("UP", 1);
        enumFacingTypeArray[2] = NORTH = new EnumFacingType("NORTH", 2);
        enumFacingTypeArray[3] = SOUTH = new EnumFacingType("SOUTH", 3);
        enumFacingTypeArray[4] = WEST = new EnumFacingType("WEST", 4);
        enumFacingTypeArray[5] = EAST = new EnumFacingType("EAST", 5);
        $VALUES = enumFacingTypeArray;
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private EnumFacingType() {
        void var2_-1;
        void var1_-1;
    }

    public static EnumFacingType valueOf(String string) {
        return Enum.valueOf(EnumFacingType.class, string);
    }
}

