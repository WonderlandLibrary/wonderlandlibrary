/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.MapColor;
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
    @Override
    public void setBlockBoundsForItemRender() {
        float f = 0.5f;
        float f2 = 0.125f;
        float f3 = 0.5f;
        this.setBlockBounds(0.0f, 0.375f, 0.0f, 1.0f, 0.625f, 1.0f);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        this.setBlockBoundsBasedOnState0(iBlockAccess.getBlockState(blockPos));
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (this.getRedstoneStrength(iBlockState) > 0) {
            this.updateNeighbors(world, blockPos);
        }
        super.breakBlock(world, blockPos, iBlockState);
    }

    private boolean canBePlacedOn(World world, BlockPos blockPos) {
        return World.doesBlockHaveSolidTopSurface(world, blockPos) || world.getBlockState(blockPos).getBlock() instanceof BlockFence;
    }

    protected BlockBasePressurePlate(Material material, MapColor mapColor) {
        super(material, mapColor);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setTickRandomly(true);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        return this.canBePlacedOn(world, blockPos.down());
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState iBlockState) {
        return null;
    }

    protected BlockBasePressurePlate(Material material) {
        this(material, material.getMaterialMapColor());
    }

    @Override
    public boolean isPassable(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return true;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, IBlockState iBlockState, Entity entity) {
        int n;
        if (!world.isRemote && (n = this.getRedstoneStrength(iBlockState)) == 0) {
            this.updateState(world, blockPos, iBlockState, n);
        }
    }

    protected abstract IBlockState setRedstoneStrength(IBlockState var1, int var2);

    protected abstract int getRedstoneStrength(IBlockState var1);

    @Override
    public int tickRate(World world) {
        return 20;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public int getStrongPower(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState, EnumFacing enumFacing) {
        return enumFacing == EnumFacing.UP ? this.getRedstoneStrength(iBlockState) : 0;
    }

    protected AxisAlignedBB getSensitiveAABB(BlockPos blockPos) {
        float f = 0.125f;
        return new AxisAlignedBB((float)blockPos.getX() + 0.125f, blockPos.getY(), (float)blockPos.getZ() + 0.125f, (float)(blockPos.getX() + 1) - 0.125f, (double)blockPos.getY() + 0.25, (float)(blockPos.getZ() + 1) - 0.125f);
    }

    @Override
    public void randomTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
    }

    protected void updateNeighbors(World world, BlockPos blockPos) {
        world.notifyNeighborsOfStateChange(blockPos, this);
        world.notifyNeighborsOfStateChange(blockPos.down(), this);
    }

    protected abstract int computeRedstoneStrength(World var1, BlockPos var2);

    @Override
    public int getMobilityFlag() {
        return 1;
    }

    @Override
    public int getWeakPower(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState, EnumFacing enumFacing) {
        return this.getRedstoneStrength(iBlockState);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    protected void updateState(World world, BlockPos blockPos, IBlockState iBlockState, int n) {
        boolean bl;
        int n2 = this.computeRedstoneStrength(world, blockPos);
        boolean bl2 = n > 0;
        boolean bl3 = bl = n2 > 0;
        if (n != n2) {
            iBlockState = this.setRedstoneStrength(iBlockState, n2);
            world.setBlockState(blockPos, iBlockState, 2);
            this.updateNeighbors(world, blockPos);
            world.markBlockRangeForRenderUpdate(blockPos, blockPos);
        }
        if (!bl && bl2) {
            world.playSoundEffect((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.1, (double)blockPos.getZ() + 0.5, "random.click", 0.3f, 0.5f);
        } else if (bl && !bl2) {
            world.playSoundEffect((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.1, (double)blockPos.getZ() + 0.5, "random.click", 0.3f, 0.6f);
        }
        if (bl) {
            world.scheduleUpdate(blockPos, this, this.tickRate(world));
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        if (!this.canBePlacedOn(world, blockPos.down())) {
            this.dropBlockAsItem(world, blockPos, iBlockState, 0);
            world.setBlockToAir(blockPos);
        }
    }

    @Override
    public boolean func_181623_g() {
        return true;
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        int n;
        if (!world.isRemote && (n = this.getRedstoneStrength(iBlockState)) > 0) {
            this.updateState(world, blockPos, iBlockState, n);
        }
    }

    protected void setBlockBoundsBasedOnState0(IBlockState iBlockState) {
        boolean bl = this.getRedstoneStrength(iBlockState) > 0;
        float f = 0.0625f;
        if (bl) {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.03125f, 0.9375f);
        } else {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.0625f, 0.9375f);
        }
    }
}

