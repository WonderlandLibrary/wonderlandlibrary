// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.init.Blocks;
import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;

public class WorldGenMinable extends WorldGenerator
{
    private final IBlockState oreBlock;
    private final int numberOfBlocks;
    private final Predicate field_175919_c;
    private static final String __OBFID = "CL_00000426";
    
    public WorldGenMinable(final IBlockState p_i45630_1_, final int p_i45630_2_) {
        this(p_i45630_1_, p_i45630_2_, (Predicate)BlockHelper.forBlock(Blocks.stone));
    }
    
    public WorldGenMinable(final IBlockState p_i45631_1_, final int p_i45631_2_, final Predicate p_i45631_3_) {
        this.oreBlock = p_i45631_1_;
        this.numberOfBlocks = p_i45631_2_;
        this.field_175919_c = p_i45631_3_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_) {
        final float var4 = p_180709_2_.nextFloat() * 3.1415927f;
        final double var5 = p_180709_3_.getX() + 8 + MathHelper.sin(var4) * this.numberOfBlocks / 8.0f;
        final double var6 = p_180709_3_.getX() + 8 - MathHelper.sin(var4) * this.numberOfBlocks / 8.0f;
        final double var7 = p_180709_3_.getZ() + 8 + MathHelper.cos(var4) * this.numberOfBlocks / 8.0f;
        final double var8 = p_180709_3_.getZ() + 8 - MathHelper.cos(var4) * this.numberOfBlocks / 8.0f;
        final double var9 = p_180709_3_.getY() + p_180709_2_.nextInt(3) - 2;
        final double var10 = p_180709_3_.getY() + p_180709_2_.nextInt(3) - 2;
        for (int var11 = 0; var11 < this.numberOfBlocks; ++var11) {
            final float var12 = var11 / this.numberOfBlocks;
            final double var13 = var5 + (var6 - var5) * var12;
            final double var14 = var9 + (var10 - var9) * var12;
            final double var15 = var7 + (var8 - var7) * var12;
            final double var16 = p_180709_2_.nextDouble() * this.numberOfBlocks / 16.0;
            final double var17 = (MathHelper.sin(3.1415927f * var12) + 1.0f) * var16 + 1.0;
            final double var18 = (MathHelper.sin(3.1415927f * var12) + 1.0f) * var16 + 1.0;
            final int var19 = MathHelper.floor_double(var13 - var17 / 2.0);
            final int var20 = MathHelper.floor_double(var14 - var18 / 2.0);
            final int var21 = MathHelper.floor_double(var15 - var17 / 2.0);
            final int var22 = MathHelper.floor_double(var13 + var17 / 2.0);
            final int var23 = MathHelper.floor_double(var14 + var18 / 2.0);
            final int var24 = MathHelper.floor_double(var15 + var17 / 2.0);
            for (int var25 = var19; var25 <= var22; ++var25) {
                final double var26 = (var25 + 0.5 - var13) / (var17 / 2.0);
                if (var26 * var26 < 1.0) {
                    for (int var27 = var20; var27 <= var23; ++var27) {
                        final double var28 = (var27 + 0.5 - var14) / (var18 / 2.0);
                        if (var26 * var26 + var28 * var28 < 1.0) {
                            for (int var29 = var21; var29 <= var24; ++var29) {
                                final double var30 = (var29 + 0.5 - var15) / (var17 / 2.0);
                                if (var26 * var26 + var28 * var28 + var30 * var30 < 1.0) {
                                    final BlockPos var31 = new BlockPos(var25, var27, var29);
                                    if (this.field_175919_c.apply((Object)worldIn.getBlockState(var31))) {
                                        worldIn.setBlockState(var31, this.oreBlock, 2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
