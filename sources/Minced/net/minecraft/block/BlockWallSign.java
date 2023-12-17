// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyDirection;

public class BlockWallSign extends BlockSign
{
    public static final PropertyDirection FACING;
    protected static final AxisAlignedBB SIGN_EAST_AABB;
    protected static final AxisAlignedBB SIGN_WEST_AABB;
    protected static final AxisAlignedBB SIGN_SOUTH_AABB;
    protected static final AxisAlignedBB SIGN_NORTH_AABB;
    
    public BlockWallSign() {
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockWallSign.FACING, EnumFacing.NORTH));
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        switch (state.getValue((IProperty<EnumFacing>)BlockWallSign.FACING)) {
            default: {
                return BlockWallSign.SIGN_NORTH_AABB;
            }
            case SOUTH: {
                return BlockWallSign.SIGN_SOUTH_AABB;
            }
            case WEST: {
                return BlockWallSign.SIGN_WEST_AABB;
            }
            case EAST: {
                return BlockWallSign.SIGN_EAST_AABB;
            }
        }
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockWallSign.FACING);
        if (!worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getMaterial().isSolid()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing enumfacing = EnumFacing.byIndex(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockWallSign.FACING, enumfacing);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<EnumFacing>)BlockWallSign.FACING).getIndex();
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockWallSign.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockWallSign.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockWallSign.FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockWallSign.FACING });
    }
    
    static {
        FACING = BlockHorizontal.FACING;
        SIGN_EAST_AABB = new AxisAlignedBB(0.0, 0.28125, 0.0, 0.125, 0.78125, 1.0);
        SIGN_WEST_AABB = new AxisAlignedBB(0.875, 0.28125, 0.0, 1.0, 0.78125, 1.0);
        SIGN_SOUTH_AABB = new AxisAlignedBB(0.0, 0.28125, 0.0, 1.0, 0.78125, 0.125);
        SIGN_NORTH_AABB = new AxisAlignedBB(0.0, 0.28125, 0.875, 1.0, 0.78125, 1.0);
    }
}
