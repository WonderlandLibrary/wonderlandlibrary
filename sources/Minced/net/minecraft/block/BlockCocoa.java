// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyInteger;

public class BlockCocoa extends BlockHorizontal implements IGrowable
{
    public static final PropertyInteger AGE;
    protected static final AxisAlignedBB[] COCOA_EAST_AABB;
    protected static final AxisAlignedBB[] COCOA_WEST_AABB;
    protected static final AxisAlignedBB[] COCOA_NORTH_AABB;
    protected static final AxisAlignedBB[] COCOA_SOUTH_AABB;
    
    public BlockCocoa() {
        super(Material.PLANTS);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockCocoa.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockCocoa.AGE, 0));
        this.setTickRandomly(true);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlock(worldIn, pos, state);
        }
        else if (worldIn.rand.nextInt(5) == 0) {
            final int i = state.getValue((IProperty<Integer>)BlockCocoa.AGE);
            if (i < 2) {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockCocoa.AGE, i + 1), 2);
            }
        }
    }
    
    public boolean canBlockStay(final World worldIn, BlockPos pos, final IBlockState state) {
        pos = pos.offset(state.getValue((IProperty<EnumFacing>)BlockCocoa.FACING));
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock() == Blocks.LOG && iblockstate.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        final int i = state.getValue((IProperty<Integer>)BlockCocoa.AGE);
        switch (state.getValue((IProperty<EnumFacing>)BlockCocoa.FACING)) {
            case SOUTH: {
                return BlockCocoa.COCOA_SOUTH_AABB[i];
            }
            default: {
                return BlockCocoa.COCOA_NORTH_AABB[i];
            }
            case WEST: {
                return BlockCocoa.COCOA_WEST_AABB[i];
            }
            case EAST: {
                return BlockCocoa.COCOA_EAST_AABB[i];
            }
        }
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockCocoa.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockCocoa.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockCocoa.FACING)));
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        final EnumFacing enumfacing = EnumFacing.fromAngle(placer.rotationYaw);
        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockCocoa.FACING, enumfacing), 2);
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        if (!facing.getAxis().isHorizontal()) {
            facing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockCocoa.FACING, facing.getOpposite()).withProperty((IProperty<Comparable>)BlockCocoa.AGE, 0);
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlock(worldIn, pos, state);
        }
    }
    
    private void dropBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        this.dropBlockAsItem(worldIn, pos, state, 0);
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        final int i = state.getValue((IProperty<Integer>)BlockCocoa.AGE);
        int j = 1;
        if (i >= 2) {
            j = 3;
        }
        for (int k = 0; k < j; ++k) {
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage()));
        }
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage());
    }
    
    @Override
    public boolean canGrow(final World worldIn, final BlockPos pos, final IBlockState state, final boolean isClient) {
        return state.getValue((IProperty<Integer>)BlockCocoa.AGE) < 2;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        return true;
    }
    
    @Override
    public void grow(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockCocoa.AGE, state.getValue((IProperty<Integer>)BlockCocoa.AGE) + 1), 2);
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockCocoa.FACING, EnumFacing.byHorizontalIndex(meta)).withProperty((IProperty<Comparable>)BlockCocoa.AGE, (meta & 0xF) >> 2);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockCocoa.FACING).getHorizontalIndex();
        i |= state.getValue((IProperty<Integer>)BlockCocoa.AGE) << 2;
        return i;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockCocoa.FACING, BlockCocoa.AGE });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        AGE = PropertyInteger.create("age", 0, 2);
        COCOA_EAST_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.6875, 0.4375, 0.375, 0.9375, 0.75, 0.625), new AxisAlignedBB(0.5625, 0.3125, 0.3125, 0.9375, 0.75, 0.6875), new AxisAlignedBB(0.4375, 0.1875, 0.25, 0.9375, 0.75, 0.75) };
        COCOA_WEST_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.0625, 0.4375, 0.375, 0.3125, 0.75, 0.625), new AxisAlignedBB(0.0625, 0.3125, 0.3125, 0.4375, 0.75, 0.6875), new AxisAlignedBB(0.0625, 0.1875, 0.25, 0.5625, 0.75, 0.75) };
        COCOA_NORTH_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.375, 0.4375, 0.0625, 0.625, 0.75, 0.3125), new AxisAlignedBB(0.3125, 0.3125, 0.0625, 0.6875, 0.75, 0.4375), new AxisAlignedBB(0.25, 0.1875, 0.0625, 0.75, 0.75, 0.5625) };
        COCOA_SOUTH_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.375, 0.4375, 0.6875, 0.625, 0.75, 0.9375), new AxisAlignedBB(0.3125, 0.3125, 0.5625, 0.6875, 0.75, 0.9375), new AxisAlignedBB(0.25, 0.1875, 0.4375, 0.75, 0.75, 0.9375) };
    }
}
