// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyInteger;

public class BlockReed extends Block
{
    public static final PropertyInteger AGE;
    protected static final AxisAlignedBB REED_AABB;
    
    protected BlockReed() {
        super(Material.PLANTS);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockReed.AGE, 0));
        this.setTickRandomly(true);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockReed.REED_AABB;
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if ((worldIn.getBlockState(pos.down()).getBlock() == Blocks.REEDS || this.checkForDrop(worldIn, pos, state)) && worldIn.isAirBlock(pos.up())) {
            int i;
            for (i = 1; worldIn.getBlockState(pos.down(i)).getBlock() == this; ++i) {}
            if (i < 3) {
                final int j = state.getValue((IProperty<Integer>)BlockReed.AGE);
                if (j == 15) {
                    worldIn.setBlockState(pos.up(), this.getDefaultState());
                    worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockReed.AGE, 0), 4);
                }
                else {
                    worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockReed.AGE, j + 1), 4);
                }
            }
        }
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        final Block block = worldIn.getBlockState(pos.down()).getBlock();
        if (block == this) {
            return true;
        }
        if (block != Blocks.GRASS && block != Blocks.DIRT && block != Blocks.SAND) {
            return false;
        }
        final BlockPos blockpos = pos.down();
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            final IBlockState iblockstate = worldIn.getBlockState(blockpos.offset(enumfacing));
            if (iblockstate.getMaterial() == Material.WATER || iblockstate.getBlock() == Blocks.FROSTED_ICE) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        this.checkForDrop(worldIn, pos, state);
    }
    
    protected final boolean checkForDrop(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.canBlockStay(worldIn, pos)) {
            return true;
        }
        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
        return false;
    }
    
    public boolean canBlockStay(final World worldIn, final BlockPos pos) {
        return this.canPlaceBlockAt(worldIn, pos);
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockReed.NULL_AABB;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.REEDS;
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
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(Items.REEDS);
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockReed.AGE, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockReed.AGE);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockReed.AGE });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        AGE = PropertyInteger.create("age", 0, 15);
        REED_AABB = new AxisAlignedBB(0.125, 0.0, 0.125, 0.875, 1.0, 0.875);
    }
}
