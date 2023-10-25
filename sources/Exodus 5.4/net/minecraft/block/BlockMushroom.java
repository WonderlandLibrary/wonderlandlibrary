/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockMushroom
extends BlockBush
implements IGrowable {
    @Override
    protected boolean canPlaceBlockOn(Block block) {
        return block.isFullBlock();
    }

    protected BlockMushroom() {
        float f = 0.2f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, f * 2.0f, 0.5f + f);
        this.setTickRandomly(true);
    }

    @Override
    public void grow(World world, Random random, BlockPos blockPos, IBlockState iBlockState) {
        this.generateBigMushroom(world, blockPos, iBlockState, random);
    }

    public boolean generateBigMushroom(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        world.setBlockToAir(blockPos);
        WorldGenBigMushroom worldGenBigMushroom = null;
        if (this == Blocks.brown_mushroom) {
            worldGenBigMushroom = new WorldGenBigMushroom(Blocks.brown_mushroom_block);
        } else if (this == Blocks.red_mushroom) {
            worldGenBigMushroom = new WorldGenBigMushroom(Blocks.red_mushroom_block);
        }
        if (worldGenBigMushroom != null && ((WorldGenerator)worldGenBigMushroom).generate(world, random, blockPos)) {
            return true;
        }
        world.setBlockState(blockPos, iBlockState, 3);
        return false;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (random.nextInt(25) == 0) {
            void var7_11;
            int n = 5;
            int n2 = 4;
            for (BlockPos blockPos22 : BlockPos.getAllInBoxMutable(blockPos.add(-4, -1, -4), blockPos.add(4, 1, 4))) {
                if (world.getBlockState(blockPos22).getBlock() != this || --n > 0) continue;
                return;
            }
            BlockPos blockPos2 = blockPos.add(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            int n3 = 0;
            while (n3 < 4) {
                if (world.isAirBlock((BlockPos)var7_11) && this.canBlockStay(world, (BlockPos)var7_11, this.getDefaultState())) {
                    blockPos = var7_11;
                }
                BlockPos blockPos3 = blockPos.add(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
                ++n3;
            }
            if (world.isAirBlock((BlockPos)var7_11) && this.canBlockStay(world, (BlockPos)var7_11, this.getDefaultState())) {
                world.setBlockState((BlockPos)var7_11, this.getDefaultState(), 2);
            }
        }
    }

    @Override
    public boolean canGrow(World world, BlockPos blockPos, IBlockState iBlockState, boolean bl) {
        return true;
    }

    @Override
    public boolean canBlockStay(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (blockPos.getY() >= 0 && blockPos.getY() < 256) {
            IBlockState iBlockState2 = world.getBlockState(blockPos.down());
            return iBlockState2.getBlock() == Blocks.mycelium ? true : (iBlockState2.getBlock() == Blocks.dirt && iBlockState2.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL ? true : world.getLight(blockPos) < 13 && this.canPlaceBlockOn(iBlockState2.getBlock()));
        }
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        return super.canPlaceBlockAt(world, blockPos) && this.canBlockStay(world, blockPos, this.getDefaultState());
    }

    @Override
    public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, IBlockState iBlockState) {
        return (double)random.nextFloat() < 0.4;
    }
}

