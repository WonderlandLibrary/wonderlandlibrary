/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockGravel
extends BlockFalling {
    @Override
    public MapColor getMapColor(IBlockState iBlockState) {
        return MapColor.stoneColor;
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        if (n > 3) {
            n = 3;
        }
        return random.nextInt(10 - n * 3) == 0 ? Items.flint : Item.getItemFromBlock(this);
    }
}

