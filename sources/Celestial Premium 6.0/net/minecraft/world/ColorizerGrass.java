/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world;

public class ColorizerGrass {
    private static int[] grassBuffer = new int[65536];

    public static void setGrassBiomeColorizer(int[] grassBufferIn) {
        grassBuffer = grassBufferIn;
    }

    public static int getGrassColor(double temperature, double humidity) {
        int j = (int)((1.0 - (humidity *= temperature)) * 255.0);
        int i = (int)((1.0 - temperature) * 255.0);
        int k = j << 8 | i;
        return k > grassBuffer.length ? -65281 : grassBuffer[k];
    }
}

