// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.BlockRenderLayer;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyDirection;

public class BlockLadder extends Block
{
    public static final PropertyDirection FACING;
    protected static final AxisAlignedBB LADDER_EAST_AABB;
    protected static final AxisAlignedBB LADDER_WEST_AABB;
    protected static final AxisAlignedBB LADDER_SOUTH_AABB;
    protected static final AxisAlignedBB LADDER_NORTH_AABB;
    
    protected BlockLadder() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockLadder.FACING, EnumFacing.NORTH));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        switch (state.getValue((IProperty<EnumFacing>)BlockLadder.FACING)) {
            case NORTH: {
                return BlockLadder.LADDER_NORTH_AABB;
            }
            case SOUTH: {
                return BlockLadder.LADDER_SOUTH_AABB;
            }
            case WEST: {
                return BlockLadder.LADDER_WEST_AABB;
            }
            default: {
                return BlockLadder.LADDER_EAST_AABB;
            }
        }
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
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return this.canAttachTo(worldIn, pos.west(), side) || this.canAttachTo(worldIn, pos.east(), side) || this.canAttachTo(worldIn, pos.north(), side) || this.canAttachTo(worldIn, pos.south(), side);
    }
    
    private boolean canAttachTo(final World p_193392_1_, final BlockPos p_193392_2_, final EnumFacing p_193392_3_) {
        final IBlockState iblockstate = p_193392_1_.getBlockState(p_193392_2_);
        final boolean flag = Block.isExceptBlockForAttachWithPiston(iblockstate.getBlock());
        return !flag && iblockstate.getBlockFaceShape(p_193392_1_, p_193392_2_, p_193392_3_) == BlockFaceShape.SOLID && !iblockstate.canProvidePower();
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        if (facing.getAxis().isHorizontal() && this.canAttachTo(worldIn, pos.offset(facing.getOpposite()), facing)) {
            return this.getDefaultState().withProperty((IProperty<Comparable>)BlockLadder.FACING, facing);
        }
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (this.canAttachTo(worldIn, pos.offset(enumfacing.getOpposite()), enumfacing)) {
                return this.getDefaultState().withProperty((IProperty<Comparable>)BlockLadder.FACING, enumfacing);
            }
        }
        return this.getDefaultState();
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockLadder.FACING);
        if (!this.canAttachTo(worldIn, pos.offset(enumfacing.getOpposite()), enumfacing)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing enumfacing = EnumFacing.byIndex(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockLadder.FACING, enumfacing);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<EnumFacing>)BlockLadder.FACING).getIndex();
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockLadder.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockLadder.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockLadder.FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockLadder.FACING });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        FACING = BlockHorizontal.FACING;
        LADDER_EAST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.1875, 1.0, 1.0);
        LADDER_WEST_AABB = new AxisAlignedBB(0.8125, 0.0, 0.0, 1.0, 1.0, 1.0);
        LADDER_SOUTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.1875);
        LADDER_NORTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.8125, 1.0, 1.0, 1.0);
    }
}
