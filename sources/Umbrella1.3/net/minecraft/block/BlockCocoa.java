/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCocoa
extends BlockDirectional
implements IGrowable {
    public static final PropertyInteger field_176501_a = PropertyInteger.create("age", 0, 2);
    private static final String __OBFID = "CL_00000216";

    public BlockCocoa() {
        super(Material.plants);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, (Comparable)((Object)EnumFacing.NORTH)).withProperty(field_176501_a, Integer.valueOf(0)));
        this.setTickRandomly(true);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int var5;
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlock(worldIn, pos, state);
        } else if (worldIn.rand.nextInt(5) == 0 && (var5 = ((Integer)state.getValue(field_176501_a)).intValue()) < 2) {
            worldIn.setBlockState(pos, state.withProperty(field_176501_a, Integer.valueOf(var5 + 1)), 2);
        }
    }

    public boolean canBlockStay(World worldIn, BlockPos p_176499_2_, IBlockState p_176499_3_) {
        IBlockState var4 = worldIn.getBlockState(p_176499_2_ = p_176499_2_.offset((EnumFacing)((Object)p_176499_3_.getValue(AGE))));
        return var4.getBlock() == Blocks.log && var4.getValue(BlockPlanks.VARIANT_PROP) == BlockPlanks.EnumType.JUNGLE;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        IBlockState var3 = access.getBlockState(pos);
        EnumFacing var4 = (EnumFacing)((Object)var3.getValue(AGE));
        int var5 = (Integer)var3.getValue(field_176501_a);
        int var6 = 4 + var5 * 2;
        int var7 = 5 + var5 * 2;
        float var8 = (float)var6 / 2.0f;
        switch (SwitchEnumFacing.FACINGARRAY[var4.ordinal()]) {
            case 1: {
                this.setBlockBounds((8.0f - var8) / 16.0f, (12.0f - (float)var7) / 16.0f, (15.0f - (float)var6) / 16.0f, (8.0f + var8) / 16.0f, 0.75f, 0.9375f);
                break;
            }
            case 2: {
                this.setBlockBounds((8.0f - var8) / 16.0f, (12.0f - (float)var7) / 16.0f, 0.0625f, (8.0f + var8) / 16.0f, 0.75f, (1.0f + (float)var6) / 16.0f);
                break;
            }
            case 3: {
                this.setBlockBounds(0.0625f, (12.0f - (float)var7) / 16.0f, (8.0f - var8) / 16.0f, (1.0f + (float)var6) / 16.0f, 0.75f, (8.0f + var8) / 16.0f);
                break;
            }
            case 4: {
                this.setBlockBounds((15.0f - (float)var6) / 16.0f, (12.0f - (float)var7) / 16.0f, (8.0f - var8) / 16.0f, 0.9375f, 0.75f, (8.0f + var8) / 16.0f);
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing var6 = EnumFacing.fromAngle(placer.rotationYaw);
        worldIn.setBlockState(pos, state.withProperty(AGE, (Comparable)((Object)var6)), 2);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (!facing.getAxis().isHorizontal()) {
            facing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(AGE, (Comparable)((Object)facing.getOpposite())).withProperty(field_176501_a, Integer.valueOf(0));
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlock(worldIn, pos, state);
        }
    }

    private void dropBlock(World worldIn, BlockPos p_176500_2_, IBlockState p_176500_3_) {
        worldIn.setBlockState(p_176500_2_, Blocks.air.getDefaultState(), 3);
        this.dropBlockAsItem(worldIn, p_176500_2_, p_176500_3_, 0);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        int var6 = (Integer)state.getValue(field_176501_a);
        int var7 = 1;
        if (var6 >= 2) {
            var7 = 3;
        }
        for (int var8 = 0; var8 < var7; ++var8) {
            BlockCocoa.spawnAsEntity(worldIn, pos, new ItemStack(Items.dye, 1, EnumDyeColor.BROWN.getDyeColorDamage()));
        }
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Items.dye;
    }

    @Override
    public int getDamageValue(World worldIn, BlockPos pos) {
        return EnumDyeColor.BROWN.getDyeColorDamage();
    }

    @Override
    public boolean isStillGrowing(World worldIn, BlockPos p_176473_2_, IBlockState p_176473_3_, boolean p_176473_4_) {
        return (Integer)p_176473_3_.getValue(field_176501_a) < 2;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random p_180670_2_, BlockPos p_180670_3_, IBlockState p_180670_4_) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_) {
        worldIn.setBlockState(p_176474_3_, p_176474_4_.withProperty(field_176501_a, Integer.valueOf((Integer)p_176474_4_.getValue(field_176501_a) + 1)), 2);
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AGE, (Comparable)((Object)EnumFacing.getHorizontal(meta))).withProperty(field_176501_a, Integer.valueOf((meta & 0xF) >> 2));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        int var3 = var2 | ((EnumFacing)((Object)state.getValue(AGE))).getHorizontalIndex();
        return var3 |= (Integer)state.getValue(field_176501_a) << 2;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, AGE, field_176501_a);
    }

    static final class SwitchEnumFacing {
        static final int[] FACINGARRAY = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002130";

        static {
            try {
                SwitchEnumFacing.FACINGARRAY[EnumFacing.SOUTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.FACINGARRAY[EnumFacing.NORTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.FACINGARRAY[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.FACINGARRAY[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumFacing() {
        }
    }
}

