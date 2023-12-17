// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorPerlin extends NoiseGenerator
{
    private final NoiseGeneratorSimplex[] noiseLevels;
    private final int levels;
    
    public NoiseGeneratorPerlin(final Random seed, final int levelsIn) {
        this.levels = levelsIn;
        this.noiseLevels = new NoiseGeneratorSimplex[levelsIn];
        for (int i = 0; i < levelsIn; ++i) {
            this.noiseLevels[i] = new NoiseGeneratorSimplex(seed);
        }
    }
    
    public double getValue(final double p_151601_1_, final double p_151601_3_) {
        double d0 = 0.0;
        double d2 = 1.0;
        for (int i = 0; i < this.levels; ++i) {
            d0 += this.noiseLevels[i].getValue(p_151601_1_ * d2, p_151601_3_ * d2) / d2;
            d2 /= 2.0;
        }
        return d0;
    }
    
    public double[] getRegion(final double[] p_151599_1_, final double p_151599_2_, final double p_151599_4_, final int p_151599_6_, final int p_151599_7_, final double p_151599_8_, final double p_151599_10_, final double p_151599_12_) {
        return this.getRegion(p_151599_1_, p_151599_2_, p_151599_4_, p_151599_6_, p_151599_7_, p_151599_8_, p_151599_10_, p_151599_12_, 0.5);
    }
    
    public double[] getRegion(double[] p_151600_1_, final double p_151600_2_, final double p_151600_4_, final int p_151600_6_, final int p_151600_7_, final double p_151600_8_, final double p_151600_10_, final double p_151600_12_, final double p_151600_14_) {
        if (p_151600_1_ != null && p_151600_1_.length >= p_151600_6_ * p_151600_7_) {
            for (int i = 0; i < p_151600_1_.length; ++i) {
                p_151600_1_[i] = 0.0;
            }
        }
        else {
            p_151600_1_ = new double[p_151600_6_ * p_151600_7_];
        }
        double d1 = 1.0;
        double d2 = 1.0;
        for (int j = 0; j < this.levels; ++j) {
            this.noiseLevels[j].add(p_151600_1_, p_151600_2_, p_151600_4_, p_151600_6_, p_151600_7_, p_151600_8_ * d2 * d1, p_151600_10_ * d2 * d1, 0.55 / d1);
            d2 *= p_151600_12_;
            d1 *= p_151600_14_;
        }
        return p_151600_1_;
    }
}
