/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.api.enums;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\b\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\b\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/api/enums/EnumFacingType;", "", "(Ljava/lang/String;I)V", "DOWN", "UP", "NORTH", "SOUTH", "WEST", "EAST", "LiKingSense"})
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

