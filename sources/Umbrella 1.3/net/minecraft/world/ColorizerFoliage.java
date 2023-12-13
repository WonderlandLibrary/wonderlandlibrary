/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world;

public class ColorizerFoliage {
    private static int[] foliageBuffer = new int[65536];
    private static final String __OBFID = "CL_00000135";

    public static void setFoliageBiomeColorizer(int[] p_77467_0_) {
        foliageBuffer = p_77467_0_;
    }

    public static int getFoliageColor(double p_77470_0_, double p_77470_2_) {
        int var4 = (int)((1.0 - p_77470_0_) * 255.0);
        int var5 = (int)((1.0 - (p_77470_2_ *= p_77470_0_)) * 255.0);
        return foliageBuffer[var5 << 8 | var4];
    }

    public static int getFoliageColorPine() {
        return 0x619961;
    }

    public static int getFoliageColorBirch() {
        return 8431445;
    }

    public static int getFoliageColorBasic() {
        return 4764952;
    }
}

