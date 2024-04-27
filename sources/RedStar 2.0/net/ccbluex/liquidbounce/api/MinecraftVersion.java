package net.ccbluex.liquidbounce.api;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\bj\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/MinecraftVersion;", "", "(Ljava/lang/String;I)V", "MC_1_8", "MC_1_12", "Pride"})
public final class MinecraftVersion
extends Enum<MinecraftVersion> {
    public static final MinecraftVersion MC_1_8;
    public static final MinecraftVersion MC_1_12;
    private static final MinecraftVersion[] $VALUES;

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
