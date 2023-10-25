/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockObserver
extends BlockDirectional {
    public static final PropertyBool field_190963_a = PropertyBool.create("powered");

    public BlockObserver() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH).withProperty(field_190963_a, false));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, FACING, field_190963_a);
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (state.getValue(field_190963_a).booleanValue()) {
            worldIn.setBlockState(pos, state.withProperty(field_190963_a, false), 2);
        } else {
            worldIn.setBlockState(pos, state.withProperty(field_190963_a, true), 2);
            worldIn.scheduleUpdate(pos, this, 2);
        }
        this.func_190961_e(worldIn, pos, state);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    }

    public void func_190962_b(IBlockState p_190962_1_, World p_190962_2_, BlockPos p_190962_3_, Block p_190962_4_, BlockPos p_190962_5_) {
        if (!p_190962_2_.isRemote && p_190962_3_.offset(p_190962_1_.getValue(FACING)).equals(p_190962_5_)) {
            this.func_190960_d(p_190962_1_, p_190962_2_, p_190962_3_);
        }
    }

    private void func_190960_d(IBlockState p_190960_1_, World p_190960_2_, BlockPos p_190960_3_) {
        if (!p_190960_1_.getValue(field_190963_a).booleanValue() && !p_190960_2_.isUpdateScheduled(p_190960_3_, this)) {
            p_190960_2_.scheduleUpdate(p_190960_3_, this, 2);
        }
    }

    protected void func_190961_e(World p_190961_1_, BlockPos p_190961_2_, IBlockState p_190961_3_) {
        EnumFacing enumfacing = p_190961_3_.getValue(FACING);
        BlockPos blockpos = p_190961_2_.offset(enumfacing.getOpposite());
        p_190961_1_.func_190524_a(blockpos, this, p_190961_2_);
        p_190961_1_.notifyNeighborsOfStateExcept(blockpos, this, enumfacing);
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.getWeakPower(blockAccess, pos, side);
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.getValue(field_190963_a) != false && blockState.getValue(FACING) == side ? 15 : 0;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            if (state.getValue(field_190963_a).booleanValue()) {
                this.updateTick(worldIn, pos, state, worldIn.rand);
            }
            this.func_190960_d(state, worldIn, pos);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getValue(field_190963_a).booleanValue() && worldIn.isUpdateScheduled(pos, this)) {
            this.func_190961_e(worldIn, pos, state.withProperty(field_190963_a, false));
        }
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.func_190914_a(pos, placer).getOpposite());
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= state.getValue(FACING).getIndex();
        if (state.getValue(field_190963_a).booleanValue()) {
            i |= 8;
        }
        return i;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
    }
}

