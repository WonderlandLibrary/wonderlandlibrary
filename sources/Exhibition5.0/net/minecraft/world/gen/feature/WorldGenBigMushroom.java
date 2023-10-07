// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenBigMushroom extends WorldGenerator
{
    private int mushroomType;
    private static final String __OBFID = "CL_00000415";
    
    public WorldGenBigMushroom(final int p_i2017_1_) {
        super(true);
        this.mushroomType = -1;
        this.mushroomType = p_i2017_1_;
    }
    
    public WorldGenBigMushroom() {
        super(false);
        this.mushroomType = -1;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_) {
        int var4 = p_180709_2_.nextInt(2);
        if (this.mushroomType >= 0) {
            var4 = this.mushroomType;
        }
        final int var5 = p_180709_2_.nextInt(3) + 4;
        boolean var6 = true;
        if (p_180709_3_.getY() < 1 || p_180709_3_.getY() + var5 + 1 >= 256) {
            return false;
        }
        for (int var7 = p_180709_3_.getY(); var7 <= p_180709_3_.getY() + 1 + var5; ++var7) {
            byte var8 = 3;
            if (var7 <= p_180709_3_.getY() + 3) {
                var8 = 0;
            }
            for (int var9 = p_180709_3_.getX() - var8; var9 <= p_180709_3_.getX() + var8 && var6; ++var9) {
                for (int var10 = p_180709_3_.getZ() - var8; var10 <= p_180709_3_.getZ() + var8 && var6; ++var10) {
                    if (var7 >= 0 && var7 < 256) {
                        final Block var11 = worldIn.getBlockState(new BlockPos(var9, var7, var10)).getBlock();
                        if (var11.getMaterial() != Material.air && var11.getMaterial() != Material.leaves) {
                            var6 = false;
                        }
                    }
                    else {
                        var6 = false;
                    }
                }
            }
        }
        if (!var6) {
            return false;
        }
        final Block var12 = worldIn.getBlockState(p_180709_3_.offsetDown()).getBlock();
        if (var12 != Blocks.dirt && var12 != Blocks.grass && var12 != Blocks.mycelium) {
            return false;
        }
        int var13 = p_180709_3_.getY() + var5;
        if (var4 == 1) {
            var13 = p_180709_3_.getY() + var5 - 3;
        }
        for (int var9 = var13; var9 <= p_180709_3_.getY() + var5; ++var9) {
            int var10 = 1;
            if (var9 < p_180709_3_.getY() + var5) {
                ++var10;
            }
            if (var4 == 0) {
                var10 = 3;
            }
            for (int var14 = p_180709_3_.getX() - var10; var14 <= p_180709_3_.getX() + var10; ++var14) {
                for (int var15 = p_180709_3_.getZ() - var10; var15 <= p_180709_3_.getZ() + var10; ++var15) {
                    int var16 = 5;
                    if (var14 == p_180709_3_.getX() - var10) {
                        --var16;
                    }
                    if (var14 == p_180709_3_.getX() + var10) {
                        ++var16;
                    }
                    if (var15 == p_180709_3_.getZ() - var10) {
                        var16 -= 3;
                    }
                    if (var15 == p_180709_3_.getZ() + var10) {
                        var16 += 3;
                    }
                    if (var4 == 0 || var9 < p_180709_3_.getY() + var5) {
                        if (var14 == p_180709_3_.getX() - var10 || var14 == p_180709_3_.getX() + var10) {
                            if (var15 == p_180709_3_.getZ() - var10) {
                                continue;
                            }
                            if (var15 == p_180709_3_.getZ() + var10) {
                                continue;
                            }
                        }
                        if (var14 == p_180709_3_.getX() - (var10 - 1) && var15 == p_180709_3_.getZ() - var10) {
                            var16 = 1;
                        }
                        if (var14 == p_180709_3_.getX() - var10 && var15 == p_180709_3_.getZ() - (var10 - 1)) {
                            var16 = 1;
                        }
                        if (var14 == p_180709_3_.getX() + (var10 - 1) && var15 == p_180709_3_.getZ() - var10) {
                            var16 = 3;
                        }
                        if (var14 == p_180709_3_.getX() + var10 && var15 == p_180709_3_.getZ() - (var10 - 1)) {
                            var16 = 3;
                        }
                        if (var14 == p_180709_3_.getX() - (var10 - 1) && var15 == p_180709_3_.getZ() + var10) {
                            var16 = 7;
                        }
                        if (var14 == p_180709_3_.getX() - var10 && var15 == p_180709_3_.getZ() + (var10 - 1)) {
                            var16 = 7;
                        }
                        if (var14 == p_180709_3_.getX() + (var10 - 1) && var15 == p_180709_3_.getZ() + var10) {
                            var16 = 9;
                        }
                        if (var14 == p_180709_3_.getX() + var10 && var15 == p_180709_3_.getZ() + (var10 - 1)) {
                            var16 = 9;
                        }
                    }
                    if (var16 == 5 && var9 < p_180709_3_.getY() + var5) {
                        var16 = 0;
                    }
                    if (var16 != 0 || p_180709_3_.getY() >= p_180709_3_.getY() + var5 - 1) {
                        final BlockPos var17 = new BlockPos(var14, var9, var15);
                        if (!worldIn.getBlockState(var17).getBlock().isFullBlock()) {
                            this.func_175905_a(worldIn, var17, Block.getBlockById(Block.getIdFromBlock(Blocks.brown_mushroom_block) + var4), var16);
                        }
                    }
                }
            }
        }
        for (int var9 = 0; var9 < var5; ++var9) {
            final Block var18 = worldIn.getBlockState(p_180709_3_.offsetUp(var9)).getBlock();
            if (!var18.isFullBlock()) {
                this.func_175905_a(worldIn, p_180709_3_.offsetUp(var9), Block.getBlockById(Block.getIdFromBlock(Blocks.brown_mushroom_block) + var4), 10);
            }
        }
        return true;
    }
}
