/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public abstract class BlockStoneSlabNew
extends BlockSlab {
    public static final PropertyBool field_176558_b = PropertyBool.create("seamless");
    public static final PropertyEnum field_176559_M = PropertyEnum.create("variant", EnumType.class);
    private static final String __OBFID = "CL_00002087";

    public BlockStoneSlabNew() {
        super(Material.rock);
        IBlockState var1 = this.blockState.getBaseState();
        var1 = this.isDouble() ? var1.withProperty(field_176558_b, Boolean.valueOf(false)) : var1.withProperty(HALF_PROP, (Comparable)((Object)BlockSlab.EnumBlockHalf.BOTTOM));
        this.setDefaultState(var1.withProperty(field_176559_M, (Comparable)((Object)EnumType.RED_SANDSTONE)));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.stone_slab2);
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock(Blocks.stone_slab2);
    }

    @Override
    public String getFullSlabName(int p_150002_1_) {
        return String.valueOf(super.getUnlocalizedName()) + "." + EnumType.func_176916_a(p_150002_1_).func_176918_c();
    }

    @Override
    public IProperty func_176551_l() {
        return field_176559_M;
    }

    @Override
    public Object func_176553_a(ItemStack p_176553_1_) {
        return EnumType.func_176916_a(p_176553_1_.getMetadata() & 7);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        if (itemIn != Item.getItemFromBlock(Blocks.double_stone_slab2)) {
            for (EnumType var7 : EnumType.values()) {
                list.add(new ItemStack(itemIn, 1, var7.func_176915_a()));
            }
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState var2 = this.getDefaultState().withProperty(field_176559_M, (Comparable)((Object)EnumType.func_176916_a(meta & 7)));
        var2 = this.isDouble() ? var2.withProperty(field_176558_b, Boolean.valueOf((meta & 8) != 0)) : var2.withProperty(HALF_PROP, (Comparable)((Object)((meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP)));
        return var2;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        int var3 = var2 | ((EnumType)((Object)state.getValue(field_176559_M))).func_176915_a();
        if (this.isDouble()) {
            if (((Boolean)state.getValue(field_176558_b)).booleanValue()) {
                var3 |= 8;
            }
        } else if (state.getValue(HALF_PROP) == BlockSlab.EnumBlockHalf.TOP) {
            var3 |= 8;
        }
        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return this.isDouble() ? new BlockState(this, field_176558_b, field_176559_M) : new BlockState(this, HALF_PROP, field_176559_M);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumType)((Object)state.getValue(field_176559_M))).func_176915_a();
    }

    public static enum EnumType implements IStringSerializable
    {
        RED_SANDSTONE("RED_SANDSTONE", 0, 0, "red_sandstone");

        private static final EnumType[] field_176921_b;
        private final int field_176922_c;
        private final String field_176919_d;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002086";

        static {
            field_176921_b = new EnumType[EnumType.values().length];
            $VALUES = new EnumType[]{RED_SANDSTONE};
            EnumType[] var0 = EnumType.values();
            int var1 = var0.length;
            for (int var2 = 0; var2 < var1; ++var2) {
                EnumType var3;
                EnumType.field_176921_b[var3.func_176915_a()] = var3 = var0[var2];
            }
        }

        private EnumType(String p_i45697_1_, int p_i45697_2_, int p_i45697_3_, String p_i45697_4_) {
            this.field_176922_c = p_i45697_3_;
            this.field_176919_d = p_i45697_4_;
        }

        public int func_176915_a() {
            return this.field_176922_c;
        }

        public String toString() {
            return this.field_176919_d;
        }

        public static EnumType func_176916_a(int p_176916_0_) {
            if (p_176916_0_ < 0 || p_176916_0_ >= field_176921_b.length) {
                p_176916_0_ = 0;
            }
            return field_176921_b[p_176916_0_];
        }

        @Override
        public String getName() {
            return this.field_176919_d;
        }

        public String func_176918_c() {
            return this.field_176919_d;
        }
    }
}

