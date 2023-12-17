// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.world.IBlockAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.properties.PropertyEnum;

public class BlockConcretePowder extends BlockFalling
{
    public static final PropertyEnum<EnumDyeColor> COLOR;
    
    public BlockConcretePowder() {
        super(Material.SAND);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockConcretePowder.COLOR, EnumDyeColor.WHITE));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public void onEndFalling(final World worldIn, final BlockPos pos, final IBlockState fallingState, final IBlockState hitState) {
        if (hitState.getMaterial().isLiquid()) {
            worldIn.setBlockState(pos, Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, (Comparable)fallingState.getValue((IProperty<V>)BlockConcretePowder.COLOR)), 3);
        }
    }
    
    protected boolean tryTouchWater(final World worldIn, final BlockPos pos, final IBlockState state) {
        boolean flag = false;
        for (final EnumFacing enumfacing : EnumFacing.values()) {
            if (enumfacing != EnumFacing.DOWN) {
                final BlockPos blockpos = pos.offset(enumfacing);
                if (worldIn.getBlockState(blockpos).getMaterial() == Material.WATER) {
                    flag = true;
                    break;
                }
            }
        }
        if (flag) {
            worldIn.setBlockState(pos, Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, (Comparable)state.getValue((IProperty<V>)BlockConcretePowder.COLOR)), 3);
        }
        return flag;
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!this.tryTouchWater(worldIn, pos, state)) {
            super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!this.tryTouchWater(worldIn, pos, state)) {
            super.onBlockAdded(worldIn, pos, state);
        }
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockConcretePowder.COLOR).getMetadata();
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> items) {
        for (final EnumDyeColor enumdyecolor : EnumDyeColor.values()) {
            items.add(new ItemStack(this, 1, enumdyecolor.getMetadata()));
        }
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return MapColor.getBlockColor(state.getValue(BlockConcretePowder.COLOR));
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockConcretePowder.COLOR, EnumDyeColor.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockConcretePowder.COLOR).getMetadata();
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockConcretePowder.COLOR });
    }
    
    static {
        COLOR = PropertyEnum.create("color", EnumDyeColor.class);
    }
}
