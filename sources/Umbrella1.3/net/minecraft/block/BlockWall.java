/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWall
extends Block {
    public static final PropertyBool field_176256_a = PropertyBool.create("up");
    public static final PropertyBool field_176254_b = PropertyBool.create("north");
    public static final PropertyBool field_176257_M = PropertyBool.create("east");
    public static final PropertyBool field_176258_N = PropertyBool.create("south");
    public static final PropertyBool field_176259_O = PropertyBool.create("west");
    public static final PropertyEnum field_176255_P = PropertyEnum.create("variant", EnumType.class);
    private static final String __OBFID = "CL_00000331";

    public BlockWall(Block p_i45435_1_) {
        super(p_i45435_1_.blockMaterial);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176256_a, Boolean.valueOf(false)).withProperty(field_176254_b, Boolean.valueOf(false)).withProperty(field_176257_M, Boolean.valueOf(false)).withProperty(field_176258_N, Boolean.valueOf(false)).withProperty(field_176259_O, Boolean.valueOf(false)).withProperty(field_176255_P, (Comparable)((Object)EnumType.NORMAL)));
        this.setHardness(p_i45435_1_.blockHardness);
        this.setResistance(p_i45435_1_.blockResistance / 3.0f);
        this.setStepSound(p_i45435_1_.stepSound);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        boolean var3 = this.func_176253_e(access, pos.offsetNorth());
        boolean var4 = this.func_176253_e(access, pos.offsetSouth());
        boolean var5 = this.func_176253_e(access, pos.offsetWest());
        boolean var6 = this.func_176253_e(access, pos.offsetEast());
        float var7 = 0.25f;
        float var8 = 0.75f;
        float var9 = 0.25f;
        float var10 = 0.75f;
        float var11 = 1.0f;
        if (var3) {
            var9 = 0.0f;
        }
        if (var4) {
            var10 = 1.0f;
        }
        if (var5) {
            var7 = 0.0f;
        }
        if (var6) {
            var8 = 1.0f;
        }
        if (var3 && var4 && !var5 && !var6) {
            var11 = 0.8125f;
            var7 = 0.3125f;
            var8 = 0.6875f;
        } else if (!var3 && !var4 && var5 && var6) {
            var11 = 0.8125f;
            var9 = 0.3125f;
            var10 = 0.6875f;
        }
        this.setBlockBounds(var7, 0.0f, var9, var8, var11, var10);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        this.maxY = 1.5;
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    public boolean func_176253_e(IBlockAccess p_176253_1_, BlockPos p_176253_2_) {
        Block var3 = p_176253_1_.getBlockState(p_176253_2_).getBlock();
        return var3 == Blocks.barrier ? false : (var3 != this && !(var3 instanceof BlockFenceGate) ? (var3.blockMaterial.isOpaque() && var3.isFullCube() ? var3.blockMaterial != Material.gourd : false) : true);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (EnumType var7 : EnumType.values()) {
            list.add(new ItemStack(itemIn, 1, var7.func_176657_a()));
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumType)((Object)state.getValue(field_176255_P))).func_176657_a();
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN ? super.shouldSideBeRendered(worldIn, pos, side) : true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176255_P, (Comparable)((Object)EnumType.func_176660_a(meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumType)((Object)state.getValue(field_176255_P))).func_176657_a();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(field_176256_a, Boolean.valueOf(!worldIn.isAirBlock(pos.offsetUp()))).withProperty(field_176254_b, Boolean.valueOf(this.func_176253_e(worldIn, pos.offsetNorth()))).withProperty(field_176257_M, Boolean.valueOf(this.func_176253_e(worldIn, pos.offsetEast()))).withProperty(field_176258_N, Boolean.valueOf(this.func_176253_e(worldIn, pos.offsetSouth()))).withProperty(field_176259_O, Boolean.valueOf(this.func_176253_e(worldIn, pos.offsetWest())));
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176256_a, field_176254_b, field_176257_M, field_176259_O, field_176258_N, field_176255_P);
    }

    public static enum EnumType implements IStringSerializable
    {
        NORMAL("NORMAL", 0, 0, "cobblestone", "normal"),
        MOSSY("MOSSY", 1, 1, "mossy_cobblestone", "mossy");

        private static final EnumType[] field_176666_c;
        private final int field_176663_d;
        private final String field_176664_e;
        private String field_176661_f;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002048";

        static {
            field_176666_c = new EnumType[EnumType.values().length];
            $VALUES = new EnumType[]{NORMAL, MOSSY};
            EnumType[] var0 = EnumType.values();
            int var1 = var0.length;
            for (int var2 = 0; var2 < var1; ++var2) {
                EnumType var3;
                EnumType.field_176666_c[var3.func_176657_a()] = var3 = var0[var2];
            }
        }

        private EnumType(String p_i45673_1_, int p_i45673_2_, int p_i45673_3_, String p_i45673_4_, String p_i45673_5_) {
            this.field_176663_d = p_i45673_3_;
            this.field_176664_e = p_i45673_4_;
            this.field_176661_f = p_i45673_5_;
        }

        public int func_176657_a() {
            return this.field_176663_d;
        }

        public String toString() {
            return this.field_176664_e;
        }

        public static EnumType func_176660_a(int p_176660_0_) {
            if (p_176660_0_ < 0 || p_176660_0_ >= field_176666_c.length) {
                p_176660_0_ = 0;
            }
            return field_176666_c[p_176660_0_];
        }

        @Override
        public String getName() {
            return this.field_176664_e;
        }

        public String func_176659_c() {
            return this.field_176661_f;
        }
    }
}

