/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world;

public class ColorizerGrass {
    private static int[] grassBuffer = new int[65536];
    private static final String __OBFID = "CL_00000138";

    public static void setGrassBiomeColorizer(int[] p_77479_0_) {
        grassBuffer = p_77479_0_;
    }

    public static int getGrassColor(double p_77480_0_, double p_77480_2_) {
        int var5 = (int)((1.0 - (p_77480_2_ *= p_77480_0_)) * 255.0);
        int var4 = (int)((1.0 - p_77480_0_) * 255.0);
        int var6 = var5 << 8 | var4;
        return var6 > grassBuffer.length ? -65281 : grassBuffer[var6];
    }
}

