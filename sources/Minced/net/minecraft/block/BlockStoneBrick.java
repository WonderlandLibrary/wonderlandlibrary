// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockStoneBrick extends Block
{
    public static final PropertyEnum<EnumType> VARIANT;
    public static final int DEFAULT_META;
    public static final int MOSSY_META;
    public static final int CRACKED_META;
    public static final int CHISELED_META;
    
    public BlockStoneBrick() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockStoneBrick.VARIANT, EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockStoneBrick.VARIANT).getMetadata();
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> items) {
        for (final EnumType blockstonebrick$enumtype : EnumType.values()) {
            items.add(new ItemStack(this, 1, blockstonebrick$enumtype.getMetadata()));
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockStoneBrick.VARIANT, EnumType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockStoneBrick.VARIANT).getMetadata();
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockStoneBrick.VARIANT });
    }
    
    static {
        VARIANT = PropertyEnum.create("variant", EnumType.class);
        DEFAULT_META = EnumType.DEFAULT.getMetadata();
        MOSSY_META = EnumType.MOSSY.getMetadata();
        CRACKED_META = EnumType.CRACKED.getMetadata();
        CHISELED_META = EnumType.CHISELED.getMetadata();
    }
    
    public enum EnumType implements IStringSerializable
    {
        DEFAULT(0, "stonebrick", "default"), 
        MOSSY(1, "mossy_stonebrick", "mossy"), 
        CRACKED(2, "cracked_stonebrick", "cracked"), 
        CHISELED(3, "chiseled_stonebrick", "chiseled");
        
        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String name;
        private final String translationKey;
        
        private EnumType(final int meta, final String name, final String unlocalizedName) {
            this.meta = meta;
            this.name = name;
            this.translationKey = unlocalizedName;
        }
        
        public int getMetadata() {
            return this.meta;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public static EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= EnumType.META_LOOKUP.length) {
                meta = 0;
            }
            return EnumType.META_LOOKUP[meta];
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        public String getTranslationKey() {
            return this.translationKey;
        }
        
        static {
            META_LOOKUP = new EnumType[values().length];
            for (final EnumType blockstonebrick$enumtype : values()) {
                EnumType.META_LOOKUP[blockstonebrick$enumtype.getMetadata()] = blockstonebrick$enumtype;
            }
        }
    }
}
