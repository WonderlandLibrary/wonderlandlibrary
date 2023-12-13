// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.biome;

import java.util.Random;
import java.util.List;
import java.util.Arrays;
import net.minecraft.util.BlockPos;

public class WorldChunkManagerHell extends WorldChunkManager
{
    private BiomeGenBase biomeGenerator;
    private float rainfall;
    private static final String __OBFID = "CL_00000169";
    
    public WorldChunkManagerHell(final BiomeGenBase p_i45374_1_, final float p_i45374_2_) {
        this.biomeGenerator = p_i45374_1_;
        this.rainfall = p_i45374_2_;
    }
    
    @Override
    public BiomeGenBase func_180631_a(final BlockPos p_180631_1_) {
        return this.biomeGenerator;
    }
    
    @Override
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] p_76937_1_, final int p_76937_2_, final int p_76937_3_, final int p_76937_4_, final int p_76937_5_) {
        if (p_76937_1_ == null || p_76937_1_.length < p_76937_4_ * p_76937_5_) {
            p_76937_1_ = new BiomeGenBase[p_76937_4_ * p_76937_5_];
        }
        Arrays.fill(p_76937_1_, 0, p_76937_4_ * p_76937_5_, this.biomeGenerator);
        return p_76937_1_;
    }
    
    @Override
    public float[] getRainfall(float[] p_76936_1_, final int p_76936_2_, final int p_76936_3_, final int p_76936_4_, final int p_76936_5_) {
        if (p_76936_1_ == null || p_76936_1_.length < p_76936_4_ * p_76936_5_) {
            p_76936_1_ = new float[p_76936_4_ * p_76936_5_];
        }
        Arrays.fill(p_76936_1_, 0, p_76936_4_ * p_76936_5_, this.rainfall);
        return p_76936_1_;
    }
    
    @Override
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] p_76933_1_, final int p_76933_2_, final int p_76933_3_, final int p_76933_4_, final int p_76933_5_) {
        if (p_76933_1_ == null || p_76933_1_.length < p_76933_4_ * p_76933_5_) {
            p_76933_1_ = new BiomeGenBase[p_76933_4_ * p_76933_5_];
        }
        Arrays.fill(p_76933_1_, 0, p_76933_4_ * p_76933_5_, this.biomeGenerator);
        return p_76933_1_;
    }
    
    @Override
    public BiomeGenBase[] getBiomeGenAt(final BiomeGenBase[] p_76931_1_, final int p_76931_2_, final int p_76931_3_, final int p_76931_4_, final int p_76931_5_, final boolean p_76931_6_) {
        return this.loadBlockGeneratorData(p_76931_1_, p_76931_2_, p_76931_3_, p_76931_4_, p_76931_5_);
    }
    
    @Override
    public BlockPos findBiomePosition(final int x, final int z, final int range, final List biomes, final Random random) {
        return biomes.contains(this.biomeGenerator) ? new BlockPos(x - range + random.nextInt(range * 2 + 1), 0, z - range + random.nextInt(range * 2 + 1)) : null;
    }
    
    @Override
    public boolean areBiomesViable(final int p_76940_1_, final int p_76940_2_, final int p_76940_3_, final List p_76940_4_) {
        return p_76940_4_.contains(this.biomeGenerator);
    }
}
