/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockRedstoneLight
extends Block {
    private final boolean isOn;

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (!world.isRemote) {
            if (this.isOn && !world.isBlockPowered(blockPos)) {
                world.setBlockState(blockPos, Blocks.redstone_lamp.getDefaultState(), 2);
            } else if (!this.isOn && world.isBlockPowered(blockPos)) {
                world.setBlockState(blockPos, Blocks.lit_redstone_lamp.getDefaultState(), 2);
            }
        }
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (!world.isRemote && this.isOn && !world.isBlockPowered(blockPos)) {
            world.setBlockState(blockPos, Blocks.redstone_lamp.getDefaultState(), 2);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        if (!world.isRemote) {
            if (this.isOn && !world.isBlockPowered(blockPos)) {
                world.scheduleUpdate(blockPos, this, 4);
            } else if (!this.isOn && world.isBlockPowered(blockPos)) {
                world.setBlockState(blockPos, Blocks.lit_redstone_lamp.getDefaultState(), 2);
            }
        }
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Item.getItemFromBlock(Blocks.redstone_lamp);
    }

    public BlockRedstoneLight(boolean bl) {
        super(Material.redstoneLight);
        this.isOn = bl;
        if (bl) {
            this.setLightLevel(1.0f);
        }
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return Item.getItemFromBlock(Blocks.redstone_lamp);
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState iBlockState) {
        return new ItemStack(Blocks.redstone_lamp);
    }
}

