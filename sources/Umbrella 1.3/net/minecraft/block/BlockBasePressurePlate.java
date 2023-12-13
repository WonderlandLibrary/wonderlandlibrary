/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockBasePressurePlate
extends Block {
    private static final String __OBFID = "CL_00000194";

    protected BlockBasePressurePlate(Material materialIn) {
        super(materialIn);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setTickRandomly(true);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        this.func_180668_d(access.getBlockState(pos));
    }

    protected void func_180668_d(IBlockState p_180668_1_) {
        boolean var2 = this.getRedstoneStrength(p_180668_1_) > 0;
        float var3 = 0.0625f;
        if (var2) {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.03125f, 0.9375f);
        } else {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.0625f, 0.9375f);
        }
    }

    @Override
    public int tickRate(World worldIn) {
        return 20;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return this.canBePlacedOn(worldIn, pos.offsetDown());
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!this.canBePlacedOn(worldIn, pos.offsetDown())) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean canBePlacedOn(World worldIn, BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos) || worldIn.getBlockState(pos).getBlock() instanceof BlockFence;
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int var5;
        if (!worldIn.isRemote && (var5 = this.getRedstoneStrength(state)) > 0) {
            this.updateState(worldIn, pos, state, var5);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        int var5;
        if (!worldIn.isRemote && (var5 = this.getRedstoneStrength(state)) == 0) {
            this.updateState(worldIn, pos, state, var5);
        }
    }

    protected void updateState(World worldIn, BlockPos pos, IBlockState state, int oldRedstoneStrength) {
        boolean var7;
        int var5 = this.computeRedstoneStrength(worldIn, pos);
        boolean var6 = oldRedstoneStrength > 0;
        boolean bl = var7 = var5 > 0;
        if (oldRedstoneStrength != var5) {
            state = this.setRedstoneStrength(state, var5);
            worldIn.setBlockState(pos, state, 2);
            this.updateNeighbors(worldIn, pos);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
        }
        if (!var7 && var6) {
            worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.1, (double)pos.getZ() + 0.5, "random.click", 0.3f, 0.5f);
        } else if (var7 && !var6) {
            worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.1, (double)pos.getZ() + 0.5, "random.click", 0.3f, 0.6f);
        }
        if (var7) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }

    protected AxisAlignedBB getSensitiveAABB(BlockPos pos) {
        float var2 = 0.125f;
        return new AxisAlignedBB((float)pos.getX() + 0.125f, pos.getY(), (float)pos.getZ() + 0.125f, (float)(pos.getX() + 1) - 0.125f, (double)pos.getY() + 0.25, (float)(pos.getZ() + 1) - 0.125f);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (this.getRedstoneStrength(state) > 0) {
            this.updateNeighbors(worldIn, pos);
        }
        super.breakBlock(worldIn, pos, state);
    }

    protected void updateNeighbors(World worldIn, BlockPos pos) {
        worldIn.notifyNeighborsOfStateChange(pos, this);
        worldIn.notifyNeighborsOfStateChange(pos.offsetDown(), this);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return this.getRedstoneStrength(state);
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return side == EnumFacing.UP ? this.getRedstoneStrength(state) : 0;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public void setBlockBoundsForItemRender() {
        float var1 = 0.5f;
        float var2 = 0.125f;
        float var3 = 0.5f;
        this.setBlockBounds(0.0f, 0.375f, 0.0f, 1.0f, 0.625f, 1.0f);
    }

    @Override
    public int getMobilityFlag() {
        return 1;
    }

    protected abstract int computeRedstoneStrength(World var1, BlockPos var2);

    protected abstract int getRedstoneStrength(IBlockState var1);

    protected abstract IBlockState setRedstoneStrength(IBlockState var1, int var2);
}

