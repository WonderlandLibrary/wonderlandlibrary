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

public class BlockPlanks extends Block
{
    public static final PropertyEnum VARIANT_PROP;
    private static final String __OBFID = "CL_00002082";
    
    public BlockPlanks() {
        super(Material.wood);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPlanks.VARIANT_PROP, EnumType.OAK));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return ((EnumType)state.getValue(BlockPlanks.VARIANT_PROP)).func_176839_a();
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        for (final EnumType var7 : EnumType.values()) {
            list.add(new ItemStack(itemIn, 1, var7.func_176839_a()));
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, EnumType.func_176837_a(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumType)state.getValue(BlockPlanks.VARIANT_PROP)).func_176839_a();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPlanks.VARIANT_PROP });
    }
    
    static {
        VARIANT_PROP = PropertyEnum.create("variant", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        OAK("OAK", 0, 0, "oak"), 
        SPRUCE("SPRUCE", 1, 1, "spruce"), 
        BIRCH("BIRCH", 2, 2, "birch"), 
        JUNGLE("JUNGLE", 3, 3, "jungle"), 
        ACACIA("ACACIA", 4, 4, "acacia"), 
        DARK_OAK("DARK_OAK", 5, 5, "dark_oak", "big_oak");
        
        private static final EnumType[] field_176842_g;
        private final int field_176850_h;
        private final String field_176851_i;
        private final String field_176848_j;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002081";
        
        private EnumType(final String p_i45695_1_, final int p_i45695_2_, final int p_i45695_3_, final String p_i45695_4_) {
            this(p_i45695_1_, p_i45695_2_, p_i45695_3_, p_i45695_4_, p_i45695_4_);
        }
        
        private EnumType(final String p_i45696_1_, final int p_i45696_2_, final int p_i45696_3_, final String p_i45696_4_, final String p_i45696_5_) {
            this.field_176850_h = p_i45696_3_;
            this.field_176851_i = p_i45696_4_;
            this.field_176848_j = p_i45696_5_;
        }
        
        public int func_176839_a() {
            return this.field_176850_h;
        }
        
        @Override
        public String toString() {
            return this.field_176851_i;
        }
        
        public static EnumType func_176837_a(int p_176837_0_) {
            if (p_176837_0_ < 0 || p_176837_0_ >= EnumType.field_176842_g.length) {
                p_176837_0_ = 0;
            }
            return EnumType.field_176842_g[p_176837_0_];
        }
        
        @Override
        public String getName() {
            return this.field_176851_i;
        }
        
        public String func_176840_c() {
            return this.field_176848_j;
        }
        
        static {
            field_176842_g = new EnumType[values().length];
            $VALUES = new EnumType[] { EnumType.OAK, EnumType.SPRUCE, EnumType.BIRCH, EnumType.JUNGLE, EnumType.ACACIA, EnumType.DARK_OAK };
            for (final EnumType var4 : values()) {
                EnumType.field_176842_g[var4.func_176839_a()] = var4;
            }
        }
    }
}
