/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.api.minecraft.client.entity.player;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\t\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t\u00a8\u0006\n"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/WEnumPlayerModelParts;", "", "(Ljava/lang/String;I)V", "CAPE", "JACKET", "LEFT_SLEEVE", "RIGHT_SLEEVE", "LEFT_PANTS_LEG", "RIGHT_PANTS_LEG", "HAT", "LiKingSense"})
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

