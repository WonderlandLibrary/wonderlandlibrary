/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public class BlockQuartz
extends Block {
    public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", EnumType.class);
    private static final String __OBFID = "CL_00000292";

    public BlockQuartz() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP, (Comparable)((Object)EnumType.DEFAULT)));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (meta == EnumType.LINES_Y.getMetaFromState()) {
            switch (SwitchAxis.field_180101_a[facing.getAxis().ordinal()]) {
                case 1: {
                    return this.getDefaultState().withProperty(VARIANT_PROP, (Comparable)((Object)EnumType.LINES_Z));
                }
                case 2: {
                    return this.getDefaultState().withProperty(VARIANT_PROP, (Comparable)((Object)EnumType.LINES_X));
                }
            }
            return this.getDefaultState().withProperty(VARIANT_PROP, (Comparable)((Object)EnumType.LINES_Y));
        }
        return meta == EnumType.CHISELED.getMetaFromState() ? this.getDefaultState().withProperty(VARIANT_PROP, (Comparable)((Object)EnumType.CHISELED)) : this.getDefaultState().withProperty(VARIANT_PROP, (Comparable)((Object)EnumType.DEFAULT));
    }

    @Override
    public int damageDropped(IBlockState state) {
        EnumType var2 = (EnumType)((Object)state.getValue(VARIANT_PROP));
        return var2 != EnumType.LINES_X && var2 != EnumType.LINES_Z ? var2.getMetaFromState() : EnumType.LINES_Y.getMetaFromState();
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState state) {
        EnumType var2 = (EnumType)((Object)state.getValue(VARIANT_PROP));
        return var2 != EnumType.LINES_X && var2 != EnumType.LINES_Z ? super.createStackedBlock(state) : new ItemStack(Item.getItemFromBlock(this), 1, EnumType.LINES_Y.getMetaFromState());
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemIn, 1, EnumType.DEFAULT.getMetaFromState()));
        list.add(new ItemStack(itemIn, 1, EnumType.CHISELED.getMetaFromState()));
        list.add(new ItemStack(itemIn, 1, EnumType.LINES_Y.getMetaFromState()));
    }

    @Override
    public MapColor getMapColor(IBlockState state) {
        return MapColor.quartzColor;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT_PROP, (Comparable)((Object)EnumType.func_176794_a(meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumType)((Object)state.getValue(VARIANT_PROP))).getMetaFromState();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, VARIANT_PROP);
    }

    public static enum EnumType implements IStringSerializable
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

        static {
            TYPES_ARRAY = new EnumType[EnumType.values().length];
            $VALUES = new EnumType[]{DEFAULT, CHISELED, LINES_Y, LINES_X, LINES_Z};
            EnumType[] var0 = EnumType.values();
            int var1 = var0.length;
            for (int var2 = 0; var2 < var1; ++var2) {
                EnumType var3;
                EnumType.TYPES_ARRAY[var3.getMetaFromState()] = var3 = var0[var2];
            }
        }

        private EnumType(String p_i45691_1_, int p_i45691_2_, int p_i45691_3_, String p_i45691_4_, String p_i45691_5_) {
            this.field_176798_g = p_i45691_3_;
            this.field_176805_h = p_i45691_4_;
            this.field_176806_i = p_i45691_5_;
        }

        public int getMetaFromState() {
            return this.field_176798_g;
        }

        public String toString() {
            return this.field_176806_i;
        }

        public static EnumType func_176794_a(int p_176794_0_) {
            if (p_176794_0_ < 0 || p_176794_0_ >= TYPES_ARRAY.length) {
                p_176794_0_ = 0;
            }
            return TYPES_ARRAY[p_176794_0_];
        }

        @Override
        public String getName() {
            return this.field_176805_h;
        }
    }

    static final class SwitchAxis {
        static final int[] field_180101_a = new int[EnumFacing.Axis.values().length];
        private static final String __OBFID = "CL_00002075";

        static {
            try {
                SwitchAxis.field_180101_a[EnumFacing.Axis.Z.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchAxis.field_180101_a[EnumFacing.Axis.X.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchAxis.field_180101_a[EnumFacing.Axis.Y.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchAxis() {
        }
    }
}

