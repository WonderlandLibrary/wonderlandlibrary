// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class BiomeVoidDecorator extends BiomeDecorator
{
    @Override
    public void decorate(final World worldIn, final Random random, final Biome biome, final BlockPos pos) {
        final BlockPos blockpos = worldIn.getSpawnPoint();
        final int i = 16;
        final double d0 = blockpos.distanceSq(pos.add(8, blockpos.getY(), 8));
        if (d0 <= 1024.0) {
            final BlockPos blockpos2 = new BlockPos(blockpos.getX() - 16, blockpos.getY() - 1, blockpos.getZ() - 16);
            final BlockPos blockpos3 = new BlockPos(blockpos.getX() + 16, blockpos.getY() - 1, blockpos.getZ() + 16);
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(blockpos2);
            for (int j = pos.getZ(); j < pos.getZ() + 16; ++j) {
                for (int k = pos.getX(); k < pos.getX() + 16; ++k) {
                    if (j >= blockpos2.getZ() && j <= blockpos3.getZ() && k >= blockpos2.getX() && k <= blockpos3.getX()) {
                        blockpos$mutableblockpos.setPos(k, blockpos$mutableblockpos.getY(), j);
                        if (blockpos.getX() == k && blockpos.getZ() == j) {
                            worldIn.setBlockState(blockpos$mutableblockpos, Blocks.COBBLESTONE.getDefaultState(), 2);
                        }
                        else {
                            worldIn.setBlockState(blockpos$mutableblockpos, Blocks.STONE.getDefaultState(), 2);
                        }
                    }
                }
            }
        }
    }
}
