/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStem
extends BlockBush
implements IGrowable {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
    public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>(){

        @Override
        public boolean apply(EnumFacing p_apply_1_) {
            if (p_apply_1_ == EnumFacing.DOWN) return false;
            return true;
        }
    });
    private final Block crop;

    protected BlockStem(Block crop) {
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0).withProperty(FACING, EnumFacing.UP));
        this.crop = crop;
        this.setTickRandomly(true);
        float f = 0.125f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.25f, 0.5f + f);
        this.setCreativeTab(null);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        EnumFacing enumfacing;
        state = state.withProperty(FACING, EnumFacing.UP);
        Iterator<EnumFacing> iterator = EnumFacing.Plane.HORIZONTAL.iterator();
        do {
            if (!iterator.hasNext()) return state;
        } while (worldIn.getBlockState(pos.offset(enumfacing = iterator.next())).getBlock() != this.crop);
        return state.withProperty(FACING, enumfacing);
    }

    @Override
    protected boolean canPlaceBlockOn(Block ground) {
        if (ground != Blocks.farmland) return false;
        return true;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.getLightFromNeighbors(pos.up()) < 9) return;
        float f = BlockCrops.getGrowthChance(this, worldIn, pos);
        if (rand.nextInt((int)(25.0f / f) + 1) != 0) return;
        int i = state.getValue(AGE);
        if (i < 7) {
            state = state.withProperty(AGE, i + 1);
            worldIn.setBlockState(pos, state, 2);
            return;
        }
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this.crop) continue;
            return;
        }
        pos = pos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));
        Block block = worldIn.getBlockState(pos.down()).getBlock();
        if (worldIn.getBlockState((BlockPos)pos).getBlock().blockMaterial != Material.air) return;
        if (block != Blocks.farmland && block != Blocks.dirt) {
            if (block != Blocks.grass) return;
        }
        worldIn.setBlockState(pos, this.crop.getDefaultState());
    }

    public void growStem(World worldIn, BlockPos pos, IBlockState state) {
        int i = state.getValue(AGE) + MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
        worldIn.setBlockState(pos, state.withProperty(AGE, Math.min(7, i)), 2);
    }

    @Override
    public int getRenderColor(IBlockState state) {
        if (state.getBlock() != this) {
            return super.getRenderColor(state);
        }
        int i = state.getValue(AGE);
        int j = i * 32;
        int k = 255 - i * 8;
        int l = i * 4;
        return j << 16 | k << 8 | l;
    }

    @Override
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return this.getRenderColor(worldIn.getBlockState(pos));
    }

    @Override
    public void setBlockBoundsForItemRender() {
        float f = 0.125f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.25f, 0.5f + f);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        this.maxY = (float)(worldIn.getBlockState(pos).getValue(AGE) * 2 + 2) / 16.0f;
        float f = 0.125f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, (float)this.maxY, 0.5f + f);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        if (worldIn.isRemote) return;
        Item item = this.getSeedItem();
        if (item == null) return;
        int i = state.getValue(AGE);
        int j = 0;
        while (j < 3) {
            if (worldIn.rand.nextInt(15) <= i) {
                BlockStem.spawnAsEntity(worldIn, pos, new ItemStack(item));
            }
            ++j;
        }
    }

    protected Item getSeedItem() {
        Item item;
        if (this.crop == Blocks.pumpkin) {
            item = Items.pumpkin_seeds;
            return item;
        }
        if (this.crop != Blocks.melon_block) return null;
        item = Items.melon_seeds;
        return item;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        Item item = this.getSeedItem();
        if (item == null) return null;
        Item item2 = item;
        return item2;
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        if (state.getValue(AGE) == 7) return false;
        return true;
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
    protected BlockState createBlockState() {
        return new BlockState(this, AGE, FACING);
    }
}

