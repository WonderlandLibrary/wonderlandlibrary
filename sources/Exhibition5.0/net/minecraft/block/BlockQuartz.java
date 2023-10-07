// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.material.MapColor;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockQuartz extends Block
{
    public static final PropertyEnum VARIANT_PROP;
    private static final String __OBFID = "CL_00000292";
    
    public BlockQuartz() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockQuartz.VARIANT_PROP, EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        if (meta != EnumType.LINES_Y.getMetaFromState()) {
            return (meta == EnumType.CHISELED.getMetaFromState()) ? this.getDefaultState().withProperty(BlockQuartz.VARIANT_PROP, EnumType.CHISELED) : this.getDefaultState().withProperty(BlockQuartz.VARIANT_PROP, EnumType.DEFAULT);
        }
        switch (SwitchAxis.field_180101_a[facing.getAxis().ordinal()]) {
            case 1: {
                return this.getDefaultState().withProperty(BlockQuartz.VARIANT_PROP, EnumType.LINES_Z);
            }
            case 2: {
                return this.getDefaultState().withProperty(BlockQuartz.VARIANT_PROP, EnumType.LINES_X);
            }
            default: {
                return this.getDefaultState().withProperty(BlockQuartz.VARIANT_PROP, EnumType.LINES_Y);
            }
        }
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        final EnumType var2 = (EnumType)state.getValue(BlockQuartz.VARIANT_PROP);
        return (var2 != EnumType.LINES_X && var2 != EnumType.LINES_Z) ? var2.getMetaFromState() : EnumType.LINES_Y.getMetaFromState();
    }
    
    @Override
    protected ItemStack createStackedBlock(final IBlockState state) {
        final EnumType var2 = (EnumType)state.getValue(BlockQuartz.VARIANT_PROP);
        return (var2 != EnumType.LINES_X && var2 != EnumType.LINES_Z) ? super.createStackedBlock(state) : new ItemStack(Item.getItemFromBlock(this), 1, EnumType.LINES_Y.getMetaFromState());
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        list.add(new ItemStack(itemIn, 1, EnumType.DEFAULT.getMetaFromState()));
        list.add(new ItemStack(itemIn, 1, EnumType.CHISELED.getMetaFromState()));
        list.add(new ItemStack(itemIn, 1, EnumType.LINES_Y.getMetaFromState()));
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return MapColor.quartzColor;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockQuartz.VARIANT_PROP, EnumType.func_176794_a(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumType)state.getValue(BlockQuartz.VARIANT_PROP)).getMetaFromState();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockQuartz.VARIANT_PROP });
    }
    
    static {
        VARIANT_PROP = PropertyEnum.create("variant", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        DEFAULT("DEFAULT", 0, 0, "default", "default"), 
        CHISELED("CHISELED", 1, 1, "chiseled", "chiseled"), 
        LINES_Y("LINES_Y", 2, 2, "lines_y", "lines"), 
        LINES_X("LINES_X", 3, 3, "lines_x", "lines"), 
        LINES_Z("LINES_Z", 4, 4, "lines_z", "lines");
        
        private static final EnumType[] TYPES_ARRAY;
        private final int field_176798_g;
        private final String field_176805_h;
        private final String field_176806_i;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002074";
        
        private EnumType(final String p_i45691_1_, final int p_i45691_2_, final int p_i45691_3_, final String p_i45691_4_, final String p_i45691_5_) {
            this.field_176798_g = p_i45691_3_;
            this.field_176805_h = p_i45691_4_;
            this.field_176806_i = p_i45691_5_;
        }
        
        public int getMetaFromState() {
            return this.field_176798_g;
        }
        
        @Override
        public String toString() {
            return this.field_176806_i;
        }
        
        public static EnumType func_176794_a(int p_176794_0_) {
            if (p_176794_0_ < 0 || p_176794_0_ >= EnumType.TYPES_ARRAY.length) {
                p_176794_0_ = 0;
            }
            return EnumType.TYPES_ARRAY[p_176794_0_];
        }
        
        @Override
        public String getName() {
            return this.field_176805_h;
        }
        
        static {
            TYPES_ARRAY = new EnumType[values().length];
            $VALUES = new EnumType[] { EnumType.DEFAULT, EnumType.CHISELED, EnumType.LINES_Y, EnumType.LINES_X, EnumType.LINES_Z };
            for (final EnumType var4 : values()) {
                EnumType.TYPES_ARRAY[var4.getMetaFromState()] = var4;
            }
        }
    }
    
    static final class SwitchAxis
    {
        static final int[] field_180101_a;
        private static final String __OBFID = "CL_00002075";
        
        static {
            field_180101_a = new int[EnumFacing.Axis.values().length];
            try {
                SwitchAxis.field_180101_a[EnumFacing.Axis.Z.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchAxis.field_180101_a[EnumFacing.Axis.X.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchAxis.field_180101_a[EnumFacing.Axis.Y.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
        }
    }
}
