// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.world.World;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.properties.PropertyEnum;

public class BlockStainedGlassPane extends BlockPane
{
    public static final PropertyEnum<EnumDyeColor> COLOR;
    
    public BlockStainedGlassPane() {
        super(Material.GLASS, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockStainedGlassPane.NORTH, false).withProperty((IProperty<Comparable>)BlockStainedGlassPane.EAST, false).withProperty((IProperty<Comparable>)BlockStainedGlassPane.SOUTH, false).withProperty((IProperty<Comparable>)BlockStainedGlassPane.WEST, false).withProperty(BlockStainedGlassPane.COLOR, EnumDyeColor.WHITE));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockStainedGlassPane.COLOR).getMetadata();
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> items) {
        for (int i = 0; i < EnumDyeColor.values().length; ++i) {
            items.add(new ItemStack(this, 1, i));
        }
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return MapColor.getBlockColor(state.getValue(BlockStainedGlassPane.COLOR));
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockStainedGlassPane.COLOR, EnumDyeColor.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockStainedGlassPane.COLOR).getMetadata();
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180: {
                return state.withProperty((IProperty<Comparable>)BlockStainedGlassPane.NORTH, (Comparable)state.getValue((IProperty<V>)BlockStainedGlassPane.SOUTH)).withProperty((IProperty<Comparable>)BlockStainedGlassPane.EAST, (Comparable)state.getValue((IProperty<V>)BlockStainedGlassPane.WEST)).withProperty((IProperty<Comparable>)BlockStainedGlassPane.SOUTH, (Comparable)state.getValue((IProperty<V>)BlockStainedGlassPane.NORTH)).withProperty((IProperty<Comparable>)BlockStainedGlassPane.WEST, (Comparable)state.getValue((IProperty<V>)BlockStainedGlassPane.EAST));
            }
            case COUNTERCLOCKWISE_90: {
                return state.withProperty((IProperty<Comparable>)BlockStainedGlassPane.NORTH, (Comparable)state.getValue((IProperty<V>)BlockStainedGlassPane.EAST)).withProperty((IProperty<Comparable>)BlockStainedGlassPane.EAST, (Comparable)state.getValue((IProperty<V>)BlockStainedGlassPane.SOUTH)).withProperty((IProperty<Comparable>)BlockStainedGlassPane.SOUTH, (Comparable)state.getValue((IProperty<V>)BlockStainedGlassPane.WEST)).withProperty((IProperty<Comparable>)BlockStainedGlassPane.WEST, (Comparable)state.getValue((IProperty<V>)BlockStainedGlassPane.NORTH));
            }
            case CLOCKWISE_90: {
                return state.withProperty((IProperty<Comparable>)BlockStainedGlassPane.NORTH, (Comparable)state.getValue((IProperty<V>)BlockStainedGlassPane.WEST)).withProperty((IProperty<Comparable>)BlockStainedGlassPane.EAST, (Comparable)state.getValue((IProperty<V>)BlockStainedGlassPane.NORTH)).withProperty((IProperty<Comparable>)BlockStainedGlassPane.SOUTH, (Comparable)state.getValue((IProperty<V>)BlockStainedGlassPane.EAST)).withProperty((IProperty<Comparable>)BlockStainedGlassPane.WEST, (Comparable)state.getValue((IProperty<V>)BlockStainedGlassPane.SOUTH));
            }
            default: {
                return state;
            }
        }
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        switch (mirrorIn) {
            case LEFT_RIGHT: {
                return state.withProperty((IProperty<Comparable>)BlockStainedGlassPane.NORTH, (Comparable)state.getValue((IProperty<V>)BlockStainedGlassPane.SOUTH)).withProperty((IProperty<Comparable>)BlockStainedGlassPane.SOUTH, (Comparable)state.getValue((IProperty<V>)BlockStainedGlassPane.NORTH));
            }
            case FRONT_BACK: {
                return state.withProperty((IProperty<Comparable>)BlockStainedGlassPane.EAST, (Comparable)state.getValue((IProperty<V>)BlockStainedGlassPane.WEST)).withProperty((IProperty<Comparable>)BlockStainedGlassPane.WEST, (Comparable)state.getValue((IProperty<V>)BlockStainedGlassPane.EAST));
            }
            default: {
                return super.withMirror(state, mirrorIn);
            }
        }
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockStainedGlassPane.NORTH, BlockStainedGlassPane.EAST, BlockStainedGlassPane.WEST, BlockStainedGlassPane.SOUTH, BlockStainedGlassPane.COLOR });
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            BlockBeacon.updateColorAsync(worldIn, pos);
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            BlockBeacon.updateColorAsync(worldIn, pos);
        }
    }
    
    static {
        COLOR = PropertyEnum.create("color", EnumDyeColor.class);
    }
}
