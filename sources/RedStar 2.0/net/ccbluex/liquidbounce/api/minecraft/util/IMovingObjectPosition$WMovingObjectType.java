package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\bj\bj\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition$WMovingObjectType;", "", "(Ljava/lang/String;I)V", "MISS", "ENTITY", "BLOCK", "Pride"})
public static final class IMovingObjectPosition$WMovingObjectType
extends Enum<IMovingObjectPosition$WMovingObjectType> {
    public static final IMovingObjectPosition$WMovingObjectType MISS;
    public static final IMovingObjectPosition$WMovingObjectType ENTITY;
    public static final IMovingObjectPosition$WMovingObjectType BLOCK;
    private static final IMovingObjectPosition$WMovingObjectType[] $VALUES;

    static {
        IMovingObjectPosition$WMovingObjectType[] wMovingObjectTypeArray = new IMovingObjectPosition$WMovingObjectType[3];
        IMovingObjectPosition$WMovingObjectType[] wMovingObjectTypeArray2 = wMovingObjectTypeArray;
        wMovingObjectTypeArray[0] = MISS = new IMovingObjectPosition$WMovingObjectType();
        wMovingObjectTypeArray[1] = ENTITY = new IMovingObjectPosition$WMovingObjectType();
        wMovingObjectTypeArray[2] = BLOCK = new IMovingObjectPosition$WMovingObjectType();
        $VALUES = wMovingObjectTypeArray;
    }

    public static IMovingObjectPosition$WMovingObjectType[] values() {
        return (IMovingObjectPosition$WMovingObjectType[])$VALUES.clone();
    }

    public static IMovingObjectPosition$WMovingObjectType valueOf(String string) {
        return Enum.valueOf(IMovingObjectPosition$WMovingObjectType.class, string);
    }
}
