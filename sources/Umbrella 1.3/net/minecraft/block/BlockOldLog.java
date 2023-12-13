/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockOldLog
extends BlockLog {
    public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate(){
        private static final String __OBFID = "CL_00002084";

        public boolean func_180200_a(BlockPlanks.EnumType p_180200_1_) {
            return p_180200_1_.func_176839_a() < 4;
        }

        public boolean apply(Object p_apply_1_) {
            return this.func_180200_a((BlockPlanks.EnumType)p_apply_1_);
        }
    });
    private static final String __OBFID = "CL_00000281";

    public BlockOldLog() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP, (Comparable)((Object)BlockPlanks.EnumType.OAK)).withProperty(AXIS_PROP, (Comparable)((Object)BlockLog.EnumAxis.Y)));
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.OAK.func_176839_a()));
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.SPRUCE.func_176839_a()));
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.BIRCH.func_176839_a()));
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.JUNGLE.func_176839_a()));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState var2 = this.getDefaultState().withProperty(VARIANT_PROP, (Comparable)((Object)BlockPlanks.EnumType.func_176837_a((meta & 3) % 4)));
        switch (meta & 0xC) {
            case 0: {
                var2 = var2.withProperty(AXIS_PROP, (Comparable)((Object)BlockLog.EnumAxis.Y));
                break;
            }
            case 4: {
                var2 = var2.withProperty(AXIS_PROP, (Comparable)((Object)BlockLog.EnumAxis.X));
                break;
            }
            case 8: {
                var2 = var2.withProperty(AXIS_PROP, (Comparable)((Object)BlockLog.EnumAxis.Z));
                break;
            }
            default: {
                var2 = var2.withProperty(AXIS_PROP, (Comparable)((Object)BlockLog.EnumAxis.NONE));
            }
        }
        return var2;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        int var3 = var2 | ((BlockPlanks.EnumType)((Object)state.getValue(VARIANT_PROP))).func_176839_a();
        switch (SwitchEnumAxis.field_180203_a[((BlockLog.EnumAxis)((Object)state.getValue(AXIS_PROP))).ordinal()]) {
            case 1: {
                var3 |= 4;
                break;
            }
            case 2: {
                var3 |= 8;
                break;
            }
            case 3: {
                var3 |= 0xC;
            }
        }
        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, VARIANT_PROP, AXIS_PROP);
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)((Object)state.getValue(VARIANT_PROP))).func_176839_a());
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((BlockPlanks.EnumType)((Object)state.getValue(VARIANT_PROP))).func_176839_a();
    }

    static final class SwitchEnumAxis {
        static final int[] field_180203_a = new int[BlockLog.EnumAxis.values().length];
        private static final String __OBFID = "CL_00002083";

        static {
            try {
                SwitchEnumAxis.field_180203_a[BlockLog.EnumAxis.X.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumAxis.field_180203_a[BlockLog.EnumAxis.Z.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumAxis.field_180203_a[BlockLog.EnumAxis.NONE.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumAxis() {
        }
    }
}

