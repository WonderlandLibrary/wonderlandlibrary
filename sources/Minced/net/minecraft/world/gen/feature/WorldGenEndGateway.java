// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenEndGateway extends WorldGenerator
{
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        for (final BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(position.add(-1, -2, -1), position.add(1, 2, 1))) {
            final boolean flag = blockpos$mutableblockpos.getX() == position.getX();
            final boolean flag2 = blockpos$mutableblockpos.getY() == position.getY();
            final boolean flag3 = blockpos$mutableblockpos.getZ() == position.getZ();
            final boolean flag4 = Math.abs(blockpos$mutableblockpos.getY() - position.getY()) == 2;
            if (flag && flag2 && flag3) {
                this.setBlockAndNotifyAdequately(worldIn, new BlockPos(blockpos$mutableblockpos), Blocks.END_GATEWAY.getDefaultState());
            }
            else if (flag2) {
                this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
            }
            else if (flag4 && flag && flag3) {
                this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
            }
            else if ((flag || flag3) && !flag4) {
                this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
            }
            else {
                this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
            }
        }
        return true;
    }
}
