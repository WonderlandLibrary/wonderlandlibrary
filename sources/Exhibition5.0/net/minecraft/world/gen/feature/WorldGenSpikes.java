// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.Block;

public class WorldGenSpikes extends WorldGenerator
{
    private Block field_150520_a;
    private static final String __OBFID = "CL_00000433";
    
    public WorldGenSpikes(final Block p_i45464_1_) {
        this.field_150520_a = p_i45464_1_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_) {
        if (worldIn.isAirBlock(p_180709_3_) && worldIn.getBlockState(p_180709_3_.offsetDown()).getBlock() == this.field_150520_a) {
            final int var4 = p_180709_2_.nextInt(32) + 6;
            final int var5 = p_180709_2_.nextInt(4) + 1;
            for (int var6 = p_180709_3_.getX() - var5; var6 <= p_180709_3_.getX() + var5; ++var6) {
                for (int var7 = p_180709_3_.getZ() - var5; var7 <= p_180709_3_.getZ() + var5; ++var7) {
                    final int var8 = var6 - p_180709_3_.getX();
                    final int var9 = var7 - p_180709_3_.getZ();
                    if (var8 * var8 + var9 * var9 <= var5 * var5 + 1 && worldIn.getBlockState(new BlockPos(var6, p_180709_3_.getY() - 1, var7)).getBlock() != this.field_150520_a) {
                        return false;
                    }
                }
            }
            for (int var6 = p_180709_3_.getY(); var6 < p_180709_3_.getY() + var4 && var6 < 256; ++var6) {
                for (int var7 = p_180709_3_.getX() - var5; var7 <= p_180709_3_.getX() + var5; ++var7) {
                    for (int var8 = p_180709_3_.getZ() - var5; var8 <= p_180709_3_.getZ() + var5; ++var8) {
                        final int var9 = var7 - p_180709_3_.getX();
                        final int var10 = var8 - p_180709_3_.getZ();
                        if (var9 * var9 + var10 * var10 <= var5 * var5 + 1) {
                            worldIn.setBlockState(new BlockPos(var7, var6, var8), Blocks.obsidian.getDefaultState(), 2);
                        }
                    }
                }
            }
            final EntityEnderCrystal var11 = new EntityEnderCrystal(worldIn);
            var11.setLocationAndAngles(p_180709_3_.getX() + 0.5f, p_180709_3_.getY() + var4, p_180709_3_.getZ() + 0.5f, p_180709_2_.nextFloat() * 360.0f, 0.0f);
            worldIn.spawnEntityInWorld(var11);
            worldIn.setBlockState(p_180709_3_.offsetUp(var4), Blocks.bedrock.getDefaultState(), 2);
            return true;
        }
        return false;
    }
}
