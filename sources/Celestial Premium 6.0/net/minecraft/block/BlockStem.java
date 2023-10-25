/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStem
extends BlockBush
implements IGrowable {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
    public static final PropertyDirection FACING = BlockTorch.FACING;
    private final Block crop;
    protected static final AxisAlignedBB[] STEM_AABB = new AxisAlignedBB[]{new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.125, 0.625), new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.25, 0.625), new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.375, 0.625), new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.5, 0.625), new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.625, 0.625), new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.75, 0.625), new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.875, 0.625), new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625)};

    protected BlockStem(Block crop) {
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0).withProperty(FACING, EnumFacing.UP));
        this.crop = crop;
        this.setTickRandomly(true);
        this.setCreativeTab(null);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return STEM_AABB[state.getValue(AGE)];
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        int i = state.getValue(AGE);
        state = state.withProperty(FACING, EnumFacing.UP);
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this.crop || i != 7) continue;
            state = state.withProperty(FACING, enumfacing);
            break;
        }
        return state;
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock() == Blocks.FARMLAND;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        float f;
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt((int)(25.0f / (f = BlockCrops.getGrowthChance(this, worldIn, pos))) + 1) == 0) {
            int i = state.getValue(AGE);
            if (i < 7) {
                state = state.withProperty(AGE, i + 1);
                worldIn.setBlockState(pos, state, 2);
            } else {
                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                    if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this.crop) continue;
                    return;
                }
                pos = pos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));
                Block block = worldIn.getBlockState(pos.down()).getBlock();
                if (worldIn.getBlockState((BlockPos)pos).getBlock().blockMaterial == Material.AIR && (block == Blocks.FARMLAND || block == Blocks.DIRT || block == Blocks.GRASS)) {
                    worldIn.setBlockState(pos, this.crop.getDefaultState());
                }
            }
        }
    }

    public void growStem(World worldIn, BlockPos pos, IBlockState state) {
        int i = state.getValue(AGE) + MathHelper.getInt(worldIn.rand, 2, 5);
        worldIn.setBlockState(pos, state.withProperty(AGE, Math.min(7, i)), 2);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        Item item;
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        if (!worldIn.isRemote && (item = this.getSeedItem()) != null) {
            int i = state.getValue(AGE);
            for (int j = 0; j < 3; ++j) {
                if (worldIn.rand.nextInt(15) > i) continue;
                BlockStem.spawnAsEntity(worldIn, pos, new ItemStack(item));
            }
        }
    }

    @Nullable
    protected Item getSeedItem() {
        if (this.crop == Blocks.PUMPKIN) {
            return Items.PUMPKIN_SEEDS;
        }
        return this.crop == Blocks.MELON_BLOCK ? Items.MELON_SEEDS : null;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.field_190931_a;
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        Item item = this.getSeedItem();
        return item == null ? ItemStack.field_190927_a : new ItemStack(item);
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return state.getValue(AGE) != 7;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        this.growStem(worldIn, pos, state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AGE, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AGE);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, AGE, FACING);
    }
}

