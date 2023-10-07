// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockStoneBrick extends Block
{
    public static final PropertyEnum VARIANT_PROP;
    public static final int DEFAULT_META;
    public static final int MOSSY_META;
    public static final int CRACKED_META;
    public static final int CHISELED_META;
    private static final String __OBFID = "CL_00000318";
    
    public BlockStoneBrick() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockStoneBrick.VARIANT_PROP, EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return ((EnumType)state.getValue(BlockStoneBrick.VARIANT_PROP)).getMetaFromState();
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        for (final EnumType var7 : EnumType.values()) {
            list.add(new ItemStack(itemIn, 1, var7.getMetaFromState()));
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockStoneBrick.VARIANT_PROP, EnumType.getStateFromMeta(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumType)state.getValue(BlockStoneBrick.VARIANT_PROP)).getMetaFromState();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockStoneBrick.VARIANT_PROP });
    }
    
    static {
        VARIANT_PROP = PropertyEnum.create("variant", EnumType.class);
        DEFAULT_META = EnumType.DEFAULT.getMetaFromState();
        MOSSY_META = EnumType.MOSSY.getMetaFromState();
        CRACKED_META = EnumType.CRACKED.getMetaFromState();
        CHISELED_META = EnumType.CHISELED.getMetaFromState();
    }
    
    public enum EnumType implements IStringSerializable
    {
        DEFAULT("DEFAULT", 0, 0, "stonebrick", "default"), 
        MOSSY("MOSSY", 1, 1, "mossy_stonebrick", "mossy"), 
        CRACKED("CRACKED", 2, 2, "cracked_stonebrick", "cracked"), 
        CHISELED("CHISELED", 3, 3, "chiseled_stonebrick", "chiseled");
        
        private static final EnumType[] TYPES_ARRAY;
        private final int field_176615_f;
        private final String field_176616_g;
        private final String field_176622_h;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002057";
        
        private EnumType(final String p_i45679_1_, final int p_i45679_2_, final int p_i45679_3_, final String p_i45679_4_, final String p_i45679_5_) {
            this.field_176615_f = p_i45679_3_;
            this.field_176616_g = p_i45679_4_;
            this.field_176622_h = p_i45679_5_;
        }
        
        public int getMetaFromState() {
            return this.field_176615_f;
        }
        
        @Override
        public String toString() {
            return this.field_176616_g;
        }
        
        public static EnumType getStateFromMeta(int p_176613_0_) {
            if (p_176613_0_ < 0 || p_176613_0_ >= EnumType.TYPES_ARRAY.length) {
                p_176613_0_ = 0;
            }
            return EnumType.TYPES_ARRAY[p_176613_0_];
        }
        
        @Override
        public String getName() {
            return this.field_176616_g;
        }
        
        public String getVariantName() {
            return this.field_176622_h;
        }
        
        static {
            TYPES_ARRAY = new EnumType[values().length];
            $VALUES = new EnumType[] { EnumType.DEFAULT, EnumType.MOSSY, EnumType.CRACKED, EnumType.CHISELED };
            for (final EnumType var4 : values()) {
                EnumType.TYPES_ARRAY[var4.getMetaFromState()] = var4;
            }
        }
    }
}
