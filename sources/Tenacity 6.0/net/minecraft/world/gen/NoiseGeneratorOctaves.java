// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import net.minecraft.util.MathHelper;
import java.util.Random;

public class NoiseGeneratorOctaves extends NoiseGenerator
{
    private NoiseGeneratorImproved[] generatorCollection;
    private int octaves;
    
    public NoiseGeneratorOctaves(final Random seed, final int octavesIn) {
        this.octaves = octavesIn;
        this.generatorCollection = new NoiseGeneratorImproved[octavesIn];
        for (int i = 0; i < octavesIn; ++i) {
            this.generatorCollection[i] = new NoiseGeneratorImproved(seed);
        }
    }
    
    public double[] generateNoiseOctaves(double[] noiseArray, final int xOffset, final int yOffset, final int zOffset, final int xSize, final int ySize, final int zSize, final double xScale, final double yScale, final double zScale) {
        if (noiseArray == null) {
            noiseArray = new double[xSize * ySize * zSize];
        }
        else {
            for (int i = 0; i < noiseArray.length; ++i) {
                noiseArray[i] = 0.0;
            }
        }
        double d3 = 1.0;
        for (int j = 0; j < this.octaves; ++j) {
            double d4 = xOffset * d3 * xScale;
            final double d5 = yOffset * d3 * yScale;
            double d6 = zOffset * d3 * zScale;
            long k = MathHelper.floor_double_long(d4);
            long l = MathHelper.floor_double_long(d6);
            d4 -= k;
            d6 -= l;
            k %= 16777216L;
            l %= 16777216L;
            d4 += k;
            d6 += l;
            this.generatorCollection[j].populateNoiseArray(noiseArray, d4, d5, d6, xSize, ySize, zSize, xScale * d3, yScale * d3, zScale * d3, d3);
            d3 /= 2.0;
        }
        return noiseArray;
    }
    
    public double[] generateNoiseOctaves(final double[] noiseArray, final int xOffset, final int zOffset, final int xSize, final int zSize, final double xScale, final double zScale, final double p_76305_10_) {
        return this.generateNoiseOctaves(noiseArray, xOffset, 10, zOffset, xSize, 1, zSize, xScale, 1.0, zScale);
    }
}
