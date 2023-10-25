/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockMelon
extends Block {
    @Override
    public int quantityDropped(Random random) {
        return 3 + random.nextInt(5);
    }

    @Override
    public int quantityDroppedWithBonus(int n, Random random) {
        return Math.min(9, this.quantityDropped(random) + random.nextInt(1 + n));
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Items.melon;
    }

    protected BlockMelon() {
        super(Material.gourd, MapColor.limeColor);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
}

