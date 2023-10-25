/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenEndGateway
extends WorldGenerator {
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(position.add(-1, -2, -1), position.add(1, 2, 1))) {
            boolean flag3;
            boolean flag = blockpos$mutableblockpos.getX() == position.getX();
            boolean flag1 = blockpos$mutableblockpos.getY() == position.getY();
            boolean flag2 = blockpos$mutableblockpos.getZ() == position.getZ();
            boolean bl = flag3 = Math.abs(blockpos$mutableblockpos.getY() - position.getY()) == 2;
            if (flag && flag1 && flag2) {
                this.setBlockAndNotifyAdequately(worldIn, new BlockPos(blockpos$mutableblockpos), Blocks.END_GATEWAY.getDefaultState());
                continue;
            }
            if (flag1) {
                this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
                continue;
            }
            if (flag3 && flag && flag2) {
                this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
                continue;
            }
            if ((flag || flag2) && !flag3) {
                this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
                continue;
            }
            this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
        }
        return true;
    }
}

