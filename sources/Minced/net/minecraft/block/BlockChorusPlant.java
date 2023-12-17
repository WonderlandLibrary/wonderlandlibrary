// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.BlockRenderLayer;
import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockChorusPlant extends Block
{
    public static final PropertyBool NORTH;
    public static final PropertyBool EAST;
    public static final PropertyBool SOUTH;
    public static final PropertyBool WEST;
    public static final PropertyBool UP;
    public static final PropertyBool DOWN;
    
    protected BlockChorusPlant() {
        super(Material.PLANTS, MapColor.PURPLE);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockChorusPlant.NORTH, false).withProperty((IProperty<Comparable>)BlockChorusPlant.EAST, false).withProperty((IProperty<Comparable>)BlockChorusPlant.SOUTH, false).withProperty((IProperty<Comparable>)BlockChorusPlant.WEST, false).withProperty((IProperty<Comparable>)BlockChorusPlant.UP, false).withProperty((IProperty<Comparable>)BlockChorusPlant.DOWN, false));
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        final Block block = worldIn.getBlockState(pos.down()).getBlock();
        final Block block2 = worldIn.getBlockState(pos.up()).getBlock();
        final Block block3 = worldIn.getBlockState(pos.north()).getBlock();
        final Block block4 = worldIn.getBlockState(pos.east()).getBlock();
        final Block block5 = worldIn.getBlockState(pos.south()).getBlock();
        final Block block6 = worldIn.getBlockState(pos.west()).getBlock();
        return state.withProperty((IProperty<Comparable>)BlockChorusPlant.DOWN, block == this || block == Blocks.CHORUS_FLOWER || block == Blocks.END_STONE).withProperty((IProperty<Comparable>)BlockChorusPlant.UP, block2 == this || block2 == Blocks.CHORUS_FLOWER).withProperty((IProperty<Comparable>)BlockChorusPlant.NORTH, block3 == this || block3 == Blocks.CHORUS_FLOWER).withProperty((IProperty<Comparable>)BlockChorusPlant.EAST, block4 == this || block4 == Blocks.CHORUS_FLOWER).withProperty((IProperty<Comparable>)BlockChorusPlant.SOUTH, block5 == this || block5 == Blocks.CHORUS_FLOWER).withProperty((IProperty<Comparable>)BlockChorusPlant.WEST, block6 == this || block6 == Blocks.CHORUS_FLOWER);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, final IBlockAccess source, final BlockPos pos) {
        state = state.getActualState(source, pos);
        final float f = 0.1875f;
        final float f2 = state.getValue((IProperty<Boolean>)BlockChorusPlant.WEST) ? 0.0f : 0.1875f;
        final float f3 = state.getValue((IProperty<Boolean>)BlockChorusPlant.DOWN) ? 0.0f : 0.1875f;
        final float f4 = state.getValue((IProperty<Boolean>)BlockChorusPlant.NORTH) ? 0.0f : 0.1875f;
        final float f5 = state.getValue((IProperty<Boolean>)BlockChorusPlant.EAST) ? 1.0f : 0.8125f;
        final float f6 = state.getValue((IProperty<Boolean>)BlockChorusPlant.UP) ? 1.0f : 0.8125f;
        final float f7 = state.getValue((IProperty<Boolean>)BlockChorusPlant.SOUTH) ? 1.0f : 0.8125f;
        return new AxisAlignedBB(f2, f3, f4, f5, f6, f7);
    }
    
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        if (!isActualState) {
            state = state.getActualState(worldIn, pos);
        }
        final float f = 0.1875f;
        final float f2 = 0.8125f;
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875, 0.1875, 0.1875, 0.8125, 0.8125, 0.8125));
        if (state.getValue((IProperty<Boolean>)BlockChorusPlant.WEST)) {
            Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.0, 0.1875, 0.1875, 0.1875, 0.8125, 0.8125));
        }
        if (state.getValue((IProperty<Boolean>)BlockChorusPlant.EAST)) {
            Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.8125, 0.1875, 0.1875, 1.0, 0.8125, 0.8125));
        }
        if (state.getValue((IProperty<Boolean>)BlockChorusPlant.UP)) {
            Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875, 0.8125, 0.1875, 0.8125, 1.0, 0.8125));
        }
        if (state.getValue((IProperty<Boolean>)BlockChorusPlant.DOWN)) {
            Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875, 0.0, 0.1875, 0.8125, 0.1875, 0.8125));
        }
        if (state.getValue((IProperty<Boolean>)BlockChorusPlant.NORTH)) {
            Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875, 0.1875, 0.0, 0.8125, 0.8125, 0.1875));
        }
        if (state.getValue((IProperty<Boolean>)BlockChorusPlant.SOUTH)) {
            Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875, 0.1875, 0.8125, 0.8125, 0.8125, 1.0));
        }
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!this.canSurviveAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.CHORUS_FRUIT;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return random.nextInt(2);
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
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canSurviveAt(worldIn, pos);
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!this.canSurviveAt(worldIn, pos)) {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }
    
    public boolean canSurviveAt(final World wordIn, final BlockPos pos) {
        final boolean flag = wordIn.isAirBlock(pos.up());
        final boolean flag2 = wordIn.isAirBlock(pos.down());
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            final BlockPos blockpos = pos.offset(enumfacing);
            final Block block = wordIn.getBlockState(blockpos).getBlock();
            if (block == this) {
                if (!flag && !flag2) {
                    return false;
                }
                final Block block2 = wordIn.getBlockState(blockpos.down()).getBlock();
                if (block2 == this || block2 == Blocks.END_STONE) {
                    return true;
                }
                continue;
            }
        }
        final Block block3 = wordIn.getBlockState(pos.down()).getBlock();
        return block3 == this || block3 == Blocks.END_STONE;
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    @Deprecated
    public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        final Block block = blockAccess.getBlockState(pos.offset(side)).getBlock();
        return block != this && block != Blocks.CHORUS_FLOWER && (side != EnumFacing.DOWN || block != Blocks.END_STONE);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockChorusPlant.NORTH, BlockChorusPlant.EAST, BlockChorusPlant.SOUTH, BlockChorusPlant.WEST, BlockChorusPlant.UP, BlockChorusPlant.DOWN });
    }
    
    @Override
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return false;
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        NORTH = PropertyBool.create("north");
        EAST = PropertyBool.create("east");
        SOUTH = PropertyBool.create("south");
        WEST = PropertyBool.create("west");
        UP = PropertyBool.create("up");
        DOWN = PropertyBool.create("down");
    }
}
