/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.block.BlockBush;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class GeneratorBushFeature
extends WorldGenerator {
    private BlockBush field_175908_a;

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        int n = 0;
        while (n < 64) {
            BlockPos blockPos2 = blockPos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
            if (world.isAirBlock(blockPos2) && (!world.provider.getHasNoSky() || blockPos2.getY() < 255) && this.field_175908_a.canBlockStay(world, blockPos2, this.field_175908_a.getDefaultState())) {
                world.setBlockState(blockPos2, this.field_175908_a.getDefaultState(), 2);
            }
            ++n;
        }
        return true;
    }

    public GeneratorBushFeature(BlockBush blockBush) {
        this.field_175908_a = blockBush;
    }
}

