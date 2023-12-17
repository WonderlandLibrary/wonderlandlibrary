// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.state.BlockFaceShape;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyDirection;

public class BlockTorch extends Block
{
    public static final PropertyDirection FACING;
    protected static final AxisAlignedBB STANDING_AABB;
    protected static final AxisAlignedBB TORCH_NORTH_AABB;
    protected static final AxisAlignedBB TORCH_SOUTH_AABB;
    protected static final AxisAlignedBB TORCH_WEST_AABB;
    protected static final AxisAlignedBB TORCH_EAST_AABB;
    
    protected BlockTorch() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.UP));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        switch (state.getValue((IProperty<EnumFacing>)BlockTorch.FACING)) {
            case EAST: {
                return BlockTorch.TORCH_EAST_AABB;
            }
            case WEST: {
                return BlockTorch.TORCH_WEST_AABB;
            }
            case SOUTH: {
                return BlockTorch.TORCH_SOUTH_AABB;
            }
            case NORTH: {
                return BlockTorch.TORCH_NORTH_AABB;
            }
            default: {
                return BlockTorch.STANDING_AABB;
            }
        }
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockTorch.NULL_AABB;
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
    
    private boolean canPlaceOn(final World worldIn, final BlockPos pos) {
        final Block block = worldIn.getBlockState(pos).getBlock();
        final boolean flag = block == Blocks.END_GATEWAY || block == Blocks.LIT_PUMPKIN;
        if (worldIn.getBlockState(pos).isTopSolid()) {
            return !flag;
        }
        final boolean flag2 = block instanceof BlockFence || block == Blocks.GLASS || block == Blocks.COBBLESTONE_WALL || block == Blocks.STAINED_GLASS;
        return flag2 && !flag;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        for (final EnumFacing enumfacing : BlockTorch.FACING.getAllowedValues()) {
            if (this.canPlaceAt(worldIn, pos, enumfacing)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean canPlaceAt(final World worldIn, final BlockPos pos, final EnumFacing facing) {
        final BlockPos blockpos = pos.offset(facing.getOpposite());
        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
        final Block block = iblockstate.getBlock();
        final BlockFaceShape blockfaceshape = iblockstate.getBlockFaceShape(worldIn, blockpos, facing);
        return (facing.equals(EnumFacing.UP) && this.canPlaceOn(worldIn, blockpos)) || (facing != EnumFacing.UP && facing != EnumFacing.DOWN && !Block.isExceptBlockForAttachWithPiston(block) && blockfaceshape == BlockFaceShape.SOLID);
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        if (this.canPlaceAt(worldIn, pos, facing)) {
            return this.getDefaultState().withProperty((IProperty<Comparable>)BlockTorch.FACING, facing);
        }
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (this.canPlaceAt(worldIn, pos, enumfacing)) {
                return this.getDefaultState().withProperty((IProperty<Comparable>)BlockTorch.FACING, enumfacing);
            }
        }
        return this.getDefaultState();
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.checkForDrop(worldIn, pos, state);
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        this.onNeighborChangeInternal(worldIn, pos, state);
    }
    
    protected boolean onNeighborChangeInternal(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!this.checkForDrop(worldIn, pos, state)) {
            return true;
        }
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockTorch.FACING);
        final EnumFacing.Axis enumfacing$axis = enumfacing.getAxis();
        final EnumFacing enumfacing2 = enumfacing.getOpposite();
        final BlockPos blockpos = pos.offset(enumfacing2);
        boolean flag = false;
        if (enumfacing$axis.isHorizontal() && worldIn.getBlockState(blockpos).getBlockFaceShape(worldIn, blockpos, enumfacing) != BlockFaceShape.SOLID) {
            flag = true;
        }
        else if (enumfacing$axis.isVertical() && !this.canPlaceOn(worldIn, blockpos)) {
            flag = true;
        }
        if (flag) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return true;
        }
        return false;
    }
    
    protected boolean checkForDrop(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (state.getBlock() == this && this.canPlaceAt(worldIn, pos, state.getValue((IProperty<EnumFacing>)BlockTorch.FACING))) {
            return true;
        }
        if (worldIn.getBlockState(pos).getBlock() == this) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        return false;
    }
    
    @Override
    public void randomDisplayTick(final IBlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        final EnumFacing enumfacing = stateIn.getValue((IProperty<EnumFacing>)BlockTorch.FACING);
        final double d0 = pos.getX() + 0.5;
        final double d2 = pos.getY() + 0.7;
        final double d3 = pos.getZ() + 0.5;
        final double d4 = 0.22;
        final double d5 = 0.27;
        if (enumfacing.getAxis().isHorizontal()) {
            final EnumFacing enumfacing2 = enumfacing.getOpposite();
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.27 * enumfacing2.getXOffset(), d2 + 0.22, d3 + 0.27 * enumfacing2.getZOffset(), 0.0, 0.0, 0.0, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.27 * enumfacing2.getXOffset(), d2 + 0.22, d3 + 0.27 * enumfacing2.getZOffset(), 0.0, 0.0, 0.0, new int[0]);
        }
        else {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d2, d3, 0.0, 0.0, 0.0, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d2, d3, 0.0, 0.0, 0.0, new int[0]);
        }
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState iblockstate = this.getDefaultState();
        switch (meta) {
            case 1: {
                iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.EAST);
                break;
            }
            case 2: {
                iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.WEST);
                break;
            }
            case 3: {
                iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.SOUTH);
                break;
            }
            case 4: {
                iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.NORTH);
                break;
            }
            default: {
                iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.UP);
                break;
            }
        }
        return iblockstate;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        switch (state.getValue((IProperty<EnumFacing>)BlockTorch.FACING)) {
            case EAST: {
                i |= 0x1;
                break;
            }
            case WEST: {
                i |= 0x2;
                break;
            }
            case SOUTH: {
                i |= 0x3;
                break;
            }
            case NORTH: {
                i |= 0x4;
                break;
            }
            default: {
                i |= 0x5;
                break;
            }
        }
        return i;
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockTorch.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockTorch.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockTorch.FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockTorch.FACING });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        FACING = PropertyDirection.create("facing", (Predicate<EnumFacing>)new Predicate<EnumFacing>() {
            public boolean apply(@Nullable final EnumFacing p_apply_1_) {
                return p_apply_1_ != EnumFacing.DOWN;
            }
        });
        STANDING_AABB = new AxisAlignedBB(0.4000000059604645, 0.0, 0.4000000059604645, 0.6000000238418579, 0.6000000238418579, 0.6000000238418579);
        TORCH_NORTH_AABB = new AxisAlignedBB(0.3499999940395355, 0.20000000298023224, 0.699999988079071, 0.6499999761581421, 0.800000011920929, 1.0);
        TORCH_SOUTH_AABB = new AxisAlignedBB(0.3499999940395355, 0.20000000298023224, 0.0, 0.6499999761581421, 0.800000011920929, 0.30000001192092896);
        TORCH_WEST_AABB = new AxisAlignedBB(0.699999988079071, 0.20000000298023224, 0.3499999940395355, 1.0, 0.800000011920929, 0.6499999761581421);
        TORCH_EAST_AABB = new AxisAlignedBB(0.0, 0.20000000298023224, 0.3499999940395355, 0.30000001192092896, 0.800000011920929, 0.6499999761581421);
    }
}
