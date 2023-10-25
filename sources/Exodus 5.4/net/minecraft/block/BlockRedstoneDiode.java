/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRedstoneDiode
extends BlockDirectional {
    protected final boolean isRepeaterPowered;

    protected boolean canPowerSide(Block block) {
        return block.canProvidePower();
    }

    public boolean isFacingTowardsRepeater(World world, BlockPos blockPos, IBlockState iBlockState) {
        EnumFacing enumFacing = iBlockState.getValue(FACING).getOpposite();
        BlockPos blockPos2 = blockPos.offset(enumFacing);
        return BlockRedstoneDiode.isRedstoneRepeaterBlockID(world.getBlockState(blockPos2).getBlock()) ? world.getBlockState(blockPos2).getValue(FACING) != enumFacing : false;
    }

    protected int getPowerOnSides(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState) {
        EnumFacing enumFacing = iBlockState.getValue(FACING);
        EnumFacing enumFacing2 = enumFacing.rotateY();
        EnumFacing enumFacing3 = enumFacing.rotateYCCW();
        return Math.max(this.getPowerOnSide(iBlockAccess, blockPos.offset(enumFacing2), enumFacing2), this.getPowerOnSide(iBlockAccess, blockPos.offset(enumFacing3), enumFacing3));
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (this.isRepeaterPowered) {
            EnumFacing[] enumFacingArray = EnumFacing.values();
            int n = enumFacingArray.length;
            int n2 = 0;
            while (n2 < n) {
                EnumFacing enumFacing = enumFacingArray[n2];
                world.notifyNeighborsOfStateChange(blockPos.offset(enumFacing), this);
                ++n2;
            }
        }
        super.onBlockDestroyedByPlayer(world, blockPos, iBlockState);
    }

    protected int calculateInputStrength(World world, BlockPos blockPos, IBlockState iBlockState) {
        EnumFacing enumFacing = iBlockState.getValue(FACING);
        BlockPos blockPos2 = blockPos.offset(enumFacing);
        int n = world.getRedstonePower(blockPos2, enumFacing);
        if (n >= 15) {
            return n;
        }
        IBlockState iBlockState2 = world.getBlockState(blockPos2);
        return Math.max(n, iBlockState2.getBlock() == Blocks.redstone_wire ? iBlockState2.getValue(BlockRedstoneWire.POWER) : 0);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState iBlockState, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        if (this.shouldBePowered(world, blockPos, iBlockState)) {
            world.scheduleUpdate(blockPos, this, 1);
        }
    }

    public boolean isAssociated(Block block) {
        return block == this.getPoweredState(this.getDefaultState()).getBlock() || block == this.getUnpoweredState(this.getDefaultState()).getBlock();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    public boolean canBlockStay(World world, BlockPos blockPos) {
        return World.doesBlockHaveSolidTopSurface(world, blockPos.down());
    }

    protected abstract IBlockState getPoweredState(IBlockState var1);

    protected abstract int getDelay(IBlockState var1);

    @Override
    public void randomTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
    }

    @Override
    public boolean isAssociatedBlock(Block block) {
        return this.isAssociated(block);
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        if (this.canBlockStay(world, blockPos)) {
            this.updateState(world, blockPos, iBlockState);
        } else {
            this.dropBlockAsItem(world, blockPos, iBlockState, 0);
            world.setBlockToAir(blockPos);
            EnumFacing[] enumFacingArray = EnumFacing.values();
            int n = enumFacingArray.length;
            int n2 = 0;
            while (n2 < n) {
                EnumFacing enumFacing = enumFacingArray[n2];
                world.notifyNeighborsOfStateChange(blockPos.offset(enumFacing), this);
                ++n2;
            }
        }
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        return this.getDefaultState().withProperty(FACING, entityLivingBase.getHorizontalFacing().getOpposite());
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (!this.isLocked(world, blockPos, iBlockState)) {
            boolean bl = this.shouldBePowered(world, blockPos, iBlockState);
            if (this.isRepeaterPowered && !bl) {
                world.setBlockState(blockPos, this.getUnpoweredState(iBlockState), 2);
            } else if (!this.isRepeaterPowered) {
                world.setBlockState(blockPos, this.getPoweredState(iBlockState), 2);
                if (!bl) {
                    world.updateBlockTick(blockPos, this.getPoweredState(iBlockState).getBlock(), this.getTickDelay(iBlockState), -1);
                }
            }
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        this.notifyNeighbors(world, blockPos, iBlockState);
    }

    protected void updateState(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (!this.isLocked(world, blockPos, iBlockState)) {
            boolean bl = this.shouldBePowered(world, blockPos, iBlockState);
            if ((this.isRepeaterPowered && !bl || !this.isRepeaterPowered && bl) && !world.isBlockTickPending(blockPos, this)) {
                int n = -1;
                if (this.isFacingTowardsRepeater(world, blockPos, iBlockState)) {
                    n = -3;
                } else if (this.isRepeaterPowered) {
                    n = -2;
                }
                world.updateBlockTick(blockPos, this, this.getDelay(iBlockState), n);
            }
        }
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    protected int getActiveSignal(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState) {
        return 15;
    }

    protected abstract IBlockState getUnpoweredState(IBlockState var1);

    public static boolean isRedstoneRepeaterBlockID(Block block) {
        return Blocks.unpowered_repeater.isAssociated(block) || Blocks.unpowered_comparator.isAssociated(block);
    }

    public boolean isLocked(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState) {
        return false;
    }

    protected int getPowerOnSide(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        IBlockState iBlockState = iBlockAccess.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        return this.canPowerSide(block) ? (block == Blocks.redstone_wire ? iBlockState.getValue(BlockRedstoneWire.POWER).intValue() : iBlockAccess.getStrongPower(blockPos, enumFacing)) : 0;
    }

    protected boolean isPowered(IBlockState iBlockState) {
        return this.isRepeaterPowered;
    }

    protected BlockRedstoneDiode(boolean bl) {
        super(Material.circuits);
        this.isRepeaterPowered = bl;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        return enumFacing.getAxis() != EnumFacing.Axis.Y;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        return World.doesBlockHaveSolidTopSurface(world, blockPos.down()) ? super.canPlaceBlockAt(world, blockPos) : false;
    }

    @Override
    public int getStrongPower(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState, EnumFacing enumFacing) {
        return this.getWeakPower(iBlockAccess, blockPos, iBlockState, enumFacing);
    }

    protected boolean shouldBePowered(World world, BlockPos blockPos, IBlockState iBlockState) {
        return this.calculateInputStrength(world, blockPos, iBlockState) > 0;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int getWeakPower(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState, EnumFacing enumFacing) {
        return !this.isPowered(iBlockState) ? 0 : (iBlockState.getValue(FACING) == enumFacing ? this.getActiveSignal(iBlockAccess, blockPos, iBlockState) : 0);
    }

    protected int getTickDelay(IBlockState iBlockState) {
        return this.getDelay(iBlockState);
    }

    protected void notifyNeighbors(World world, BlockPos blockPos, IBlockState iBlockState) {
        EnumFacing enumFacing = iBlockState.getValue(FACING);
        BlockPos blockPos2 = blockPos.offset(enumFacing.getOpposite());
        world.notifyBlockOfStateChange(blockPos2, this);
        world.notifyNeighborsOfStateExcept(blockPos2, this, enumFacing);
    }
}

