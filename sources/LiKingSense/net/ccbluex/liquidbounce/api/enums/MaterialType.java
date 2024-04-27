/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.api.enums;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2={"Lnet/ccbluex/liquidbounce/api/enums/MaterialType;", "", "(Ljava/lang/String;I)V", "AIR", "WATER", "LAVA", "LiKingSense"})
public final class MaterialType
extends Enum<MaterialType> {
    public static final /* enum */ MaterialType AIR;
    public static final /* enum */ MaterialType WATER;
    public static final /* enum */ MaterialType LAVA;
    private static final /* synthetic */ MaterialType[] $VALUES;

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

