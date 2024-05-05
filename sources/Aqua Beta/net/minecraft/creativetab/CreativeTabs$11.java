package net.minecraft.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

static final class CreativeTabs.11
extends CreativeTabs {
    CreativeTabs.11(int index, String label) {
        super(index, label);
    }

    public Item getTabIconItem() {
        return Items.stick;
    }
}
