// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockEndPortalFrame extends Block
{
    public static final PropertyDirection FACING;
    public static final PropertyBool EYE;
    protected static final AxisAlignedBB AABB_BLOCK;
    protected static final AxisAlignedBB AABB_EYE;
    private static BlockPattern portalShape;
    
    public BlockEndPortalFrame() {
        super(Material.ROCK, MapColor.GREEN);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockEndPortalFrame.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockEndPortalFrame.EYE, false));
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockEndPortalFrame.AABB_BLOCK;
    }
    
    @Override
    public void addCollisionBoxToList(final IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockEndPortalFrame.AABB_BLOCK);
        if (worldIn.getBlockState(pos).getValue((IProperty<Boolean>)BlockEndPortalFrame.EYE)) {
            Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockEndPortalFrame.AABB_EYE);
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.AIR;
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockEndPortalFrame.FACING, placer.getHorizontalFacing().getOpposite()).withProperty((IProperty<Comparable>)BlockEndPortalFrame.EYE, false);
    }
    
    @Override
    @Deprecated
    public boolean hasComparatorInputOverride(final IBlockState state) {
        return true;
    }
    
    @Override
    @Deprecated
    public int getComparatorInputOverride(final IBlockState blockState, final World worldIn, final BlockPos pos) {
        return blockState.getValue((IProperty<Boolean>)BlockEndPortalFrame.EYE) ? 15 : 0;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockEndPortalFrame.EYE, (meta & 0x4) != 0x0).withProperty((IProperty<Comparable>)BlockEndPortalFrame.FACING, EnumFacing.byHorizontalIndex(meta & 0x3));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockEndPortalFrame.FACING).getHorizontalIndex();
        if (state.getValue((IProperty<Boolean>)BlockEndPortalFrame.EYE)) {
            i |= 0x4;
        }
        return i;
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockEndPortalFrame.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockEndPortalFrame.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockEndPortalFrame.FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockEndPortalFrame.FACING, BlockEndPortalFrame.EYE });
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    public static BlockPattern getOrCreatePortalShape() {
        if (BlockEndPortalFrame.portalShape == null) {
            BlockEndPortalFrame.portalShape = FactoryBlockPattern.start().aisle("?vvv?", ">???<", ">???<", ">???<", "?^^^?").where('?', BlockWorldState.hasState(BlockStateMatcher.ANY)).where('^', BlockWorldState.hasState((Predicate<IBlockState>)BlockStateMatcher.forBlock(Blocks.END_PORTAL_FRAME).where((IProperty<Comparable>)BlockEndPortalFrame.EYE, (com.google.common.base.Predicate<? extends Comparable>)Predicates.equalTo((Object)true)).where((IProperty<Comparable>)BlockEndPortalFrame.FACING, (com.google.common.base.Predicate<? extends Comparable>)Predicates.equalTo((Object)EnumFacing.SOUTH)))).where('>', BlockWorldState.hasState((Predicate<IBlockState>)BlockStateMatcher.forBlock(Blocks.END_PORTAL_FRAME).where((IProperty<Comparable>)BlockEndPortalFrame.EYE, (com.google.common.base.Predicate<? extends Comparable>)Predicates.equalTo((Object)true)).where((IProperty<Comparable>)BlockEndPortalFrame.FACING, (com.google.common.base.Predicate<? extends Comparable>)Predicates.equalTo((Object)EnumFacing.WEST)))).where('v', BlockWorldState.hasState((Predicate<IBlockState>)BlockStateMatcher.forBlock(Blocks.END_PORTAL_FRAME).where((IProperty<Comparable>)BlockEndPortalFrame.EYE, (com.google.common.base.Predicate<? extends Comparable>)Predicates.equalTo((Object)true)).where((IProperty<Comparable>)BlockEndPortalFrame.FACING, (com.google.common.base.Predicate<? extends Comparable>)Predicates.equalTo((Object)EnumFacing.NORTH)))).where('<', BlockWorldState.hasState((Predicate<IBlockState>)BlockStateMatcher.forBlock(Blocks.END_PORTAL_FRAME).where((IProperty<Comparable>)BlockEndPortalFrame.EYE, (com.google.common.base.Predicate<? extends Comparable>)Predicates.equalTo((Object)true)).where((IProperty<Comparable>)BlockEndPortalFrame.FACING, (com.google.common.base.Predicate<? extends Comparable>)Predicates.equalTo((Object)EnumFacing.EAST)))).build();
        }
        return BlockEndPortalFrame.portalShape;
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return (face == EnumFacing.DOWN) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
    
    static {
        FACING = BlockHorizontal.FACING;
        EYE = PropertyBool.create("eye");
        AABB_BLOCK = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.8125, 1.0);
        AABB_EYE = new AxisAlignedBB(0.3125, 0.8125, 0.3125, 0.6875, 1.0, 0.6875);
    }
}
