/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenBlockBlob
extends WorldGenerator {
    private final Block field_150545_a;
    private final int field_150544_b;

    public WorldGenBlockBlob(Block block, int n) {
        super(false);
        this.field_150545_a = block;
        this.field_150544_b = n;
    }

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        while (true) {
            Block block;
            if (blockPos.getY() <= 3 || !world.isAirBlock(blockPos.down()) && ((block = world.getBlockState(blockPos.down()).getBlock()) == Blocks.grass || block == Blocks.dirt || block == Blocks.stone)) {
                if (blockPos.getY() <= 3) {
                    return false;
                }
                int n = this.field_150544_b;
                int n2 = 0;
                while (n >= 0 && n2 < 3) {
                    int n3 = n + random.nextInt(2);
                    int n4 = n + random.nextInt(2);
                    int n5 = n + random.nextInt(2);
                    float f = (float)(n3 + n4 + n5) * 0.333f + 0.5f;
                    for (BlockPos blockPos2 : BlockPos.getAllInBox(blockPos.add(-n3, -n4, -n5), blockPos.add(n3, n4, n5))) {
                        if (!(blockPos2.distanceSq(blockPos) <= (double)(f * f))) continue;
                        world.setBlockState(blockPos2, this.field_150545_a.getDefaultState(), 4);
                    }
                    blockPos = blockPos.add(-(n + 1) + random.nextInt(2 + n * 2), 0 - random.nextInt(2), -(n + 1) + random.nextInt(2 + n * 2));
                    ++n2;
                }
                return true;
            }
            blockPos = blockPos.down();
        }
    }
}

