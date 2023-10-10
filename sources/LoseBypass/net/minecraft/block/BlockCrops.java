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
    protected boolean canPlaceBlockOn(Block ground) {
        if (ground != Blocks.farmland) return false;
        return true;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.getLightFromNeighbors(pos.up()) < 9) return;
        int i = state.getValue(AGE);
        if (i >= 7) return;
        float f = BlockCrops.getGrowthChance(this, worldIn, pos);
        if (rand.nextInt((int)(25.0f / f) + 1) != 0) return;
        worldIn.setBlockState(pos, state.withProperty(AGE, i + 1), 2);
    }

    public void grow(World worldIn, BlockPos pos, IBlockState state) {
        int i = state.getValue(AGE) + MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
        if (i > 7) {
            i = 7;
        }
        worldIn.setBlockState(pos, state.withProperty(AGE, i), 2);
    }

    protected static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos) {
        boolean flag1;
        float f = 1.0f;
        BlockPos blockpos = pos.down();
        for (int i = -1; i <= 1; ++i) {
            float f1;
            for (int j = -1; j <= 1; f += f1, ++j) {
                f1 = 0.0f;
                IBlockState iblockstate = worldIn.getBlockState(blockpos.add(i, 0, j));
                if (iblockstate.getBlock() == Blocks.farmland) {
                    f1 = 1.0f;
                    if (iblockstate.getValue(BlockFarmland.MOISTURE) > 0) {
                        f1 = 3.0f;
                    }
                }
                if (i == 0 && j == 0) continue;
                f1 /= 4.0f;
            }
        }
        BlockPos blockpos1 = pos.north();
        BlockPos blockpos2 = pos.south();
        BlockPos blockpos3 = pos.west();
        BlockPos blockpos4 = pos.east();
        boolean flag = blockIn == worldIn.getBlockState(blockpos3).getBlock() || blockIn == worldIn.getBlockState(blockpos4).getBlock();
        boolean bl = flag1 = blockIn == worldIn.getBlockState(blockpos1).getBlock() || blockIn == worldIn.getBlockState(blockpos2).getBlock();
        if (flag && flag1) {
            return f /= 2.0f;
        }
        boolean flag2 = blockIn == worldIn.getBlockState(blockpos3.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.south()).getBlock() || blockIn == worldIn.getBlockState(blockpos3.south()).getBlock();
        if (!flag2) return f;
        f /= 2.0f;
        return f;
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        if (worldIn.getLight(pos) < 8) {
            if (!worldIn.canSeeSky(pos)) return false;
        }
        if (!this.canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock())) return false;
        return true;
    }

    protected Item getSeed() {
        return Items.wheat_seeds;
    }

    protected Item getCrop() {
        return Items.wheat;
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
        if (worldIn.isRemote) return;
        int i = state.getValue(AGE);
        if (i < 7) return;
        int j = 3 + fortune;
        int k = 0;
        while (k < j) {
            if (worldIn.rand.nextInt(15) <= i) {
                BlockCrops.spawnAsEntity(worldIn, pos, new ItemStack(this.getSeed(), 1, 0));
            }
            ++k;
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        Item item;
        if (state.getValue(AGE) == 7) {
            item = this.getCrop();
            return item;
        }
        item = this.getSeed();
        return item;
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return this.getSeed();
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        if (state.getValue(AGE) >= 7) return false;
        return true;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        this.grow(worldIn, pos, state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AGE, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AGE);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, AGE);
    }
}

