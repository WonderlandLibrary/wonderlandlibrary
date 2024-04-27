/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.enums;

public final class MaterialType
extends Enum {
    public static final /* enum */ MaterialType LAVA;
    public static final /* enum */ MaterialType WATER;
    private static final MaterialType[] $VALUES;
    public static final /* enum */ MaterialType AIR;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private MaterialType() {
        void var2_-1;
        void var1_-1;
    }

    static {
        MaterialType[] materialTypeArray = new MaterialType[3];
        MaterialType[] materialTypeArray2 = materialTypeArray;
        materialTypeArray[0] = AIR = new MaterialType("AIR", 0);
        materialTypeArray[1] = WATER = new MaterialType("WATER", 1);
        materialTypeArray[2] = LAVA = new MaterialType("LAVA", 2);
        $VALUES = materialTypeArray;
    }

    public static MaterialType[] values() {
        return (MaterialType[])$VALUES.clone();
    }

    public static MaterialType valueOf(String string) {
        return Enum.valueOf(MaterialType.class, string);
    }
}

