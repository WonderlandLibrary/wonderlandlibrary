// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockPrismarine extends Block
{
    public static final PropertyEnum<EnumType> VARIANT;
    public static final int ROUGH_META;
    public static final int BRICKS_META;
    public static final int DARK_META;
    
    public BlockPrismarine() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPrismarine.VARIANT, EnumType.ROUGH));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public String getLocalizedName() {
        return I18n.translateToLocal(this.getTranslationKey() + "." + EnumType.ROUGH.getTranslationKey() + ".name");
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return (state.getValue(BlockPrismarine.VARIANT) == EnumType.ROUGH) ? MapColor.CYAN : MapColor.DIAMOND;
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockPrismarine.VARIANT).getMetadata();
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockPrismarine.VARIANT).getMetadata();
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockPrismarine.VARIANT });
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockPrismarine.VARIANT, EnumType.byMetadata(meta));
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, BlockPrismarine.ROUGH_META));
        items.add(new ItemStack(this, 1, BlockPrismarine.BRICKS_META));
        items.add(new ItemStack(this, 1, BlockPrismarine.DARK_META));
    }
    
    static {
        VARIANT = PropertyEnum.create("variant", EnumType.class);
        ROUGH_META = EnumType.ROUGH.getMetadata();
        BRICKS_META = EnumType.BRICKS.getMetadata();
        DARK_META = EnumType.DARK.getMetadata();
    }
    
    public enum EnumType implements IStringSerializable
    {
        ROUGH(0, "prismarine", "rough"), 
        BRICKS(1, "prismarine_bricks", "bricks"), 
        DARK(2, "dark_prismarine", "dark");
        
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
            for (final EnumType blockprismarine$enumtype : values()) {
                EnumType.META_LOOKUP[blockprismarine$enumtype.getMetadata()] = blockprismarine$enumtype;
            }
        }
    }
}
