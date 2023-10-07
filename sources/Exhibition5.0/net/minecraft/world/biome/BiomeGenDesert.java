// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.biome;

import net.minecraft.world.gen.feature.WorldGenDesertWells;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;

public class BiomeGenDesert extends BiomeGenBase
{
    private static final String __OBFID = "CL_00000167";
    
    public BiomeGenDesert(final int p_i1977_1_) {
        super(p_i1977_1_);
        this.spawnableCreatureList.clear();
        this.topBlock = Blocks.sand.getDefaultState();
        this.fillerBlock = Blocks.sand.getDefaultState();
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.deadBushPerChunk = 2;
        this.theBiomeDecorator.reedsPerChunk = 50;
        this.theBiomeDecorator.cactiPerChunk = 10;
        this.spawnableCreatureList.clear();
    }
    
    @Override
    public void func_180624_a(final World worldIn, final Random p_180624_2_, final BlockPos p_180624_3_) {
        super.func_180624_a(worldIn, p_180624_2_, p_180624_3_);
        if (p_180624_2_.nextInt(1000) == 0) {
            final int var4 = p_180624_2_.nextInt(16) + 8;
            final int var5 = p_180624_2_.nextInt(16) + 8;
            final BlockPos var6 = worldIn.getHorizon(p_180624_3_.add(var4, 0, var5)).offsetUp();
            new WorldGenDesertWells().generate(worldIn, p_180624_2_, var6);
        }
    }
}
