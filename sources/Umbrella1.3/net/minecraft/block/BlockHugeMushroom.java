/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public class BlockHugeMushroom
extends Block {
    public static final PropertyEnum field_176380_a = PropertyEnum.create("variant", EnumType.class);
    private final Block field_176379_b;
    private static final String __OBFID = "CL_00000258";

    public BlockHugeMushroom(Material p_i45711_1_, Block p_i45711_2_) {
        super(p_i45711_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176380_a, (Comparable)((Object)EnumType.ALL_OUTSIDE)));
        this.field_176379_b = p_i45711_2_;
    }

    @Override
    public int quantityDropped(Random random) {
        return Math.max(0, random.nextInt(10) - 7);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this.field_176379_b);
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock(this.field_176379_b);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176380_a, (Comparable)((Object)EnumType.func_176895_a(meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumType)((Object)state.getValue(field_176380_a))).func_176896_a();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176380_a);
    }

    public static enum EnumType implements IStringSerializable
    {
        NORTH_WEST("NORTH_WEST", 0, 1, "north_west"),
        NORTH("NORTH", 1, 2, "north"),
        NORTH_EAST("NORTH_EAST", 2, 3, "north_east"),
        WEST("WEST", 3, 4, "west"),
        CENTER("CENTER", 4, 5, "center"),
        EAST("EAST", 5, 6, "east"),
        SOUTH_WEST("SOUTH_WEST", 6, 7, "south_west"),
        SOUTH("SOUTH", 7, 8, "south"),
        SOUTH_EAST("SOUTH_EAST", 8, 9, "south_east"),
        STEM("STEM", 9, 10, "stem"),
        ALL_INSIDE("ALL_INSIDE", 10, 0, "all_inside"),
        ALL_OUTSIDE("ALL_OUTSIDE", 11, 14, "all_outside"),
        ALL_STEM("ALL_STEM", 12, 15, "all_stem");

        private static final EnumType[] field_176905_n;
        private final int field_176906_o;
        private final String field_176914_p;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002105";

        static {
            field_176905_n = new EnumType[16];
            $VALUES = new EnumType[]{NORTH_WEST, NORTH, NORTH_EAST, WEST, CENTER, EAST, SOUTH_WEST, SOUTH, SOUTH_EAST, STEM, ALL_INSIDE, ALL_OUTSIDE, ALL_STEM};
            EnumType[] var0 = EnumType.values();
            int var1 = var0.length;
            for (int var2 = 0; var2 < var1; ++var2) {
                EnumType var3;
                EnumType.field_176905_n[var3.func_176896_a()] = var3 = var0[var2];
            }
        }

        private EnumType(String p_i45710_1_, int p_i45710_2_, int p_i45710_3_, String p_i45710_4_) {
            this.field_176906_o = p_i45710_3_;
            this.field_176914_p = p_i45710_4_;
        }

        public int func_176896_a() {
            return this.field_176906_o;
        }

        public String toString() {
            return this.field_176914_p;
        }

        public static EnumType func_176895_a(int p_176895_0_) {
            EnumType var1;
            if (p_176895_0_ < 0 || p_176895_0_ >= field_176905_n.length) {
                p_176895_0_ = 0;
            }
            return (var1 = field_176905_n[p_176895_0_]) == null ? field_176905_n[0] : var1;
        }

        @Override
        public String getName() {
            return this.field_176914_p;
        }
    }
}

