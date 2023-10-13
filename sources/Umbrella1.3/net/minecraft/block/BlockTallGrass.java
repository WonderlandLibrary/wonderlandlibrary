/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTallGrass
extends BlockBush
implements IGrowable {
    public static final PropertyEnum field_176497_a = PropertyEnum.create("type", EnumType.class);
    private static final String __OBFID = "CL_00000321";

    protected BlockTallGrass() {
        super(Material.vine);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176497_a, (Comparable)((Object)EnumType.DEAD_BUSH)));
        float var1 = 0.4f;
        this.setBlockBounds(0.5f - var1, 0.0f, 0.5f - var1, 0.5f + var1, 0.8f, 0.5f + var1);
    }

    @Override
    public int getBlockColor() {
        return ColorizerGrass.getGrassColor(0.5, 1.0);
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos p_180671_2_, IBlockState p_180671_3_) {
        return this.canPlaceBlockOn(worldIn.getBlockState(p_180671_2_.offsetDown()).getBlock());
    }

    @Override
    public boolean isReplaceable(World worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public int getRenderColor(IBlockState state) {
        if (state.getBlock() != this) {
            return super.getRenderColor(state);
        }
        EnumType var2 = (EnumType)((Object)state.getValue(field_176497_a));
        return var2 == EnumType.DEAD_BUSH ? 0xFFFFFF : ColorizerGrass.getGrassColor(0.5, 1.0);
    }

    @Override
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return worldIn.getBiomeGenForCoords(pos).func_180627_b(pos);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return rand.nextInt(8) == 0 ? Items.wheat_seeds : null;
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        return 1 + random.nextInt(fortune * 2 + 1);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, IBlockState state, TileEntity te) {
        if (!worldIn.isRemote && playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == Items.shears) {
            playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            BlockTallGrass.spawnAsEntity(worldIn, pos, new ItemStack(Blocks.tallgrass, 1, ((EnumType)((Object)state.getValue(field_176497_a))).func_177044_a()));
        } else {
            super.harvestBlock(worldIn, playerIn, pos, state, te);
        }
    }

    @Override
    public int getDamageValue(World worldIn, BlockPos pos) {
        IBlockState var3 = worldIn.getBlockState(pos);
        return var3.getBlock().getMetaFromState(var3);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (int var4 = 1; var4 < 3; ++var4) {
            list.add(new ItemStack(itemIn, 1, var4));
        }
    }

    @Override
    public boolean isStillGrowing(World worldIn, BlockPos p_176473_2_, IBlockState p_176473_3_, boolean p_176473_4_) {
        return p_176473_3_.getValue(field_176497_a) != EnumType.DEAD_BUSH;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random p_180670_2_, BlockPos p_180670_3_, IBlockState p_180670_4_) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_) {
        BlockDoublePlant.EnumPlantType var5 = BlockDoublePlant.EnumPlantType.GRASS;
        if (p_176474_4_.getValue(field_176497_a) == EnumType.FERN) {
            var5 = BlockDoublePlant.EnumPlantType.FERN;
        }
        if (Blocks.double_plant.canPlaceBlockAt(worldIn, p_176474_3_)) {
            Blocks.double_plant.func_176491_a(worldIn, p_176474_3_, var5, 2);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176497_a, (Comparable)((Object)EnumType.func_177045_a(meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumType)((Object)state.getValue(field_176497_a))).func_177044_a();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176497_a);
    }

    @Override
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XYZ;
    }

    public static enum EnumType implements IStringSerializable
    {
        DEAD_BUSH("DEAD_BUSH", 0, 0, "dead_bush"),
        GRASS("GRASS", 1, 1, "tall_grass"),
        FERN("FERN", 2, 2, "fern");

        private static final EnumType[] field_177048_d;
        private final int field_177049_e;
        private final String field_177046_f;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002055";

        static {
            field_177048_d = new EnumType[EnumType.values().length];
            $VALUES = new EnumType[]{DEAD_BUSH, GRASS, FERN};
            EnumType[] var0 = EnumType.values();
            int var1 = var0.length;
            for (int var2 = 0; var2 < var1; ++var2) {
                EnumType var3;
                EnumType.field_177048_d[var3.func_177044_a()] = var3 = var0[var2];
            }
        }

        private EnumType(String p_i45676_1_, int p_i45676_2_, int p_i45676_3_, String p_i45676_4_) {
            this.field_177049_e = p_i45676_3_;
            this.field_177046_f = p_i45676_4_;
        }

        public int func_177044_a() {
            return this.field_177049_e;
        }

        public String toString() {
            return this.field_177046_f;
        }

        public static EnumType func_177045_a(int p_177045_0_) {
            if (p_177045_0_ < 0 || p_177045_0_ >= field_177048_d.length) {
                p_177045_0_ = 0;
            }
            return field_177048_d[p_177045_0_];
        }

        @Override
        public String getName() {
            return this.field_177046_f;
        }
    }
}

