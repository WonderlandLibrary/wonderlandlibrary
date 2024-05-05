package net.minecraft.creativetab;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

static final class CreativeTabs.1
extends CreativeTabs {
    CreativeTabs.1(int index, String label) {
        super(index, label);
    }

    public Item getTabIconItem() {
        return Item.getItemFromBlock((Block)Blocks.brick_block);
    }
}
