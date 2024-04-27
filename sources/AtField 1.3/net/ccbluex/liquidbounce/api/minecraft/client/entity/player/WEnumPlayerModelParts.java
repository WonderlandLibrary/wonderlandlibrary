/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.minecraft.client.entity.player;

public final class WEnumPlayerModelParts
extends Enum {
    public static final /* enum */ WEnumPlayerModelParts LEFT_PANTS_LEG;
    public static final /* enum */ WEnumPlayerModelParts RIGHT_PANTS_LEG;
    public static final /* enum */ WEnumPlayerModelParts JACKET;
    private static final WEnumPlayerModelParts[] $VALUES;
    public static final /* enum */ WEnumPlayerModelParts HAT;
    public static final /* enum */ WEnumPlayerModelParts LEFT_SLEEVE;
    public static final /* enum */ WEnumPlayerModelParts CAPE;
    public static final /* enum */ WEnumPlayerModelParts RIGHT_SLEEVE;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private WEnumPlayerModelParts() {
        void var2_-1;
        void var1_-1;
    }

    static {
        WEnumPlayerModelParts[] wEnumPlayerModelPartsArray = new WEnumPlayerModelParts[7];
        WEnumPlayerModelParts[] wEnumPlayerModelPartsArray2 = wEnumPlayerModelPartsArray;
        wEnumPlayerModelPartsArray[0] = CAPE = new WEnumPlayerModelParts("CAPE", 0);
        wEnumPlayerModelPartsArray[1] = JACKET = new WEnumPlayerModelParts("JACKET", 1);
        wEnumPlayerModelPartsArray[2] = LEFT_SLEEVE = new WEnumPlayerModelParts("LEFT_SLEEVE", 2);
        wEnumPlayerModelPartsArray[3] = RIGHT_SLEEVE = new WEnumPlayerModelParts("RIGHT_SLEEVE", 3);
        wEnumPlayerModelPartsArray[4] = LEFT_PANTS_LEG = new WEnumPlayerModelParts("LEFT_PANTS_LEG", 4);
        wEnumPlayerModelPartsArray[5] = RIGHT_PANTS_LEG = new WEnumPlayerModelParts("RIGHT_PANTS_LEG", 5);
        wEnumPlayerModelPartsArray[6] = HAT = new WEnumPlayerModelParts("HAT", 6);
        $VALUES = wEnumPlayerModelPartsArray;
    }

    public static WEnumPlayerModelParts valueOf(String string) {
        return Enum.valueOf(WEnumPlayerModelParts.class, string);
    }

    public static WEnumPlayerModelParts[] values() {
        return (WEnumPlayerModelParts[])$VALUES.clone();
    }
}

