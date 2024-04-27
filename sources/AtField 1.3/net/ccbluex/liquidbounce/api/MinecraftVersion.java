/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api;

public final class MinecraftVersion
extends Enum {
    public static final /* enum */ MinecraftVersion MC_1_8;
    public static final /* enum */ MinecraftVersion MC_1_12;
    private static final MinecraftVersion[] $VALUES;

    public static MinecraftVersion valueOf(String string) {
        return Enum.valueOf(MinecraftVersion.class, string);
    }

    static {
        MinecraftVersion[] minecraftVersionArray = new MinecraftVersion[2];
        MinecraftVersion[] minecraftVersionArray2 = minecraftVersionArray;
        minecraftVersionArray[0] = MC_1_8 = new MinecraftVersion("MC_1_8", 0);
        minecraftVersionArray[1] = MC_1_12 = new MinecraftVersion("MC_1_12", 1);
        $VALUES = minecraftVersionArray;
    }

    public static MinecraftVersion[] values() {
        return (MinecraftVersion[])$VALUES.clone();
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private MinecraftVersion() {
        void var2_-1;
        void var1_-1;
    }
}

