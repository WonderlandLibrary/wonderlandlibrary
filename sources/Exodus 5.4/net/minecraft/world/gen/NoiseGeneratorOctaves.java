/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.NoiseGeneratorImproved;

public class NoiseGeneratorOctaves
extends NoiseGenerator {
    private NoiseGeneratorImproved[] generatorCollection;
    private int octaves;

    public NoiseGeneratorOctaves(Random random, int n) {
        this.octaves = n;
        this.generatorCollection = new NoiseGeneratorImproved[n];
        int n2 = 0;
        while (n2 < n) {
            this.generatorCollection[n2] = new NoiseGeneratorImproved(random);
            ++n2;
        }
    }

    public double[] generateNoiseOctaves(double[] dArray, int n, int n2, int n3, int n4, double d, double d2, double d3) {
        return this.generateNoiseOctaves(dArray, n, 10, n2, n3, 1, n4, d, 1.0, d2);
    }

    public double[] generateNoiseOctaves(double[] dArray, int n, int n2, int n3, int n4, int n5, int n6, double d, double d2, double d3) {
        if (dArray == null) {
            dArray = new double[n4 * n5 * n6];
        } else {
            int n7 = 0;
            while (n7 < dArray.length) {
                dArray[n7] = 0.0;
                ++n7;
            }
        }
        double d4 = 1.0;
        int n8 = 0;
        while (n8 < this.octaves) {
            double d5 = (double)n * d4 * d;
            double d6 = (double)n2 * d4 * d2;
            double d7 = (double)n3 * d4 * d3;
            long l = MathHelper.floor_double_long(d5);
            long l2 = MathHelper.floor_double_long(d7);
            d5 -= (double)l;
            d7 -= (double)l2;
            this.generatorCollection[n8].populateNoiseArray(dArray, d5 += (double)(l %= 0x1000000L), d6, d7 += (double)(l2 %= 0x1000000L), n4, n5, n6, d * d4, d2 * d4, d3 * d4, d4);
            d4 /= 2.0;
            ++n8;
        }
        return dArray;
    }
}

