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

public class BlockPlanks
extends Block {
    public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", EnumType.class);
    private static final String __OBFID = "CL_00002082";

    public BlockPlanks() {
        super(Material.wood);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP, (Comparable)((Object)EnumType.OAK)));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumType)((Object)state.getValue(VARIANT_PROP))).func_176839_a();
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (EnumType var7 : EnumType.values()) {
            list.add(new ItemStack(itemIn, 1, var7.func_176839_a()));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT_PROP, (Comparable)((Object)EnumType.func_176837_a(meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumType)((Object)state.getValue(VARIANT_PROP))).func_176839_a();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, VARIANT_PROP);
    }

    public static enum EnumType implements IStringSerializable
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

        static {
            field_176842_g = new EnumType[EnumType.values().length];
            $VALUES = new EnumType[]{OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK};
            EnumType[] var0 = EnumType.values();
            int var1 = var0.length;
            for (int var2 = 0; var2 < var1; ++var2) {
                EnumType var3;
                EnumType.field_176842_g[var3.func_176839_a()] = var3 = var0[var2];
            }
        }

        private EnumType(String p_i45695_1_, int p_i45695_2_, int p_i45695_3_, String p_i45695_4_) {
            this(p_i45695_1_, p_i45695_2_, p_i45695_3_, p_i45695_4_, p_i45695_4_);
        }

        private EnumType(String p_i45696_1_, int p_i45696_2_, int p_i45696_3_, String p_i45696_4_, String p_i45696_5_) {
            this.field_176850_h = p_i45696_3_;
            this.field_176851_i = p_i45696_4_;
            this.field_176848_j = p_i45696_5_;
        }

        public int func_176839_a() {
            return this.field_176850_h;
        }

        public String toString() {
            return this.field_176851_i;
        }

        public static EnumType func_176837_a(int p_176837_0_) {
            if (p_176837_0_ < 0 || p_176837_0_ >= field_176842_g.length) {
                p_176837_0_ = 0;
            }
            return field_176842_g[p_176837_0_];
        }

        @Override
        public String getName() {
            return this.field_176851_i;
        }

        public String func_176840_c() {
            return this.field_176848_j;
        }
    }
}

