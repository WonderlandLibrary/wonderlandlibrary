// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;

public class WorldGenIcePath extends WorldGenerator
{
    private final Block block;
    private final int basePathWidth;
    
    public WorldGenIcePath(final int basePathWidthIn) {
        this.block = Blocks.PACKED_ICE;
        this.basePathWidth = basePathWidthIn;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, BlockPos position) {
        while (worldIn.isAirBlock(position) && position.getY() > 2) {
            position = position.down();
        }
        if (worldIn.getBlockState(position).getBlock() != Blocks.SNOW) {
            return false;
        }
        final int i = rand.nextInt(this.basePathWidth - 2) + 2;
        final int j = 1;
        for (int k = position.getX() - i; k <= position.getX() + i; ++k) {
            for (int l = position.getZ() - i; l <= position.getZ() + i; ++l) {
                final int i2 = k - position.getX();
                final int j2 = l - position.getZ();
                if (i2 * i2 + j2 * j2 <= i * i) {
                    for (int k2 = position.getY() - 1; k2 <= position.getY() + 1; ++k2) {
                        final BlockPos blockpos = new BlockPos(k, k2, l);
                        final Block block = worldIn.getBlockState(blockpos).getBlock();
                        if (block == Blocks.DIRT || block == Blocks.SNOW || block == Blocks.ICE) {
                            worldIn.setBlockState(blockpos, this.block.getDefaultState(), 2);
                        }
                    }
                }
            }
        }
        return true;
    }
}
