// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen;

import net.minecraft.util.MathHelper;
import java.util.Random;

public class NoiseGeneratorOctaves extends NoiseGenerator
{
    private NoiseGeneratorImproved[] generatorCollection;
    private int octaves;
    private static final String __OBFID = "CL_00000535";
    
    public NoiseGeneratorOctaves(final Random p_i2111_1_, final int p_i2111_2_) {
        this.octaves = p_i2111_2_;
        this.generatorCollection = new NoiseGeneratorImproved[p_i2111_2_];
        for (int var3 = 0; var3 < p_i2111_2_; ++var3) {
            this.generatorCollection[var3] = new NoiseGeneratorImproved(p_i2111_1_);
        }
    }
    
    public double[] generateNoiseOctaves(double[] p_76304_1_, final int p_76304_2_, final int p_76304_3_, final int p_76304_4_, final int p_76304_5_, final int p_76304_6_, final int p_76304_7_, final double p_76304_8_, final double p_76304_10_, final double p_76304_12_) {
        if (p_76304_1_ == null) {
            p_76304_1_ = new double[p_76304_5_ * p_76304_6_ * p_76304_7_];
        }
        else {
            for (int var14 = 0; var14 < p_76304_1_.length; ++var14) {
                p_76304_1_[var14] = 0.0;
            }
        }
        double var15 = 1.0;
        for (int var16 = 0; var16 < this.octaves; ++var16) {
            double var17 = p_76304_2_ * var15 * p_76304_8_;
            final double var18 = p_76304_3_ * var15 * p_76304_10_;
            double var19 = p_76304_4_ * var15 * p_76304_12_;
            long var20 = MathHelper.floor_double_long(var17);
            long var21 = MathHelper.floor_double_long(var19);
            var17 -= var20;
            var19 -= var21;
            var20 %= 16777216L;
            var21 %= 16777216L;
            var17 += var20;
            var19 += var21;
            this.generatorCollection[var16].populateNoiseArray(p_76304_1_, var17, var18, var19, p_76304_5_, p_76304_6_, p_76304_7_, p_76304_8_ * var15, p_76304_10_ * var15, p_76304_12_ * var15, var15);
            var15 /= 2.0;
        }
        return p_76304_1_;
    }
    
    public double[] generateNoiseOctaves(final double[] p_76305_1_, final int p_76305_2_, final int p_76305_3_, final int p_76305_4_, final int p_76305_5_, final double p_76305_6_, final double p_76305_8_, final double p_76305_10_) {
        return this.generateNoiseOctaves(p_76305_1_, p_76305_2_, 10, p_76305_3_, p_76305_4_, 1, p_76305_5_, p_76305_6_, 1.0, p_76305_8_);
    }
}
