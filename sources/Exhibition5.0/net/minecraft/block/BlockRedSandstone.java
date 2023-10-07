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

public class BlockRedSandstone extends Block
{
    public static final PropertyEnum TYPE;
    private static final String __OBFID = "CL_00002072";
    
    public BlockRedSandstone() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRedSandstone.TYPE, EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return ((EnumType)state.getValue(BlockRedSandstone.TYPE)).getMetaFromState();
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        for (final EnumType var7 : EnumType.values()) {
            list.add(new ItemStack(itemIn, 1, var7.getMetaFromState()));
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockRedSandstone.TYPE, EnumType.func_176825_a(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumType)state.getValue(BlockRedSandstone.TYPE)).getMetaFromState();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockRedSandstone.TYPE });
    }
    
    static {
        TYPE = PropertyEnum.create("type", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        DEFAULT("DEFAULT", 0, 0, "red_sandstone", "default"), 
        CHISELED("CHISELED", 1, 1, "chiseled_red_sandstone", "chiseled"), 
        SMOOTH("SMOOTH", 2, 2, "smooth_red_sandstone", "smooth");
        
        private static final EnumType[] field_176831_d;
        private final int field_176832_e;
        private final String field_176829_f;
        private final String field_176830_g;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002071";
        
        private EnumType(final String p_i45690_1_, final int p_i45690_2_, final int p_i45690_3_, final String p_i45690_4_, final String p_i45690_5_) {
            this.field_176832_e = p_i45690_3_;
            this.field_176829_f = p_i45690_4_;
            this.field_176830_g = p_i45690_5_;
        }
        
        public int getMetaFromState() {
            return this.field_176832_e;
        }
        
        @Override
        public String toString() {
            return this.field_176829_f;
        }
        
        public static EnumType func_176825_a(int p_176825_0_) {
            if (p_176825_0_ < 0 || p_176825_0_ >= EnumType.field_176831_d.length) {
                p_176825_0_ = 0;
            }
            return EnumType.field_176831_d[p_176825_0_];
        }
        
        @Override
        public String getName() {
            return this.field_176829_f;
        }
        
        public String func_176828_c() {
            return this.field_176830_g;
        }
        
        static {
            field_176831_d = new EnumType[values().length];
            $VALUES = new EnumType[] { EnumType.DEFAULT, EnumType.CHISELED, EnumType.SMOOTH };
            for (final EnumType var4 : values()) {
                EnumType.field_176831_d[var4.getMetaFromState()] = var4;
            }
        }
    }
}
