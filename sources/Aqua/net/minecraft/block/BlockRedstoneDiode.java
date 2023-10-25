package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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

    protected BlockRedstoneDiode(boolean powered) {
        super(Material.circuits);
        this.isRepeaterPowered = powered;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.down()) ? super.canPlaceBlockAt(worldIn, pos) : false;
    }

    public boolean canBlockStay(World worldIn, BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.down());
    }

    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!this.isLocked((IBlockAccess)worldIn, pos, state)) {
            boolean flag = this.shouldBePowered(worldIn, pos, state);
            if (this.isRepeaterPowered && !flag) {
                worldIn.setBlockState(pos, this.getUnpoweredState(state), 2);
            } else if (!this.isRepeaterPowered) {
                worldIn.setBlockState(pos, this.getPoweredState(state), 2);
                if (!flag) {
                    worldIn.updateBlockTick(pos, this.getPoweredState(state).getBlock(), this.getTickDelay(state), -1);
                }
            }
        }
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return side.getAxis() != EnumFacing.Axis.Y;
    }

    protected boolean isPowered(IBlockState state) {
        return this.isRepeaterPowered;
    }

    public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return this.getWeakPower(worldIn, pos, state, side);
    }

    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return !this.isPowered(state) ? 0 : (state.getValue((IProperty)FACING) == side ? this.getActiveSignal(worldIn, pos, state) : 0);
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (this.canBlockStay(worldIn, pos)) {
            this.updateState(worldIn, pos, state);
        } else {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            for (EnumFacing enumfacing : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), (Block)this);
            }
        }
    }

    protected void updateState(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.isLocked((IBlockAccess)worldIn, pos, state)) {
            boolean flag = this.shouldBePowered(worldIn, pos, state);
            if ((this.isRepeaterPowered && !flag || !this.isRepeaterPowered && flag) && !worldIn.isBlockTickPending(pos, (Block)this)) {
                int i = -1;
                if (this.isFacingTowardsRepeater(worldIn, pos, state)) {
                    i = -3;
                } else if (this.isRepeaterPowered) {
                    i = -2;
                }
                worldIn.updateBlockTick(pos, (Block)this, this.getDelay(state), i);
            }
        }
    }

    public boolean isLocked(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
        return false;
    }

    protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state) {
        return this.calculateInputStrength(worldIn, pos, state) > 0;
    }

    protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
        BlockPos blockpos = pos.offset(enumfacing);
        int i = worldIn.getRedstonePower(blockpos, enumfacing);
        if (i >= 15) {
            return i;
        }
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        return Math.max((int)i, (int)(iblockstate.getBlock() == Blocks.redstone_wire ? (Integer)iblockstate.getValue((IProperty)BlockRedstoneWire.POWER) : 0));
    }

    protected int getPowerOnSides(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
        EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
        EnumFacing enumfacing1 = enumfacing.rotateY();
        EnumFacing enumfacing2 = enumfacing.rotateYCCW();
        return Math.max((int)this.getPowerOnSide(worldIn, pos.offset(enumfacing1), enumfacing1), (int)this.getPowerOnSide(worldIn, pos.offset(enumfacing2), enumfacing2));
    }

    protected int getPowerOnSide(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        return this.canPowerSide(block) ? (block == Blocks.redstone_wire ? ((Integer)iblockstate.getValue((IProperty)BlockRedstoneWire.POWER)).intValue() : worldIn.getStrongPower(pos, side)) : 0;
    }

    public boolean canProvidePower() {
        return true;
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing().getOpposite());
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (this.shouldBePowered(worldIn, pos, state)) {
            worldIn.scheduleUpdate(pos, (Block)this, 1);
        }
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.notifyNeighbors(worldIn, pos, state);
    }

    protected void notifyNeighbors(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
        BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        worldIn.notifyBlockOfStateChange(blockpos, (Block)this);
        worldIn.notifyNeighborsOfStateExcept(blockpos, (Block)this, enumfacing);
    }

    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        if (this.isRepeaterPowered) {
            for (EnumFacing enumfacing : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), (Block)this);
            }
        }
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    protected boolean canPowerSide(Block blockIn) {
        return blockIn.canProvidePower();
    }

    protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
        return 15;
    }

    public static boolean isRedstoneRepeaterBlockID(Block blockIn) {
        return Blocks.unpowered_repeater.isAssociated(blockIn) || Blocks.unpowered_comparator.isAssociated(blockIn);
    }

    public boolean isAssociated(Block other) {
        return other == this.getPoweredState(this.getDefaultState()).getBlock() || other == this.getUnpoweredState(this.getDefaultState()).getBlock();
    }

    public boolean isFacingTowardsRepeater(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing enumfacing = ((EnumFacing)state.getValue((IProperty)FACING)).getOpposite();
        BlockPos blockpos = pos.offset(enumfacing);
        return BlockRedstoneDiode.isRedstoneRepeaterBlockID(worldIn.getBlockState(blockpos).getBlock()) ? worldIn.getBlockState(blockpos).getValue((IProperty)FACING) != enumfacing : false;
    }

    protected int getTickDelay(IBlockState state) {
        return this.getDelay(state);
    }

    protected abstract int getDelay(IBlockState var1);

    protected abstract IBlockState getPoweredState(IBlockState var1);

    protected abstract IBlockState getUnpoweredState(IBlockState var1);

    public boolean isAssociatedBlock(Block other) {
        return this.isAssociated(other);
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
}
