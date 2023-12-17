// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;

public class BlockEndRod extends BlockDirectional
{
    protected static final AxisAlignedBB END_ROD_VERTICAL_AABB;
    protected static final AxisAlignedBB END_ROD_NS_AABB;
    protected static final AxisAlignedBB END_ROD_EW_AABB;
    
    protected BlockEndRod() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockEndRod.FACING, EnumFacing.UP));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockEndRod.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockEndRod.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withProperty((IProperty<Comparable>)BlockEndRod.FACING, mirrorIn.mirror(state.getValue((IProperty<EnumFacing>)BlockEndRod.FACING)));
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        switch (state.getValue((IProperty<EnumFacing>)BlockEndRod.FACING).getAxis()) {
            default: {
                return BlockEndRod.END_ROD_EW_AABB;
            }
            case Z: {
                return BlockEndRod.END_ROD_NS_AABB;
            }
            case Y: {
                return BlockEndRod.END_ROD_VERTICAL_AABB;
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
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return true;
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        final IBlockState iblockstate = worldIn.getBlockState(pos.offset(facing.getOpposite()));
        if (iblockstate.getBlock() == Blocks.END_ROD) {
            final EnumFacing enumfacing = iblockstate.getValue((IProperty<EnumFacing>)BlockEndRod.FACING);
            if (enumfacing == facing) {
                return this.getDefaultState().withProperty((IProperty<Comparable>)BlockEndRod.FACING, facing.getOpposite());
            }
        }
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockEndRod.FACING, facing);
    }
    
    @Override
    public void randomDisplayTick(final IBlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        final EnumFacing enumfacing = stateIn.getValue((IProperty<EnumFacing>)BlockEndRod.FACING);
        final double d0 = pos.getX() + 0.55 - rand.nextFloat() * 0.1f;
        final double d2 = pos.getY() + 0.55 - rand.nextFloat() * 0.1f;
        final double d3 = pos.getZ() + 0.55 - rand.nextFloat() * 0.1f;
        final double d4 = 0.4f - (rand.nextFloat() + rand.nextFloat()) * 0.4f;
        if (rand.nextInt(5) == 0) {
            worldIn.spawnParticle(EnumParticleTypes.END_ROD, d0 + enumfacing.getXOffset() * d4, d2 + enumfacing.getYOffset() * d4, d3 + enumfacing.getZOffset() * d4, rand.nextGaussian() * 0.005, rand.nextGaussian() * 0.005, rand.nextGaussian() * 0.005, new int[0]);
        }
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState iblockstate = this.getDefaultState();
        iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockEndRod.FACING, EnumFacing.byIndex(meta));
        return iblockstate;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<EnumFacing>)BlockEndRod.FACING).getIndex();
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockEndRod.FACING });
    }
    
    @Override
    @Deprecated
    public EnumPushReaction getPushReaction(final IBlockState state) {
        return EnumPushReaction.NORMAL;
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        END_ROD_VERTICAL_AABB = new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);
        END_ROD_NS_AABB = new AxisAlignedBB(0.375, 0.375, 0.0, 0.625, 0.625, 1.0);
        END_ROD_EW_AABB = new AxisAlignedBB(0.0, 0.375, 0.375, 1.0, 0.625, 0.625);
    }
}
