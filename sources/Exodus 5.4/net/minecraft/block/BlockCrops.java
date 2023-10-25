/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockCrops
extends BlockBush
implements IGrowable {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        float f;
        int n;
        super.updateTick(world, blockPos, iBlockState, random);
        if (world.getLightFromNeighbors(blockPos.up()) >= 9 && (n = iBlockState.getValue(AGE).intValue()) < 7 && random.nextInt((int)(25.0f / (f = BlockCrops.getGrowthChance(this, world, blockPos))) + 1) == 0) {
            world.setBlockState(blockPos, iBlockState.withProperty(AGE, n + 1), 2);
        }
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return iBlockState.getValue(AGE) == 7 ? this.getCrop() : this.getSeed();
    }

    protected BlockCrops() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
        this.setTickRandomly(true);
        float f = 0.5f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.25f, 0.5f + f);
        this.setCreativeTab(null);
        this.setHardness(0.0f);
        this.setStepSound(soundTypeGrass);
        this.disableStats();
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(AGE);
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return this.getSeed();
    }

    @Override
    public boolean canGrow(World world, BlockPos blockPos, IBlockState iBlockState, boolean bl) {
        return iBlockState.getValue(AGE) < 7;
    }

    @Override
    protected boolean canPlaceBlockOn(Block block) {
        return block == Blocks.farmland;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, AGE);
    }

    @Override
    public void grow(World world, Random random, BlockPos blockPos, IBlockState iBlockState) {
        this.grow(world, blockPos, iBlockState);
    }

    @Override
    public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, IBlockState iBlockState) {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(AGE, n);
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState iBlockState, float f, int n) {
        int n2;
        super.dropBlockAsItemWithChance(world, blockPos, iBlockState, f, 0);
        if (!world.isRemote && (n2 = iBlockState.getValue(AGE).intValue()) >= 7) {
            int n3 = 3 + n;
            int n4 = 0;
            while (n4 < n3) {
                if (world.rand.nextInt(15) <= n2) {
                    BlockCrops.spawnAsEntity(world, blockPos, new ItemStack(this.getSeed(), 1, 0));
                }
                ++n4;
            }
        }
    }

    public void grow(World world, BlockPos blockPos, IBlockState iBlockState) {
        int n = iBlockState.getValue(AGE) + MathHelper.getRandomIntegerInRange(world.rand, 2, 5);
        if (n > 7) {
            n = 7;
        }
        world.setBlockState(blockPos, iBlockState.withProperty(AGE, n), 2);
    }

    protected Item getCrop() {
        return Items.wheat;
    }

    protected static float getGrowthChance(Block block, World world, BlockPos blockPos) {
        boolean bl;
        Object object;
        float f = 1.0f;
        BlockPos blockPos2 = blockPos.down();
        int n = -1;
        while (n <= 1) {
            int n2 = -1;
            while (n2 <= 1) {
                float f2 = 0.0f;
                object = world.getBlockState(blockPos2.add(n, 0, n2));
                if (object.getBlock() == Blocks.farmland) {
                    f2 = 1.0f;
                    if (object.getValue(BlockFarmland.MOISTURE) > 0) {
                        f2 = 3.0f;
                    }
                }
                if (n != 0 || n2 != 0) {
                    f2 /= 4.0f;
                }
                f += f2;
                ++n2;
            }
            ++n;
        }
        BlockPos blockPos3 = blockPos.north();
        BlockPos blockPos4 = blockPos.south();
        BlockPos blockPos5 = blockPos.west();
        object = blockPos.east();
        boolean bl2 = block == world.getBlockState(blockPos5).getBlock() || block == world.getBlockState((BlockPos)object).getBlock();
        boolean bl3 = bl = block == world.getBlockState(blockPos3).getBlock() || block == world.getBlockState(blockPos4).getBlock();
        if (bl2 && bl) {
            f /= 2.0f;
        } else {
            boolean bl4;
            boolean bl5 = bl4 = block == world.getBlockState(blockPos5.north()).getBlock() || block == world.getBlockState(((BlockPos)object).north()).getBlock() || block == world.getBlockState(((BlockPos)object).south()).getBlock() || block == world.getBlockState(blockPos5.south()).getBlock();
            if (bl4) {
                f /= 2.0f;
            }
        }
        return f;
    }

    @Override
    public boolean canBlockStay(World world, BlockPos blockPos, IBlockState iBlockState) {
        return (world.getLight(blockPos) >= 8 || world.canSeeSky(blockPos)) && this.canPlaceBlockOn(world.getBlockState(blockPos.down()).getBlock());
    }

    protected Item getSeed() {
        return Items.wheat_seeds;
    }
}

