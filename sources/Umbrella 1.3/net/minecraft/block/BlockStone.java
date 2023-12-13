/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockStone
extends Block {
    public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", EnumType.class);
    private static final String __OBFID = "CL_00000317";

    public BlockStone() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP, (Comparable)((Object)EnumType.STONE)));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(VARIANT_PROP) == EnumType.STONE ? Item.getItemFromBlock(Blocks.cobblestone) : Item.getItemFromBlock(Blocks.stone);
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
        STONE("STONE", 0, 0, "stone"),
        GRANITE("GRANITE", 1, 1, "granite"),
        GRANITE_SMOOTH("GRANITE_SMOOTH", 2, 2, "smooth_granite", "graniteSmooth"),
        DIORITE("DIORITE", 3, 3, "diorite"),
        DIORITE_SMOOTH("DIORITE_SMOOTH", 4, 4, "smooth_diorite", "dioriteSmooth"),
        ANDESITE("ANDESITE", 5, 5, "andesite"),
        ANDESITE_SMOOTH("ANDESITE_SMOOTH", 6, 6, "smooth_andesite", "andesiteSmooth");

        private static final EnumType[] BLOCKSTATES;
        private final int meta;
        private final String name;
        private final String field_176654_k;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002058";

        static {
            BLOCKSTATES = new EnumType[EnumType.values().length];
            $VALUES = new EnumType[]{STONE, GRANITE, GRANITE_SMOOTH, DIORITE, DIORITE_SMOOTH, ANDESITE, ANDESITE_SMOOTH};
            EnumType[] var0 = EnumType.values();
            int var1 = var0.length;
            for (int var2 = 0; var2 < var1; ++var2) {
                EnumType var3;
                EnumType.BLOCKSTATES[var3.getMetaFromState()] = var3 = var0[var2];
            }
        }

        private EnumType(String p_i45680_1_, int p_i45680_2_, int p_i45680_3_, String p_i45680_4_) {
            this(p_i45680_1_, p_i45680_2_, p_i45680_3_, p_i45680_4_, p_i45680_4_);
        }

        private EnumType(String p_i45681_1_, int p_i45681_2_, int p_i45681_3_, String p_i45681_4_, String p_i45681_5_) {
            this.meta = p_i45681_3_;
            this.name = p_i45681_4_;
            this.field_176654_k = p_i45681_5_;
        }

        public int getMetaFromState() {
            return this.meta;
        }

        public String toString() {
            return this.name;
        }

        public static EnumType getStateFromMeta(int p_176643_0_) {
            if (p_176643_0_ < 0 || p_176643_0_ >= BLOCKSTATES.length) {
                p_176643_0_ = 0;
            }
            return BLOCKSTATES[p_176643_0_];
        }

        @Override
        public String getName() {
            return this.name;
        }

        public String func_176644_c() {
            return this.field_176654_k;
        }
    }
}

