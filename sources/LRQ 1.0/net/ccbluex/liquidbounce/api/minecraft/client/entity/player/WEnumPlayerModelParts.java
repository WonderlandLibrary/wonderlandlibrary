/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.minecraft.client.entity.player;

public final class WEnumPlayerModelParts
extends Enum<WEnumPlayerModelParts> {
    public static final /* enum */ WEnumPlayerModelParts CAPE;
    public static final /* enum */ WEnumPlayerModelParts JACKET;
    public static final /* enum */ WEnumPlayerModelParts LEFT_SLEEVE;
    public static final /* enum */ WEnumPlayerModelParts RIGHT_SLEEVE;
    public static final /* enum */ WEnumPlayerModelParts LEFT_PANTS_LEG;
    public static final /* enum */ WEnumPlayerModelParts RIGHT_PANTS_LEG;
    public static final /* enum */ WEnumPlayerModelParts HAT;
    private static final /* synthetic */ WEnumPlayerModelParts[] $VALUES;

    static {
        WEnumPlayerModelParts[] wEnumPlayerModelPartsArray = new WEnumPlayerModelParts[7];
        WEnumPlayerModelParts[] wEnumPlayerModelPartsArray2 = wEnumPlayerModelPartsArray;
        wEnumPlayerModelPartsArray[0] = CAPE = new WEnumPlayerModelParts();
        wEnumPlayerModelPartsArray[1] = JACKET = new WEnumPlayerModelParts();
        wEnumPlayerModelPartsArray[2] = LEFT_SLEEVE = new WEnumPlayerModelParts();
        wEnumPlayerModelPartsArray[3] = RIGHT_SLEEVE = new WEnumPlayerModelParts();
        wEnumPlayerModelPartsArray[4] = LEFT_PANTS_LEG = new WEnumPlayerModelParts();
        wEnumPlayerModelPartsArray[5] = RIGHT_PANTS_LEG = new WEnumPlayerModelParts();
        wEnumPlayerModelPartsArray[6] = HAT = new WEnumPlayerModelParts();
        $VALUES = wEnumPlayerModelPartsArray;
    }

    public static WEnumPlayerModelParts[] values() {
        return (WEnumPlayerModelParts[])$VALUES.clone();
    }

    public static WEnumPlayerModelParts valueOf(String string) {
        return Enum.valueOf(WEnumPlayerModelParts.class, string);
    }
}

