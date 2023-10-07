// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockPrismarine extends Block
{
    public static final PropertyEnum VARIANTS;
    public static final int ROUGHMETA;
    public static final int BRICKSMETA;
    public static final int DARKMETA;
    private static final String __OBFID = "CL_00002077";
    
    public BlockPrismarine() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPrismarine.VARIANTS, EnumType.ROUGH));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return ((EnumType)state.getValue(BlockPrismarine.VARIANTS)).getMetadata();
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumType)state.getValue(BlockPrismarine.VARIANTS)).getMetadata();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPrismarine.VARIANTS });
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockPrismarine.VARIANTS, EnumType.func_176810_a(meta));
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        list.add(new ItemStack(itemIn, 1, BlockPrismarine.ROUGHMETA));
        list.add(new ItemStack(itemIn, 1, BlockPrismarine.BRICKSMETA));
        list.add(new ItemStack(itemIn, 1, BlockPrismarine.DARKMETA));
    }
    
    static {
        VARIANTS = PropertyEnum.create("variant", EnumType.class);
        ROUGHMETA = EnumType.ROUGH.getMetadata();
        BRICKSMETA = EnumType.BRICKS.getMetadata();
        DARKMETA = EnumType.DARK.getMetadata();
    }
    
    public enum EnumType implements IStringSerializable
    {
        ROUGH("ROUGH", 0, 0, "prismarine", "rough"), 
        BRICKS("BRICKS", 1, 1, "prismarine_bricks", "bricks"), 
        DARK("DARK", 2, 2, "dark_prismarine", "dark");
        
        private static final EnumType[] field_176813_d;
        private final int meta;
        private final String field_176811_f;
        private final String field_176812_g;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002076";
        
        private EnumType(final String p_i45692_1_, final int p_i45692_2_, final int p_i45692_3_, final String p_i45692_4_, final String p_i45692_5_) {
            this.meta = p_i45692_3_;
            this.field_176811_f = p_i45692_4_;
            this.field_176812_g = p_i45692_5_;
        }
        
        public int getMetadata() {
            return this.meta;
        }
        
        @Override
        public String toString() {
            return this.field_176811_f;
        }
        
        public static EnumType func_176810_a(int p_176810_0_) {
            if (p_176810_0_ < 0 || p_176810_0_ >= EnumType.field_176813_d.length) {
                p_176810_0_ = 0;
            }
            return EnumType.field_176813_d[p_176810_0_];
        }
        
        @Override
        public String getName() {
            return this.field_176811_f;
        }
        
        public String func_176809_c() {
            return this.field_176812_g;
        }
        
        static {
            field_176813_d = new EnumType[values().length];
            $VALUES = new EnumType[] { EnumType.ROUGH, EnumType.BRICKS, EnumType.DARK };
            for (final EnumType var4 : values()) {
                EnumType.field_176813_d[var4.getMetadata()] = var4;
            }
        }
    }
}
