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

public class BlockSandStone
extends Block {
    public static final PropertyEnum field_176297_a = PropertyEnum.create("type", EnumType.class);
    private static final String __OBFID = "CL_00000304";

    public BlockSandStone() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176297_a, (Comparable)((Object)EnumType.DEFAULT)));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumType)((Object)state.getValue(field_176297_a))).func_176675_a();
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (EnumType var7 : EnumType.values()) {
            list.add(new ItemStack(itemIn, 1, var7.func_176675_a()));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176297_a, (Comparable)((Object)EnumType.func_176673_a(meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumType)((Object)state.getValue(field_176297_a))).func_176675_a();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176297_a);
    }

    public static enum EnumType implements IStringSerializable
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

        static {
            field_176679_d = new EnumType[EnumType.values().length];
            $VALUES = new EnumType[]{DEFAULT, CHISELED, SMOOTH};
            EnumType[] var0 = EnumType.values();
            int var1 = var0.length;
            for (int var2 = 0; var2 < var1; ++var2) {
                EnumType var3;
                EnumType.field_176679_d[var3.func_176675_a()] = var3 = var0[var2];
            }
        }

        private EnumType(String p_i45686_1_, int p_i45686_2_, int p_i45686_3_, String p_i45686_4_, String p_i45686_5_) {
            this.field_176680_e = p_i45686_3_;
            this.field_176677_f = p_i45686_4_;
            this.field_176678_g = p_i45686_5_;
        }

        public int func_176675_a() {
            return this.field_176680_e;
        }

        public String toString() {
            return this.field_176677_f;
        }

        public static EnumType func_176673_a(int p_176673_0_) {
            if (p_176673_0_ < 0 || p_176673_0_ >= field_176679_d.length) {
                p_176673_0_ = 0;
            }
            return field_176679_d[p_176673_0_];
        }

        @Override
        public String getName() {
            return this.field_176677_f;
        }

        public String func_176676_c() {
            return this.field_176678_g;
        }
    }
}

