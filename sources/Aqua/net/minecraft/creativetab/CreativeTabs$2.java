package net.minecraft.creativetab;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

static final class CreativeTabs.2
extends CreativeTabs {
    CreativeTabs.2(int index, String label) {
        super(index, label);
    }

    public Item getTabIconItem() {
        return Item.getItemFromBlock((Block)Blocks.double_plant);
    }

    public int getIconItemDamage() {
        return BlockDoublePlant.EnumPlantType.PAEONIA.getMeta();
    }
}
