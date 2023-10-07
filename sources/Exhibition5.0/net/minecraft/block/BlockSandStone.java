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

public class BlockSandStone extends Block
{
    public static final PropertyEnum field_176297_a;
    private static final String __OBFID = "CL_00000304";
    
    public BlockSandStone() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSandStone.field_176297_a, EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return ((EnumType)state.getValue(BlockSandStone.field_176297_a)).func_176675_a();
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        for (final EnumType var7 : EnumType.values()) {
            list.add(new ItemStack(itemIn, 1, var7.func_176675_a()));
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockSandStone.field_176297_a, EnumType.func_176673_a(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumType)state.getValue(BlockSandStone.field_176297_a)).func_176675_a();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockSandStone.field_176297_a });
    }
    
    static {
        field_176297_a = PropertyEnum.create("type", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        DEFAULT("DEFAULT", 0, 0, "sandstone", "default"), 
        CHISELED("CHISELED", 1, 1, "chiseled_sandstone", "chiseled"), 
        SMOOTH("SMOOTH", 2, 2, "smooth_sandstone", "smooth");
        
        private static final EnumType[] field_176679_d;
        private final int field_176680_e;
        private final String field_176677_f;
        private final String field_176678_g;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002068";
        
        private EnumType(final String p_i45686_1_, final int p_i45686_2_, final int p_i45686_3_, final String p_i45686_4_, final String p_i45686_5_) {
            this.field_176680_e = p_i45686_3_;
            this.field_176677_f = p_i45686_4_;
            this.field_176678_g = p_i45686_5_;
        }
        
        public int func_176675_a() {
            return this.field_176680_e;
        }
        
        @Override
        public String toString() {
            return this.field_176677_f;
        }
        
        public static EnumType func_176673_a(int p_176673_0_) {
            if (p_176673_0_ < 0 || p_176673_0_ >= EnumType.field_176679_d.length) {
                p_176673_0_ = 0;
            }
            return EnumType.field_176679_d[p_176673_0_];
        }
        
        @Override
        public String getName() {
            return this.field_176677_f;
        }
        
        public String func_176676_c() {
            return this.field_176678_g;
        }
        
        static {
            field_176679_d = new EnumType[values().length];
            $VALUES = new EnumType[] { EnumType.DEFAULT, EnumType.CHISELED, EnumType.SMOOTH };
            for (final EnumType var4 : values()) {
                EnumType.field_176679_d[var4.func_176675_a()] = var4;
            }
        }
    }
}
