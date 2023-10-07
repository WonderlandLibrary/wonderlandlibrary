// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.biome;

import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.World;
import net.minecraft.block.BlockFlower;
import net.minecraft.util.BlockPos;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;
import net.minecraft.entity.monster.EntitySlime;

public class BiomeGenSwamp extends BiomeGenBase
{
    private static final String __OBFID = "CL_00000185";
    
    protected BiomeGenSwamp(final int p_i1988_1_) {
        super(p_i1988_1_);
        this.theBiomeDecorator.treesPerChunk = 2;
        this.theBiomeDecorator.flowersPerChunk = 1;
        this.theBiomeDecorator.deadBushPerChunk = 1;
        this.theBiomeDecorator.mushroomsPerChunk = 8;
        this.theBiomeDecorator.reedsPerChunk = 10;
        this.theBiomeDecorator.clayPerChunk = 1;
        this.theBiomeDecorator.waterlilyPerChunk = 4;
        this.theBiomeDecorator.sandPerChunk2 = 0;
        this.theBiomeDecorator.sandPerChunk = 0;
        this.theBiomeDecorator.grassPerChunk = 5;
        this.waterColorMultiplier = 14745518;
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 1, 1, 1));
    }
    
    @Override
    public WorldGenAbstractTree genBigTreeChance(final Random p_150567_1_) {
        return this.worldGeneratorSwamp;
    }
    
    @Override
    public int func_180627_b(final BlockPos p_180627_1_) {
        final double var2 = BiomeGenSwamp.field_180281_af.func_151601_a(p_180627_1_.getX() * 0.0225, p_180627_1_.getZ() * 0.0225);
        return (var2 < -0.1) ? 5011004 : 6975545;
    }
    
    @Override
    public int func_180625_c(final BlockPos p_180625_1_) {
        return 6975545;
    }
    
    @Override
    public BlockFlower.EnumFlowerType pickRandomFlower(final Random p_180623_1_, final BlockPos p_180623_2_) {
        return BlockFlower.EnumFlowerType.BLUE_ORCHID;
    }
    
    @Override
    public void genTerrainBlocks(final World worldIn, final Random p_180622_2_, final ChunkPrimer p_180622_3_, final int p_180622_4_, final int p_180622_5_, final double p_180622_6_) {
        final double var8 = BiomeGenSwamp.field_180281_af.func_151601_a(p_180622_4_ * 0.25, p_180622_5_ * 0.25);
        if (var8 > 0.0) {
            final int var9 = p_180622_4_ & 0xF;
            final int var10 = p_180622_5_ & 0xF;
            int var11 = 255;
            while (var11 >= 0) {
                if (p_180622_3_.getBlockState(var10, var11, var9).getBlock().getMaterial() != Material.air) {
                    if (var11 != 62 || p_180622_3_.getBlockState(var10, var11, var9).getBlock() == Blocks.water) {
                        break;
                    }
                    p_180622_3_.setBlockState(var10, var11, var9, Blocks.water.getDefaultState());
                    if (var8 < 0.12) {
                        p_180622_3_.setBlockState(var10, var11 + 1, var9, Blocks.waterlily.getDefaultState());
                        break;
                    }
                    break;
                }
                else {
                    --var11;
                }
            }
        }
        this.func_180628_b(worldIn, p_180622_2_, p_180622_3_, p_180622_4_, p_180622_5_, p_180622_6_);
    }
}
