/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenFlowers
extends WorldGenerator {
    private BlockFlower flower;
    private IBlockState field_175915_b;

    public WorldGenFlowers(BlockFlower blockFlower, BlockFlower.EnumFlowerType enumFlowerType) {
        this.setGeneratedBlock(blockFlower, enumFlowerType);
    }

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        int n = 0;
        while (n < 64) {
            BlockPos blockPos2 = blockPos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
            if (world.isAirBlock(blockPos2) && (!world.provider.getHasNoSky() || blockPos2.getY() < 255) && this.flower.canBlockStay(world, blockPos2, this.field_175915_b)) {
                world.setBlockState(blockPos2, this.field_175915_b, 2);
            }
            ++n;
        }
        return true;
    }

    public void setGeneratedBlock(BlockFlower blockFlower, BlockFlower.EnumFlowerType enumFlowerType) {
        this.flower = blockFlower;
        this.field_175915_b = blockFlower.getDefaultState().withProperty(blockFlower.getTypeProperty(), enumFlowerType);
    }
}

