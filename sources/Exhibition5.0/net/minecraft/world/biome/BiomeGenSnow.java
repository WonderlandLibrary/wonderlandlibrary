// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.biome;

import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenIcePath;
import net.minecraft.world.gen.feature.WorldGenIceSpike;

public class BiomeGenSnow extends BiomeGenBase
{
    private boolean field_150615_aC;
    private WorldGenIceSpike field_150616_aD;
    private WorldGenIcePath field_150617_aE;
    private static final String __OBFID = "CL_00000174";
    
    public BiomeGenSnow(final int p_i45378_1_, final boolean p_i45378_2_) {
        super(p_i45378_1_);
        this.field_150616_aD = new WorldGenIceSpike();
        this.field_150617_aE = new WorldGenIcePath(4);
        this.field_150615_aC = p_i45378_2_;
        if (p_i45378_2_) {
            this.topBlock = Blocks.snow.getDefaultState();
        }
        this.spawnableCreatureList.clear();
    }
    
    @Override
    public void func_180624_a(final World worldIn, final Random p_180624_2_, final BlockPos p_180624_3_) {
        if (this.field_150615_aC) {
            for (int var4 = 0; var4 < 3; ++var4) {
                final int var5 = p_180624_2_.nextInt(16) + 8;
                final int var6 = p_180624_2_.nextInt(16) + 8;
                this.field_150616_aD.generate(worldIn, p_180624_2_, worldIn.getHorizon(p_180624_3_.add(var5, 0, var6)));
            }
            for (int var4 = 0; var4 < 2; ++var4) {
                final int var5 = p_180624_2_.nextInt(16) + 8;
                final int var6 = p_180624_2_.nextInt(16) + 8;
                this.field_150617_aE.generate(worldIn, p_180624_2_, worldIn.getHorizon(p_180624_3_.add(var5, 0, var6)));
            }
        }
        super.func_180624_a(worldIn, p_180624_2_, p_180624_3_);
    }
    
    @Override
    public WorldGenAbstractTree genBigTreeChance(final Random p_150567_1_) {
        return new WorldGenTaiga2(false);
    }
    
    @Override
    protected BiomeGenBase createMutatedBiome(final int p_180277_1_) {
        final BiomeGenBase var2 = new BiomeGenSnow(p_180277_1_, true).func_150557_a(13828095, true).setBiomeName(this.biomeName + " Spikes").setEnableSnow().setTemperatureRainfall(0.0f, 0.5f).setHeight(new Height(this.minHeight + 0.1f, this.maxHeight + 0.1f));
        var2.minHeight = this.minHeight + 0.3f;
        var2.maxHeight = this.maxHeight + 0.4f;
        return var2;
    }
}
