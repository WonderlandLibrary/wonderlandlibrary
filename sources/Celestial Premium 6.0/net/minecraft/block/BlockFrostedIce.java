/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockFrostedIce
extends BlockIce {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);

    public BlockFrostedIce() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AGE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AGE, MathHelper.clamp(meta, 0, 3));
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if ((rand.nextInt(3) == 0 || this.countNeighbors(worldIn, pos) < 4) && worldIn.getLightFromNeighbors(pos) > 11 - state.getValue(AGE) - state.getLightOpacity()) {
            this.slightlyMelt(worldIn, pos, state, rand, true);
        } else {
            worldIn.scheduleUpdate(pos, this, MathHelper.getInt(rand, 20, 40));
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        int i;
        if (blockIn == this && (i = this.countNeighbors(worldIn, pos)) < 2) {
            this.turnIntoWater(worldIn, pos);
        }
    }

    private int countNeighbors(World p_185680_1_, BlockPos p_185680_2_) {
        int i = 0;
        for (EnumFacing enumfacing : EnumFacing.values()) {
            if (p_185680_1_.getBlockState(p_185680_2_.offset(enumfacing)).getBlock() != this || ++i < 4) continue;
            return i;
        }
        return i;
    }

    protected void slightlyMelt(World p_185681_1_, BlockPos p_185681_2_, IBlockState p_185681_3_, Random p_185681_4_, boolean p_185681_5_) {
        int i = p_185681_3_.getValue(AGE);
        if (i < 3) {
            p_185681_1_.setBlockState(p_185681_2_, p_185681_3_.withProperty(AGE, i + 1), 2);
            p_185681_1_.scheduleUpdate(p_185681_2_, this, MathHelper.getInt(p_185681_4_, 20, 40));
        } else {
            this.turnIntoWater(p_185681_1_, p_185681_2_);
            if (p_185681_5_) {
                for (EnumFacing enumfacing : EnumFacing.values()) {
                    BlockPos blockpos = p_185681_2_.offset(enumfacing);
                    IBlockState iblockstate = p_185681_1_.getBlockState(blockpos);
                    if (iblockstate.getBlock() != this) continue;
                    this.slightlyMelt(p_185681_1_, blockpos, iblockstate, p_185681_4_, false);
                }
            }
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, AGE);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return ItemStack.field_190927_a;
    }
}

