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
        float f2 = 0.5f;
        this.setBlockBounds(0.5f - f2, 0.0f, 0.5f - f2, 0.5f + f2, 0.25f, 0.5f + f2);
        this.setCreativeTab(null);
        this.setHardness(0.0f);
        this.setStepSound(soundTypeGrass);
        this.disableStats();
    }

    @Override
    protected boolean canPlaceBlockOn(Block ground) {
        return ground == Blocks.farmland;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        float f2;
        int i2;
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && (i2 = state.getValue(AGE).intValue()) < 7 && rand.nextInt((int)(25.0f / (f2 = BlockCrops.getGrowthChance(this, worldIn, pos))) + 1) == 0) {
            worldIn.setBlockState(pos, state.withProperty(AGE, i2 + 1), 2);
        }
    }

    public void grow(World worldIn, BlockPos pos, IBlockState state) {
        int i2 = state.getValue(AGE) + MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
        if (i2 > 7) {
            i2 = 7;
        }
        worldIn.setBlockState(pos, state.withProperty(AGE, i2), 2);
    }

    protected static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos) {
        boolean flag1;
        float f2 = 1.0f;
        BlockPos blockpos = pos.down();
        for (int i2 = -1; i2 <= 1; ++i2) {
            for (int j2 = -1; j2 <= 1; ++j2) {
                float f1 = 0.0f;
                IBlockState iblockstate = worldIn.getBlockState(blockpos.add(i2, 0, j2));
                if (iblockstate.getBlock() == Blocks.farmland) {
                    f1 = 1.0f;
                    if (iblockstate.getValue(BlockFarmland.MOISTURE) > 0) {
                        f1 = 3.0f;
                    }
                }
                if (i2 != 0 || j2 != 0) {
                    f1 /= 4.0f;
                }
                f2 += f1;
            }
        }
        BlockPos blockpos1 = pos.north();
        BlockPos blockpos2 = pos.south();
        BlockPos blockpos3 = pos.west();
        BlockPos blockpos4 = pos.east();
        boolean flag = blockIn == worldIn.getBlockState(blockpos3).getBlock() || blockIn == worldIn.getBlockState(blockpos4).getBlock();
        boolean bl = flag1 = blockIn == worldIn.getBlockState(blockpos1).getBlock() || blockIn == worldIn.getBlockState(blockpos2).getBlock();
        if (flag && flag1) {
            f2 /= 2.0f;
        } else {
            boolean flag2;
            boolean bl2 = flag2 = blockIn == worldIn.getBlockState(blockpos3.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.south()).getBlock() || blockIn == worldIn.getBlockState(blockpos3.south()).getBlock();
            if (flag2) {
                f2 /= 2.0f;
            }
        }
        return f2;
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        return (worldIn.getLight(pos) >= 8 || worldIn.canSeeSky(pos)) && this.canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
    }

    protected Item getSeed() {
        return Items.wheat_seeds;
    }

    protected Item getCrop() {
        return Items.wheat;
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        int i2;
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
        if (!worldIn.isRemote && (i2 = state.getValue(AGE).intValue()) >= 7) {
            int j2 = 3 + fortune;
            for (int k2 = 0; k2 < j2; ++k2) {
                if (worldIn.rand.nextInt(15) > i2) continue;
                BlockCrops.spawnAsEntity(worldIn, pos, new ItemStack(this.getSeed(), 1, 0));
            }
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(AGE) == 7 ? this.getCrop() : this.getSeed();
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return this.getSeed();
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return state.getValue(AGE) < 7;
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

