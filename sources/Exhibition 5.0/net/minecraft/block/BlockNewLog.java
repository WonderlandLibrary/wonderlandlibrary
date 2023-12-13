// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;

public class BlockNewLog extends BlockLog
{
    public static final PropertyEnum field_176300_b;
    private static final String __OBFID = "CL_00000277";
    
    public BlockNewLog() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockNewLog.field_176300_b, BlockPlanks.EnumType.ACACIA).withProperty(BlockLog.AXIS_PROP, EnumAxis.Y));
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.ACACIA.func_176839_a() - 4));
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4));
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState var2 = this.getDefaultState().withProperty(BlockNewLog.field_176300_b, BlockPlanks.EnumType.func_176837_a((meta & 0x3) + 4));
        switch (meta & 0xC) {
            case 0: {
                var2 = var2.withProperty(BlockLog.AXIS_PROP, EnumAxis.Y);
                break;
            }
            case 4: {
                var2 = var2.withProperty(BlockLog.AXIS_PROP, EnumAxis.X);
                break;
            }
            case 8: {
                var2 = var2.withProperty(BlockLog.AXIS_PROP, EnumAxis.Z);
                break;
            }
            default: {
                var2 = var2.withProperty(BlockLog.AXIS_PROP, EnumAxis.NONE);
                break;
            }
        }
        return var2;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((BlockPlanks.EnumType)state.getValue(BlockNewLog.field_176300_b)).func_176839_a() - 4;
        switch (SwitchEnumAxis.field_180191_a[((EnumAxis)state.getValue(BlockLog.AXIS_PROP)).ordinal()]) {
            case 1: {
                var3 |= 0x4;
                break;
            }
            case 2: {
                var3 |= 0x8;
                break;
            }
            case 3: {
                var3 |= 0xC;
                break;
            }
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockNewLog.field_176300_b, BlockLog.AXIS_PROP });
    }
    
    @Override
    protected ItemStack createStackedBlock(final IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)state.getValue(BlockNewLog.field_176300_b)).func_176839_a() - 4);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return ((BlockPlanks.EnumType)state.getValue(BlockNewLog.field_176300_b)).func_176839_a() - 4;
    }
    
    static {
        field_176300_b = PropertyEnum.create("variant", BlockPlanks.EnumType.class, (Predicate)new Predicate() {
            private static final String __OBFID = "CL_00002089";
            
            public boolean func_180194_a(final BlockPlanks.EnumType p_180194_1_) {
                return p_180194_1_.func_176839_a() >= 4;
            }
            
            public boolean apply(final Object p_apply_1_) {
                return this.func_180194_a((BlockPlanks.EnumType)p_apply_1_);
            }
        });
    }
    
    static final class SwitchEnumAxis
    {
        static final int[] field_180191_a;
        private static final String __OBFID = "CL_00002088";
        
        static {
            field_180191_a = new int[EnumAxis.values().length];
            try {
                SwitchEnumAxis.field_180191_a[EnumAxis.X.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumAxis.field_180191_a[EnumAxis.Z.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumAxis.field_180191_a[EnumAxis.NONE.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
        }
    }
}
