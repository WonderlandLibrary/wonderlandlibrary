// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraft.entity.Entity;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;

public abstract class BlockBasePressurePlate extends Block
{
    protected static final AxisAlignedBB PRESSED_AABB;
    protected static final AxisAlignedBB UNPRESSED_AABB;
    protected static final AxisAlignedBB PRESSURE_AABB;
    
    protected BlockBasePressurePlate(final Material materialIn) {
        this(materialIn, materialIn.getMaterialMapColor());
    }
    
    protected BlockBasePressurePlate(final Material materialIn, final MapColor mapColorIn) {
        super(materialIn, mapColorIn);
        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.setTickRandomly(true);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        final boolean flag = this.getRedstoneStrength(state) > 0;
        return flag ? BlockBasePressurePlate.PRESSED_AABB : BlockBasePressurePlate.UNPRESSED_AABB;
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 20;
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockBasePressurePlate.NULL_AABB;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return true;
    }
    
    @Override
    public boolean canSpawnInBlock() {
        return true;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return this.canBePlacedOn(worldIn, pos.down());
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!this.canBePlacedOn(worldIn, pos.down())) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }
    
    private boolean canBePlacedOn(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos).isTopSolid() || worldIn.getBlockState(pos).getBlock() instanceof BlockFence;
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote) {
            final int i = this.getRedstoneStrength(state);
            if (i > 0) {
                this.updateState(worldIn, pos, state, i);
            }
        }
    }
    
    @Override
    public void onEntityCollision(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (!worldIn.isRemote) {
            final int i = this.getRedstoneStrength(state);
            if (i == 0) {
                this.updateState(worldIn, pos, state, i);
            }
        }
    }
    
    protected void updateState(final World worldIn, final BlockPos pos, IBlockState state, final int oldRedstoneStrength) {
        final int i = this.computeRedstoneStrength(worldIn, pos);
        final boolean flag = oldRedstoneStrength > 0;
        final boolean flag2 = i > 0;
        if (oldRedstoneStrength != i) {
            state = this.setRedstoneStrength(state, i);
            worldIn.setBlockState(pos, state, 2);
            this.updateNeighbors(worldIn, pos);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
        }
        if (!flag2 && flag) {
            this.playClickOffSound(worldIn, pos);
        }
        else if (flag2 && !flag) {
            this.playClickOnSound(worldIn, pos);
        }
        if (flag2) {
            worldIn.scheduleUpdate(new BlockPos(pos), this, this.tickRate(worldIn));
        }
    }
    
    protected abstract void playClickOnSound(final World p0, final BlockPos p1);
    
    protected abstract void playClickOffSound(final World p0, final BlockPos p1);
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.getRedstoneStrength(state) > 0) {
            this.updateNeighbors(worldIn, pos);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    protected void updateNeighbors(final World worldIn, final BlockPos pos) {
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
        worldIn.notifyNeighborsOfStateChange(pos.down(), this, false);
    }
    
    @Override
    @Deprecated
    public int getWeakPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return this.getRedstoneStrength(blockState);
    }
    
    @Override
    @Deprecated
    public int getStrongPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return (side == EnumFacing.UP) ? this.getRedstoneStrength(blockState) : 0;
    }
    
    @Override
    @Deprecated
    public boolean canProvidePower(final IBlockState state) {
        return true;
    }
    
    @Override
    @Deprecated
    public EnumPushReaction getPushReaction(final IBlockState state) {
        return EnumPushReaction.DESTROY;
    }
    
    protected abstract int computeRedstoneStrength(final World p0, final BlockPos p1);
    
    protected abstract int getRedstoneStrength(final IBlockState p0);
    
    protected abstract IBlockState setRedstoneStrength(final IBlockState p0, final int p1);
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        PRESSED_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.03125, 0.9375);
        UNPRESSED_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.0625, 0.9375);
        PRESSURE_AABB = new AxisAlignedBB(0.125, 0.0, 0.125, 0.875, 0.25, 0.875);
    }
}
