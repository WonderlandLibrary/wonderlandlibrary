/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockRedSandstone
extends Block {
    public static final PropertyEnum TYPE = PropertyEnum.create("type", EnumType.class);
    private static final String __OBFID = "CL_00002072";

    public BlockRedSandstone() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, (Comparable)((Object)EnumType.DEFAULT)));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumType)((Object)state.getValue(TYPE))).getMetaFromState();
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (EnumType var7 : EnumType.values()) {
            list.add(new ItemStack(itemIn, 1, var7.getMetaFromState()));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, (Comparable)((Object)EnumType.func_176825_a(meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumType)((Object)state.getValue(TYPE))).getMetaFromState();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, TYPE);
    }

    public static enum EnumType implements IStringSerializable
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

        static {
            field_176831_d = new EnumType[EnumType.values().length];
            $VALUES = new EnumType[]{DEFAULT, CHISELED, SMOOTH};
            EnumType[] var0 = EnumType.values();
            int var1 = var0.length;
            for (int var2 = 0; var2 < var1; ++var2) {
                EnumType var3;
                EnumType.field_176831_d[var3.getMetaFromState()] = var3 = var0[var2];
            }
        }

        private EnumType(String p_i45690_1_, int p_i45690_2_, int p_i45690_3_, String p_i45690_4_, String p_i45690_5_) {
            this.field_176832_e = p_i45690_3_;
            this.field_176829_f = p_i45690_4_;
            this.field_176830_g = p_i45690_5_;
        }

        public int getMetaFromState() {
            return this.field_176832_e;
        }

        public String toString() {
            return this.field_176829_f;
        }

        public static EnumType func_176825_a(int p_176825_0_) {
            if (p_176825_0_ < 0 || p_176825_0_ >= field_176831_d.length) {
                p_176825_0_ = 0;
            }
            return field_176831_d[p_176825_0_];
        }

        @Override
        public String getName() {
            return this.field_176829_f;
        }

        public String func_176828_c() {
            return this.field_176830_g;
        }
    }
}

