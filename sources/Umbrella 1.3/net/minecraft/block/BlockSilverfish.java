/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public class BlockSilverfish
extends Block {
    public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", EnumType.class);
    private static final String __OBFID = "CL_00000271";

    public BlockSilverfish() {
        super(Material.clay);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP, (Comparable)((Object)EnumType.STONE)));
        this.setHardness(0.0f);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    public static boolean func_176377_d(IBlockState p_176377_0_) {
        Block var1 = p_176377_0_.getBlock();
        return p_176377_0_ == Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT_PROP, (Comparable)((Object)BlockStone.EnumType.STONE)) || var1 == Blocks.cobblestone || var1 == Blocks.stonebrick;
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState state) {
        switch (SwitchEnumType.field_180178_a[((EnumType)((Object)state.getValue(VARIANT_PROP))).ordinal()]) {
            case 1: {
                return new ItemStack(Blocks.cobblestone);
            }
            case 2: {
                return new ItemStack(Blocks.stonebrick);
            }
            case 3: {
                return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.MOSSY.getMetaFromState());
            }
            case 4: {
                return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CRACKED.getMetaFromState());
            }
            case 5: {
                return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CHISELED.getMetaFromState());
            }
        }
        return new ItemStack(Blocks.stone);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!worldIn.isRemote && worldIn.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
            EntitySilverfish var6 = new EntitySilverfish(worldIn);
            var6.setLocationAndAngles((double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, 0.0f, 0.0f);
            worldIn.spawnEntityInWorld(var6);
            var6.spawnExplosionParticle();
        }
    }

    @Override
    public int getDamageValue(World worldIn, BlockPos pos) {
        IBlockState var3 = worldIn.getBlockState(pos);
        return var3.getBlock().getMetaFromState(var3);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (EnumType var7 : EnumType.values()) {
            list.add(new ItemStack(itemIn, 1, var7.func_176881_a()));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT_PROP, (Comparable)((Object)EnumType.func_176879_a(meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumType)((Object)state.getValue(VARIANT_PROP))).func_176881_a();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, VARIANT_PROP);
    }

    public static enum EnumType implements IStringSerializable
    {
        STONE("STONE", 0, 0, "stone", null){
            private static final String __OBFID = "CL_00002097";

            @Override
            public IBlockState func_176883_d() {
                return Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT_PROP, (Comparable)((Object)BlockStone.EnumType.STONE));
            }
        }
        ,
        COBBLESTONE("COBBLESTONE", 1, 1, "cobblestone", "cobble", null){
            private static final String __OBFID = "CL_00002096";

            @Override
            public IBlockState func_176883_d() {
                return Blocks.cobblestone.getDefaultState();
            }
        }
        ,
        STONEBRICK("STONEBRICK", 2, 2, "stone_brick", "brick", null){
            private static final String __OBFID = "CL_00002095";

            @Override
            public IBlockState func_176883_d() {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT_PROP, (Comparable)((Object)BlockStoneBrick.EnumType.DEFAULT));
            }
        }
        ,
        MOSSY_STONEBRICK("MOSSY_STONEBRICK", 3, 3, "mossy_brick", "mossybrick", null){
            private static final String __OBFID = "CL_00002094";

            @Override
            public IBlockState func_176883_d() {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT_PROP, (Comparable)((Object)BlockStoneBrick.EnumType.MOSSY));
            }
        }
        ,
        CRACKED_STONEBRICK("CRACKED_STONEBRICK", 4, 4, "cracked_brick", "crackedbrick", null){
            private static final String __OBFID = "CL_00002093";

            @Override
            public IBlockState func_176883_d() {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT_PROP, (Comparable)((Object)BlockStoneBrick.EnumType.CRACKED));
            }
        }
        ,
        CHISELED_STONEBRICK("CHISELED_STONEBRICK", 5, 5, "chiseled_brick", "chiseledbrick", null){
            private static final String __OBFID = "CL_00002092";

            @Override
            public IBlockState func_176883_d() {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT_PROP, (Comparable)((Object)BlockStoneBrick.EnumType.CHISELED));
            }
        };

        private static final EnumType[] field_176885_g;
        private final int field_176893_h;
        private final String field_176894_i;
        private final String field_176891_j;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002098";

        static {
            field_176885_g = new EnumType[EnumType.values().length];
            $VALUES = new EnumType[]{STONE, COBBLESTONE, STONEBRICK, MOSSY_STONEBRICK, CRACKED_STONEBRICK, CHISELED_STONEBRICK};
            EnumType[] var0 = EnumType.values();
            int var1 = var0.length;
            for (int var2 = 0; var2 < var1; ++var2) {
                EnumType var3;
                EnumType.field_176885_g[var3.func_176881_a()] = var3 = var0[var2];
            }
        }

        private EnumType(String p_i45704_1_, int p_i45704_2_, int p_i45704_3_, String p_i45704_4_) {
            this(p_i45704_1_, p_i45704_2_, p_i45704_3_, p_i45704_4_, p_i45704_4_);
        }

        private EnumType(String p_i45705_1_, int p_i45705_2_, int p_i45705_3_, String p_i45705_4_, String p_i45705_5_) {
            this.field_176893_h = p_i45705_3_;
            this.field_176894_i = p_i45705_4_;
            this.field_176891_j = p_i45705_5_;
        }

        public int func_176881_a() {
            return this.field_176893_h;
        }

        public String toString() {
            return this.field_176894_i;
        }

        public static EnumType func_176879_a(int p_176879_0_) {
            if (p_176879_0_ < 0 || p_176879_0_ >= field_176885_g.length) {
                p_176879_0_ = 0;
            }
            return field_176885_g[p_176879_0_];
        }

        @Override
        public String getName() {
            return this.field_176894_i;
        }

        public String func_176882_c() {
            return this.field_176891_j;
        }

        public abstract IBlockState func_176883_d();

        public static EnumType func_176878_a(IBlockState p_176878_0_) {
            for (EnumType var4 : EnumType.values()) {
                if (p_176878_0_ != var4.func_176883_d()) continue;
                return var4;
            }
            return STONE;
        }

        private EnumType(String p_i45706_1_, int p_i45706_2_, int p_i45706_3_, String p_i45706_4_, SwitchEnumType p_i45706_5_) {
            this(p_i45706_1_, p_i45706_2_, p_i45706_3_, p_i45706_4_);
        }

        private EnumType(String p_i45707_1_, int p_i45707_2_, int p_i45707_3_, String p_i45707_4_, String p_i45707_5_, SwitchEnumType p_i45707_6_) {
            this(p_i45707_1_, p_i45707_2_, p_i45707_3_, p_i45707_4_, p_i45707_5_);
        }
    }

    static final class SwitchEnumType {
        static final int[] field_180178_a = new int[EnumType.values().length];
        private static final String __OBFID = "CL_00002099";

        static {
            try {
                SwitchEnumType.field_180178_a[EnumType.COBBLESTONE.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumType.field_180178_a[EnumType.STONEBRICK.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumType.field_180178_a[EnumType.MOSSY_STONEBRICK.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumType.field_180178_a[EnumType.CRACKED_STONEBRICK.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumType.field_180178_a[EnumType.CHISELED_STONEBRICK.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumType() {
        }
    }
}

