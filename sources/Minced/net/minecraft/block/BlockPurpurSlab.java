// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public abstract class BlockPurpurSlab extends BlockSlab
{
    public static final PropertyEnum<Variant> VARIANT;
    
    public BlockPurpurSlab() {
        super(Material.ROCK, MapColor.MAGENTA);
        IBlockState iblockstate = this.blockState.getBaseState();
        if (!this.isDouble()) {
            iblockstate = iblockstate.withProperty(BlockPurpurSlab.HALF, EnumBlockHalf.BOTTOM);
        }
        this.setDefaultState(iblockstate.withProperty(BlockPurpurSlab.VARIANT, Variant.DEFAULT));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.PURPUR_SLAB);
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(Blocks.PURPUR_SLAB);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState iblockstate = this.getDefaultState().withProperty(BlockPurpurSlab.VARIANT, Variant.DEFAULT);
        if (!this.isDouble()) {
            iblockstate = iblockstate.withProperty(BlockPurpurSlab.HALF, ((meta & 0x8) == 0x0) ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
        }
        return iblockstate;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        if (!this.isDouble() && state.getValue(BlockPurpurSlab.HALF) == EnumBlockHalf.TOP) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return this.isDouble() ? new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockPurpurSlab.VARIANT }) : new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockPurpurSlab.HALF, BlockPurpurSlab.VARIANT });
    }
    
    @Override
    public String getTranslationKey(final int meta) {
        return super.getTranslationKey();
    }
    
    @Override
    public IProperty<?> getVariantProperty() {
        return BlockPurpurSlab.VARIANT;
    }
    
    @Override
    public Comparable<?> getTypeForItem(final ItemStack stack) {
        return Variant.DEFAULT;
    }
    
    static {
        VARIANT = PropertyEnum.create("variant", Variant.class);
    }
    
    public static class Double extends BlockPurpurSlab
    {
        @Override
        public boolean isDouble() {
            return true;
        }
    }
    
    public static class Half extends BlockPurpurSlab
    {
        @Override
        public boolean isDouble() {
            return false;
        }
    }
    
    public enum Variant implements IStringSerializable
    {
        DEFAULT;
        
        @Override
        public String getName() {
            return "default";
        }
    }
}
