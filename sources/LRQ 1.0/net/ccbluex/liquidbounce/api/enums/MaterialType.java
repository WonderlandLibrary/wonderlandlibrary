/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.enums;

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

