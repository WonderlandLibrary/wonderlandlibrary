/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

public class ColorizerFoliage {
    private static int[] foliageBuffer = new int[65536];

    public static void setFoliageBiomeColorizer(int[] nArray) {
        foliageBuffer = nArray;
    }

    public static int getFoliageColorPine() {
        return 0x619961;
    }

    public static int getFoliageColorBasic() {
        return 4764952;
    }

    public static int getFoliageColorBirch() {
        return 8431445;
    }

    public static int getFoliageColor(double d, double d2) {
        int n = (int)((1.0 - d) * 255.0);
        int n2 = (int)((1.0 - (d2 *= d)) * 255.0);
        return foliageBuffer[n2 << 8 | n];
    }
}

