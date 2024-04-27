/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.api;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004\u00a8\u0006\u0005"}, d2={"Lnet/ccbluex/liquidbounce/api/MinecraftVersion;", "", "(Ljava/lang/String;I)V", "MC_1_8", "MC_1_12", "LiKingSense"})
public final class MinecraftVersion
extends Enum<MinecraftVersion> {
    public static final /* enum */ MinecraftVersion MC_1_8;
    public static final /* enum */ MinecraftVersion MC_1_12;
    private static final /* synthetic */ MinecraftVersion[] $VALUES;

    static {
        MinecraftVersion[] minecraftVersionArray = new MinecraftVersion[2];
        MinecraftVersion[] minecraftVersionArray2 = minecraftVersionArray;
        minecraftVersionArray[0] = MC_1_8 = new MinecraftVersion();
        minecraftVersionArray[1] = MC_1_12 = new MinecraftVersion();
        $VALUES = minecraftVersionArray;
    }

    public static MinecraftVersion[] values() {
        return (MinecraftVersion[])$VALUES.clone();
    }

    public static MinecraftVersion valueOf(String string) {
        return Enum.valueOf(MinecraftVersion.class, string);
    }
}

