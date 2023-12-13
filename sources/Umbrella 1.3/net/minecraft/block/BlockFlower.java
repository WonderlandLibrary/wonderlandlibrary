/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Collections2
 *  com.google.common.collect.Lists
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public abstract class BlockFlower
extends BlockBush {
    protected PropertyEnum field_176496_a;
    private static final String __OBFID = "CL_00000246";

    protected BlockFlower() {
        super(Material.plants);
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.func_176494_l(), (Comparable)((Object)(this.func_176495_j() == EnumFlowerColor.RED ? EnumFlowerType.POPPY : EnumFlowerType.DANDELION))));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumFlowerType)((Object)state.getValue(this.func_176494_l()))).func_176968_b();
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (EnumFlowerType var7 : EnumFlowerType.func_176966_a(this.func_176495_j())) {
            list.add(new ItemStack(itemIn, 1, var7.func_176968_b()));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(this.func_176494_l(), (Comparable)((Object)EnumFlowerType.func_176967_a(this.func_176495_j(), meta)));
    }

    public abstract EnumFlowerColor func_176495_j();

    public IProperty func_176494_l() {
        if (this.field_176496_a == null) {
            this.field_176496_a = PropertyEnum.create("type", EnumFlowerType.class, new Predicate(){
                private static final String __OBFID = "CL_00002120";

                public boolean func_180354_a(EnumFlowerType p_180354_1_) {
                    return p_180354_1_.func_176964_a() == BlockFlower.this.func_176495_j();
                }

                public boolean apply(Object p_apply_1_) {
                    return this.func_180354_a((EnumFlowerType)p_apply_1_);
                }
            });
        }
        return this.field_176496_a;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFlowerType)((Object)state.getValue(this.func_176494_l()))).func_176968_b();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, this.func_176494_l());
    }

    @Override
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XZ;
    }

    public static enum EnumFlowerColor {
        YELLOW("YELLOW", 0),
        RED("RED", 1);

        private static final EnumFlowerColor[] $VALUES;
        private static final String __OBFID = "CL_00002117";

        static {
            $VALUES = new EnumFlowerColor[]{YELLOW, RED};
        }

        private EnumFlowerColor(String p_i45716_1_, int p_i45716_2_) {
        }

        public BlockFlower func_180346_a() {
            return this == YELLOW ? Blocks.yellow_flower : Blocks.red_flower;
        }
    }

    public static enum EnumFlowerType implements IStringSerializable
    {
        DANDELION("DANDELION", 0, EnumFlowerColor.YELLOW, 0, "dandelion"),
        POPPY("POPPY", 1, EnumFlowerColor.RED, 0, "poppy"),
        BLUE_ORCHID("BLUE_ORCHID", 2, EnumFlowerColor.RED, 1, "blue_orchid", "blueOrchid"),
        ALLIUM("ALLIUM", 3, EnumFlowerColor.RED, 2, "allium"),
        HOUSTONIA("HOUSTONIA", 4, EnumFlowerColor.RED, 3, "houstonia"),
        RED_TULIP("RED_TULIP", 5, EnumFlowerColor.RED, 4, "red_tulip", "tulipRed"),
        ORANGE_TULIP("ORANGE_TULIP", 6, EnumFlowerColor.RED, 5, "orange_tulip", "tulipOrange"),
        WHITE_TULIP("WHITE_TULIP", 7, EnumFlowerColor.RED, 6, "white_tulip", "tulipWhite"),
        PINK_TULIP("PINK_TULIP", 8, EnumFlowerColor.RED, 7, "pink_tulip", "tulipPink"),
        OXEYE_DAISY("OXEYE_DAISY", 9, EnumFlowerColor.RED, 8, "oxeye_daisy", "oxeyeDaisy");

        private static final EnumFlowerType[][] field_176981_k;
        private final EnumFlowerColor field_176978_l;
        private final int field_176979_m;
        private final String field_176976_n;
        private final String field_176977_o;
        private static final EnumFlowerType[] $VALUES;
        private static final String __OBFID = "CL_00002119";

        static {
            field_176981_k = new EnumFlowerType[EnumFlowerColor.values().length][];
            $VALUES = new EnumFlowerType[]{DANDELION, POPPY, BLUE_ORCHID, ALLIUM, HOUSTONIA, RED_TULIP, ORANGE_TULIP, WHITE_TULIP, PINK_TULIP, OXEYE_DAISY};
            for (final EnumFlowerColor var3 : EnumFlowerColor.values()) {
                Collection var4 = Collections2.filter((Collection)Lists.newArrayList((Object[])EnumFlowerType.values()), (Predicate)new Predicate(){
                    private static final String __OBFID = "CL_00002118";

                    public boolean func_180350_a(EnumFlowerType p_180350_1_) {
                        return p_180350_1_.func_176964_a() == var3;
                    }

                    public boolean apply(Object p_apply_1_) {
                        return this.func_180350_a((EnumFlowerType)p_apply_1_);
                    }
                });
                EnumFlowerType.field_176981_k[var3.ordinal()] = var4.toArray(new EnumFlowerType[var4.size()]);
            }
        }

        private EnumFlowerType(String p_i45718_1_, int p_i45718_2_, EnumFlowerColor p_i45718_3_, int p_i45718_4_, String p_i45718_5_) {
            this(p_i45718_1_, p_i45718_2_, p_i45718_3_, p_i45718_4_, p_i45718_5_, p_i45718_5_);
        }

        private EnumFlowerType(String p_i45719_1_, int p_i45719_2_, EnumFlowerColor p_i45719_3_, int p_i45719_4_, String p_i45719_5_, String p_i45719_6_) {
            this.field_176978_l = p_i45719_3_;
            this.field_176979_m = p_i45719_4_;
            this.field_176976_n = p_i45719_5_;
            this.field_176977_o = p_i45719_6_;
        }

        public EnumFlowerColor func_176964_a() {
            return this.field_176978_l;
        }

        public int func_176968_b() {
            return this.field_176979_m;
        }

        public static EnumFlowerType func_176967_a(EnumFlowerColor p_176967_0_, int p_176967_1_) {
            EnumFlowerType[] var2 = field_176981_k[p_176967_0_.ordinal()];
            if (p_176967_1_ < 0 || p_176967_1_ >= var2.length) {
                p_176967_1_ = 0;
            }
            return var2[p_176967_1_];
        }

        public static EnumFlowerType[] func_176966_a(EnumFlowerColor p_176966_0_) {
            return field_176981_k[p_176966_0_.ordinal()];
        }

        public String toString() {
            return this.field_176976_n;
        }

        @Override
        public String getName() {
            return this.field_176976_n;
        }

        public String func_176963_d() {
            return this.field_176977_o;
        }
    }
}

