/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockPotato
extends BlockCrops {
    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState iBlockState, float f, int n) {
        super.dropBlockAsItemWithChance(world, blockPos, iBlockState, f, n);
        if (!world.isRemote && iBlockState.getValue(AGE) >= 7 && world.rand.nextInt(50) == 0) {
            BlockPotato.spawnAsEntity(world, blockPos, new ItemStack(Items.poisonous_potato));
        }
    }

    @Override
    protected Item getCrop() {
        return Items.potato;
    }

    @Override
    protected Item getSeed() {
        return Items.potato;
    }
}

