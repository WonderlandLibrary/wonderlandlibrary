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

public class BlockStoneBrick
extends Block {
    public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", EnumType.class);
    public static final int DEFAULT_META = EnumType.DEFAULT.getMetaFromState();
    public static final int MOSSY_META = EnumType.MOSSY.getMetaFromState();
    public static final int CRACKED_META = EnumType.CRACKED.getMetaFromState();
    public static final int CHISELED_META = EnumType.CHISELED.getMetaFromState();
    private static final String __OBFID = "CL_00000318";

    public BlockStoneBrick() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP, (Comparable)((Object)EnumType.DEFAULT)));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumType)((Object)state.getValue(VARIANT_PROP))).getMetaFromState();
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (EnumType var7 : EnumType.values()) {
            list.add(new ItemStack(itemIn, 1, var7.getMetaFromState()));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT_PROP, (Comparable)((Object)EnumType.getStateFromMeta(meta)));
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
        DEFAULT("DEFAULT", 0, 0, "stonebrick", "default"),
        MOSSY("MOSSY", 1, 1, "mossy_stonebrick", "mossy"),
        CRACKED("CRACKED", 2, 2, "cracked_stonebrick", "cracked"),
        CHISELED("CHISELED", 3, 3, "chiseled_stonebrick", "chiseled");

        private static final EnumType[] TYPES_ARRAY;
        private final int field_176615_f;
        private final String field_176616_g;
        private final String field_176622_h;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002057";

        static {
            TYPES_ARRAY = new EnumType[EnumType.values().length];
            $VALUES = new EnumType[]{DEFAULT, MOSSY, CRACKED, CHISELED};
            EnumType[] var0 = EnumType.values();
            int var1 = var0.length;
            for (int var2 = 0; var2 < var1; ++var2) {
                EnumType var3;
                EnumType.TYPES_ARRAY[var3.getMetaFromState()] = var3 = var0[var2];
            }
        }

        private EnumType(String p_i45679_1_, int p_i45679_2_, int p_i45679_3_, String p_i45679_4_, String p_i45679_5_) {
            this.field_176615_f = p_i45679_3_;
            this.field_176616_g = p_i45679_4_;
            this.field_176622_h = p_i45679_5_;
        }

        public int getMetaFromState() {
            return this.field_176615_f;
        }

        public String toString() {
            return this.field_176616_g;
        }

        public static EnumType getStateFromMeta(int p_176613_0_) {
            if (p_176613_0_ < 0 || p_176613_0_ >= TYPES_ARRAY.length) {
                p_176613_0_ = 0;
            }
            return TYPES_ARRAY[p_176613_0_];
        }

        @Override
        public String getName() {
            return this.field_176616_g;
        }

        public String getVariantName() {
            return this.field_176622_h;
        }
    }
}

