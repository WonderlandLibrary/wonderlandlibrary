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
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        Item item;
        if (fortune > 3) {
            fortune = 3;
        }
        if (rand.nextInt(10 - fortune * 3) == 0) {
            item = Items.flint;
            return item;
        }
        item = Item.getItemFromBlock(this);
        return item;
    }

    @Override
    public MapColor getMapColor(IBlockState state) {
        return MapColor.stoneColor;
    }
}

