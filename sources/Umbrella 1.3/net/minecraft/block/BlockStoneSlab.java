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

public abstract class BlockStoneSlab
extends BlockSlab {
    public static final PropertyBool field_176555_b = PropertyBool.create("seamless");
    public static final PropertyEnum field_176556_M = PropertyEnum.create("variant", EnumType.class);
    private static final String __OBFID = "CL_00000320";

    public BlockStoneSlab() {
        super(Material.rock);
        IBlockState var1 = this.blockState.getBaseState();
        var1 = this.isDouble() ? var1.withProperty(field_176555_b, Boolean.valueOf(false)) : var1.withProperty(HALF_PROP, (Comparable)((Object)BlockSlab.EnumBlockHalf.BOTTOM));
        this.setDefaultState(var1.withProperty(field_176556_M, (Comparable)((Object)EnumType.STONE)));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.stone_slab);
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock(Blocks.stone_slab);
    }

    @Override
    public String getFullSlabName(int p_150002_1_) {
        return String.valueOf(super.getUnlocalizedName()) + "." + EnumType.func_176625_a(p_150002_1_).func_176627_c();
    }

    @Override
    public IProperty func_176551_l() {
        return field_176556_M;
    }

    @Override
    public Object func_176553_a(ItemStack p_176553_1_) {
        return EnumType.func_176625_a(p_176553_1_.getMetadata() & 7);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        if (itemIn != Item.getItemFromBlock(Blocks.double_stone_slab)) {
            for (EnumType var7 : EnumType.values()) {
                if (var7 == EnumType.WOOD) continue;
                list.add(new ItemStack(itemIn, 1, var7.func_176624_a()));
            }
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState var2 = this.getDefaultState().withProperty(field_176556_M, (Comparable)((Object)EnumType.func_176625_a(meta & 7)));
        var2 = this.isDouble() ? var2.withProperty(field_176555_b, Boolean.valueOf((meta & 8) != 0)) : var2.withProperty(HALF_PROP, (Comparable)((Object)((meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP)));
        return var2;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        int var3 = var2 | ((EnumType)((Object)state.getValue(field_176556_M))).func_176624_a();
        if (this.isDouble()) {
            if (((Boolean)state.getValue(field_176555_b)).booleanValue()) {
                var3 |= 8;
            }
        } else if (state.getValue(HALF_PROP) == BlockSlab.EnumBlockHalf.TOP) {
            var3 |= 8;
        }
        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return this.isDouble() ? new BlockState(this, field_176555_b, field_176556_M) : new BlockState(this, HALF_PROP, field_176556_M);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumType)((Object)state.getValue(field_176556_M))).func_176624_a();
    }

    public static enum EnumType implements IStringSerializable
    {
        STONE("STONE", 0, 0, "stone"),
        SAND("SAND", 1, 1, "sandstone", "sand"),
        WOOD("WOOD", 2, 2, "wood_old", "wood"),
        COBBLESTONE("COBBLESTONE", 3, 3, "cobblestone", "cobble"),
        BRICK("BRICK", 4, 4, "brick"),
        SMOOTHBRICK("SMOOTHBRICK", 5, 5, "stone_brick", "smoothStoneBrick"),
        NETHERBRICK("NETHERBRICK", 6, 6, "nether_brick", "netherBrick"),
        QUARTZ("QUARTZ", 7, 7, "quartz");

        private static final EnumType[] field_176640_i;
        private final int field_176637_j;
        private final String field_176638_k;
        private final String field_176635_l;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002056";

        static {
            field_176640_i = new EnumType[EnumType.values().length];
            $VALUES = new EnumType[]{STONE, SAND, WOOD, COBBLESTONE, BRICK, SMOOTHBRICK, NETHERBRICK, QUARTZ};
            EnumType[] var0 = EnumType.values();
            int var1 = var0.length;
            for (int var2 = 0; var2 < var1; ++var2) {
                EnumType var3;
                EnumType.field_176640_i[var3.func_176624_a()] = var3 = var0[var2];
            }
        }

        private EnumType(String p_i45677_1_, int p_i45677_2_, int p_i45677_3_, String p_i45677_4_) {
            this(p_i45677_1_, p_i45677_2_, p_i45677_3_, p_i45677_4_, p_i45677_4_);
        }

        private EnumType(String p_i45678_1_, int p_i45678_2_, int p_i45678_3_, String p_i45678_4_, String p_i45678_5_) {
            this.field_176637_j = p_i45678_3_;
            this.field_176638_k = p_i45678_4_;
            this.field_176635_l = p_i45678_5_;
        }

        public int func_176624_a() {
            return this.field_176637_j;
        }

        public String toString() {
            return this.field_176638_k;
        }

        public static EnumType func_176625_a(int p_176625_0_) {
            if (p_176625_0_ < 0 || p_176625_0_ >= field_176640_i.length) {
                p_176625_0_ = 0;
            }
            return field_176640_i[p_176625_0_];
        }

        @Override
        public String getName() {
            return this.field_176638_k;
        }

        public String func_176627_c() {
            return this.field_176635_l;
        }
    }
}

