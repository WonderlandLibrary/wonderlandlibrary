// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;

public abstract class BlockRedstoneDiode extends BlockHorizontal
{
    protected static final AxisAlignedBB REDSTONE_DIODE_AABB;
    protected final boolean isRepeaterPowered;
    
    protected BlockRedstoneDiode(final boolean powered) {
        super(Material.CIRCUITS);
        this.isRepeaterPowered = powered;
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockRedstoneDiode.REDSTONE_DIODE_AABB;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos.down()).isTopSolid() && super.canPlaceBlockAt(worldIn, pos);
    }
    
    public boolean canBlockStay(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos.down()).isTopSolid();
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!this.isLocked(worldIn, pos, state)) {
            final boolean flag = this.shouldBePowered(worldIn, pos, state);
            if (this.isRepeaterPowered && !flag) {
                worldIn.setBlockState(pos, this.getUnpoweredState(state), 2);
            }
            else if (!this.isRepeaterPowered) {
                worldIn.setBlockState(pos, this.getPoweredState(state), 2);
                if (!flag) {
                    worldIn.updateBlockTick(pos, this.getPoweredState(state).getBlock(), this.getTickDelay(state), -1);
                }
            }
        }
    }
    
    @Override
    @Deprecated
    public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return side.getAxis() != EnumFacing.Axis.Y;
    }
    
    protected boolean isPowered(final IBlockState state) {
        return this.isRepeaterPowered;
    }
    
    @Override
    @Deprecated
    public int getStrongPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return blockState.getWeakPower(blockAccess, pos, side);
    }
    
    @Override
    @Deprecated
    public int getWeakPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        if (!this.isPowered(blockState)) {
            return 0;
        }
        return (blockState.getValue((IProperty<Comparable>)BlockRedstoneDiode.FACING) == side) ? this.getActiveSignal(blockAccess, pos, blockState) : 0;
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (this.canBlockStay(worldIn, pos)) {
            this.updateState(worldIn, pos, state);
        }
        else {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            for (final EnumFacing enumfacing : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
        }
    }
    
    protected void updateState(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!this.isLocked(worldIn, pos, state)) {
            final boolean flag = this.shouldBePowered(worldIn, pos, state);
            if (this.isRepeaterPowered != flag && !worldIn.isBlockTickPending(pos, this)) {
                int i = -1;
                if (this.isFacingTowardsRepeater(worldIn, pos, state)) {
                    i = -3;
                }
                else if (this.isRepeaterPowered) {
                    i = -2;
                }
                worldIn.updateBlockTick(pos, this, this.getDelay(state), i);
            }
        }
    }
    
    public boolean isLocked(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state) {
        return false;
    }
    
    protected boolean shouldBePowered(final World worldIn, final BlockPos pos, final IBlockState state) {
        return this.calculateInputStrength(worldIn, pos, state) > 0;
    }
    
    protected int calculateInputStrength(final World worldIn, final BlockPos pos, final IBlockState state) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockRedstoneDiode.FACING);
        final BlockPos blockpos = pos.offset(enumfacing);
        final int i = worldIn.getRedstonePower(blockpos, enumfacing);
        if (i >= 15) {
            return i;
        }
        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
        return Math.max(i, (iblockstate.getBlock() == Blocks.REDSTONE_WIRE) ? ((int)iblockstate.getValue((IProperty<Integer>)BlockRedstoneWire.POWER)) : 0);
    }
    
    protected int getPowerOnSides(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockRedstoneDiode.FACING);
        final EnumFacing enumfacing2 = enumfacing.rotateY();
        final EnumFacing enumfacing3 = enumfacing.rotateYCCW();
        return Math.max(this.getPowerOnSide(worldIn, pos.offset(enumfacing2), enumfacing2), this.getPowerOnSide(worldIn, pos.offset(enumfacing3), enumfacing3));
    }
    
    protected int getPowerOnSide(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        if (!this.isAlternateInput(iblockstate)) {
            return 0;
        }
        if (block == Blocks.REDSTONE_BLOCK) {
            return 15;
        }
        return (block == Blocks.REDSTONE_WIRE) ? iblockstate.getValue((IProperty<Integer>)BlockRedstoneWire.POWER) : worldIn.getStrongPower(pos, side);
    }
    
    @Override
    @Deprecated
    public boolean canProvidePower(final IBlockState state) {
        return true;
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockRedstoneDiode.FACING, placer.getHorizontalFacing().getOpposite());
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        if (this.shouldBePowered(worldIn, pos, state)) {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.notifyNeighbors(worldIn, pos, state);
    }
    
    protected void notifyNeighbors(final World worldIn, final BlockPos pos, final IBlockState state) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockRedstoneDiode.FACING);
        final BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        worldIn.neighborChanged(blockpos, this, pos);
        worldIn.notifyNeighborsOfStateExcept(blockpos, this, enumfacing);
    }
    
    @Override
    public void onPlayerDestroy(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.isRepeaterPowered) {
            for (final EnumFacing enumfacing : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
        }
        super.onPlayerDestroy(worldIn, pos, state);
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    protected boolean isAlternateInput(final IBlockState state) {
        return state.canProvidePower();
    }
    
    protected int getActiveSignal(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state) {
        return 15;
    }
    
    public static boolean isDiode(final IBlockState state) {
        return Blocks.UNPOWERED_REPEATER.isSameDiode(state) || Blocks.UNPOWERED_COMPARATOR.isSameDiode(state);
    }
    
    public boolean isSameDiode(final IBlockState state) {
        final Block block = state.getBlock();
        return block == this.getPoweredState(this.getDefaultState()).getBlock() || block == this.getUnpoweredState(this.getDefaultState()).getBlock();
    }
    
    public boolean isFacingTowardsRepeater(final World worldIn, final BlockPos pos, final IBlockState state) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockRedstoneDiode.FACING).getOpposite();
        final BlockPos blockpos = pos.offset(enumfacing);
        return isDiode(worldIn.getBlockState(blockpos)) && worldIn.getBlockState(blockpos).getValue((IProperty<Comparable>)BlockRedstoneDiode.FACING) != enumfacing;
    }
    
    protected int getTickDelay(final IBlockState state) {
        return this.getDelay(state);
    }
    
    protected abstract int getDelay(final IBlockState p0);
    
    protected abstract IBlockState getPoweredState(final IBlockState p0);
    
    protected abstract IBlockState getUnpoweredState(final IBlockState p0);
    
    @Override
    public boolean isAssociatedBlock(final Block other) {
        return this.isSameDiode(other.getDefaultState());
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return (face == EnumFacing.DOWN) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
    
    static {
        REDSTONE_DIODE_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
    }
}
