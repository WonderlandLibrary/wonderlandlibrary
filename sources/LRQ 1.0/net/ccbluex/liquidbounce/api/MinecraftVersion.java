/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api;

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

