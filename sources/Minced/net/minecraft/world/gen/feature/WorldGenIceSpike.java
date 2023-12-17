// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.MathHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenIceSpike extends WorldGenerator
{
    @Override
    public boolean generate(final World worldIn, final Random rand, BlockPos position) {
        while (worldIn.isAirBlock(position) && position.getY() > 2) {
            position = position.down();
        }
        if (worldIn.getBlockState(position).getBlock() != Blocks.SNOW) {
            return false;
        }
        position = position.up(rand.nextInt(4));
        final int i = rand.nextInt(4) + 7;
        final int j = i / 4 + rand.nextInt(2);
        if (j > 1 && rand.nextInt(60) == 0) {
            position = position.up(10 + rand.nextInt(30));
        }
        for (int k = 0; k < i; ++k) {
            final float f = (1.0f - k / (float)i) * j;
            for (int l = MathHelper.ceil(f), i2 = -l; i2 <= l; ++i2) {
                final float f2 = MathHelper.abs(i2) - 0.25f;
                for (int j2 = -l; j2 <= l; ++j2) {
                    final float f3 = MathHelper.abs(j2) - 0.25f;
                    if (((i2 == 0 && j2 == 0) || f2 * f2 + f3 * f3 <= f * f) && ((i2 != -l && i2 != l && j2 != -l && j2 != l) || rand.nextFloat() <= 0.75f)) {
                        IBlockState iblockstate = worldIn.getBlockState(position.add(i2, k, j2));
                        Block block = iblockstate.getBlock();
                        if (iblockstate.getMaterial() == Material.AIR || block == Blocks.DIRT || block == Blocks.SNOW || block == Blocks.ICE) {
                            this.setBlockAndNotifyAdequately(worldIn, position.add(i2, k, j2), Blocks.PACKED_ICE.getDefaultState());
                        }
                        if (k != 0 && l > 1) {
                            iblockstate = worldIn.getBlockState(position.add(i2, -k, j2));
                            block = iblockstate.getBlock();
                            if (iblockstate.getMaterial() == Material.AIR || block == Blocks.DIRT || block == Blocks.SNOW || block == Blocks.ICE) {
                                this.setBlockAndNotifyAdequately(worldIn, position.add(i2, -k, j2), Blocks.PACKED_ICE.getDefaultState());
                            }
                        }
                    }
                }
            }
        }
        int k2 = j - 1;
        if (k2 < 0) {
            k2 = 0;
        }
        else if (k2 > 1) {
            k2 = 1;
        }
        for (int l2 = -k2; l2 <= k2; ++l2) {
            for (int i3 = -k2; i3 <= k2; ++i3) {
                BlockPos blockpos = position.add(l2, -1, i3);
                int j3 = 50;
                if (Math.abs(l2) == 1 && Math.abs(i3) == 1) {
                    j3 = rand.nextInt(5);
                }
                while (blockpos.getY() > 50) {
                    final IBlockState iblockstate2 = worldIn.getBlockState(blockpos);
                    final Block block2 = iblockstate2.getBlock();
                    if (iblockstate2.getMaterial() != Material.AIR && block2 != Blocks.DIRT && block2 != Blocks.SNOW && block2 != Blocks.ICE && block2 != Blocks.PACKED_ICE) {
                        break;
                    }
                    this.setBlockAndNotifyAdequately(worldIn, blockpos, Blocks.PACKED_ICE.getDefaultState());
                    blockpos = blockpos.down();
                    if (--j3 > 0) {
                        continue;
                    }
                    blockpos = blockpos.down(rand.nextInt(5) + 1);
                    j3 = rand.nextInt(5);
                }
            }
        }
        return true;
    }
}
