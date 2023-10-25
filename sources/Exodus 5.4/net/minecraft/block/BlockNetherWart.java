/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockNetherWart
extends BlockBush {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);

    protected BlockNetherWart() {
        super(Material.plants, MapColor.redColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
        this.setTickRandomly(true);
        float f = 0.5f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.25f, 0.5f + f);
        this.setCreativeTab(null);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(AGE);
    }

    @Override
    protected boolean canPlaceBlockOn(Block block) {
        return block == Blocks.soul_sand;
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        int n = iBlockState.getValue(AGE);
        if (n < 3 && random.nextInt(10) == 0) {
            iBlockState = iBlockState.withProperty(AGE, n + 1);
            world.setBlockState(blockPos, iBlockState, 2);
        }
        super.updateTick(world, blockPos, iBlockState, random);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, AGE);
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState iBlockState, float f, int n) {
        if (!world.isRemote) {
            int n2 = 1;
            if (iBlockState.getValue(AGE) >= 3) {
                n2 = 2 + world.rand.nextInt(3);
                if (n > 0) {
                    n2 += world.rand.nextInt(n + 1);
                }
            }
            int n3 = 0;
            while (n3 < n2) {
                BlockNetherWart.spawnAsEntity(world, blockPos, new ItemStack(Items.nether_wart));
                ++n3;
            }
        }
    }

    @Override
    public boolean canBlockStay(World world, BlockPos blockPos, IBlockState iBlockState) {
        return this.canPlaceBlockOn(world.getBlockState(blockPos.down()).getBlock());
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(AGE, n);
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return Items.nether_wart;
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return null;
    }
}

