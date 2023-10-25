/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
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

        public boolean apply(EnumFacing enumFacing) {
            return enumFacing != EnumFacing.DOWN;
        }
    });
    private final Block crop;

    @Override
    public void grow(World world, Random random, BlockPos blockPos, IBlockState iBlockState) {
        this.growStem(world, blockPos, iBlockState);
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return null;
    }

    @Override
    public IBlockState getActualState(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        iBlockState = iBlockState.withProperty(FACING, EnumFacing.UP);
        for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
            if (iBlockAccess.getBlockState(blockPos.offset(enumFacing)).getBlock() != this.crop) continue;
            iBlockState = iBlockState.withProperty(FACING, enumFacing);
            break;
        }
        return iBlockState;
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(AGE, n);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        float f = 0.125f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.25f, 0.5f + f);
    }

    public void growStem(World world, BlockPos blockPos, IBlockState iBlockState) {
        int n = iBlockState.getValue(AGE) + MathHelper.getRandomIntegerInRange(world.rand, 2, 5);
        world.setBlockState(blockPos, iBlockState.withProperty(AGE, Math.min(7, n)), 2);
    }

    @Override
    public boolean canGrow(World world, BlockPos blockPos, IBlockState iBlockState, boolean bl) {
        return iBlockState.getValue(AGE) != 7;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState iBlockState, float f, int n) {
        Item item;
        super.dropBlockAsItemWithChance(world, blockPos, iBlockState, f, n);
        if (!world.isRemote && (item = this.getSeedItem()) != null) {
            int n2 = iBlockState.getValue(AGE);
            int n3 = 0;
            while (n3 < 3) {
                if (world.rand.nextInt(15) <= n2) {
                    BlockStem.spawnAsEntity(world, blockPos, new ItemStack(item));
                }
                ++n3;
            }
        }
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(AGE);
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        float f;
        super.updateTick(world, blockPos, iBlockState, random);
        if (world.getLightFromNeighbors(blockPos.up()) >= 9 && random.nextInt((int)(25.0f / (f = BlockCrops.getGrowthChance(this, world, blockPos))) + 1) == 0) {
            int n = iBlockState.getValue(AGE);
            if (n < 7) {
                iBlockState = iBlockState.withProperty(AGE, n + 1);
                world.setBlockState(blockPos, iBlockState, 2);
            } else {
                Object object2;
                for (Object object2 : EnumFacing.Plane.HORIZONTAL) {
                    if (world.getBlockState(blockPos.offset((EnumFacing)object2)).getBlock() != this.crop) continue;
                    return;
                }
                blockPos = blockPos.offset(EnumFacing.Plane.HORIZONTAL.random(random));
                object2 = world.getBlockState(blockPos.down()).getBlock();
                if (world.getBlockState((BlockPos)blockPos).getBlock().blockMaterial == Material.air && (object2 == Blocks.farmland || object2 == Blocks.dirt || object2 == Blocks.grass)) {
                    world.setBlockState(blockPos, this.crop.getDefaultState());
                }
            }
        }
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, AGE, FACING);
    }

    protected BlockStem(Block block) {
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0).withProperty(FACING, EnumFacing.UP));
        this.crop = block;
        this.setTickRandomly(true);
        float f = 0.125f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.25f, 0.5f + f);
        this.setCreativeTab(null);
    }

    @Override
    public int colorMultiplier(IBlockAccess iBlockAccess, BlockPos blockPos, int n) {
        return this.getRenderColor(iBlockAccess.getBlockState(blockPos));
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        this.maxY = (float)(iBlockAccess.getBlockState(blockPos).getValue(AGE) * 2 + 2) / 16.0f;
        float f = 0.125f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, (float)this.maxY, 0.5f + f);
    }

    protected Item getSeedItem() {
        return this.crop == Blocks.pumpkin ? Items.pumpkin_seeds : (this.crop == Blocks.melon_block ? Items.melon_seeds : null);
    }

    @Override
    protected boolean canPlaceBlockOn(Block block) {
        return block == Blocks.farmland;
    }

    @Override
    public int getRenderColor(IBlockState iBlockState) {
        if (iBlockState.getBlock() != this) {
            return super.getRenderColor(iBlockState);
        }
        int n = iBlockState.getValue(AGE);
        int n2 = n * 32;
        int n3 = 255 - n * 8;
        int n4 = n * 4;
        return n2 << 16 | n3 << 8 | n4;
    }

    @Override
    public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, IBlockState iBlockState) {
        return true;
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        Item item = this.getSeedItem();
        return item != null ? item : null;
    }
}

