package me.aquavit.liquidsense.utils.forge;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BlocksTab extends CreativeTabs {

    /**
     * Initialize of special blocks tab
     */
    public BlocksTab() {
        super("Special blocks");
        this.setBackgroundImageName("item_search.png");
    }

    /**
     * @return searchbar status
     */
    @Override
    public boolean hasSearchBar() {
        return true;
    }

    /**
     * Return name of tab
     *
     * @return tab name
     */
    @Override
    public String getTranslatedTabLabel() {
        return "Special blocks";
    }

    /**
     * Return icon item of tab
     *
     * @return icon item
     */
    @Override
    public Item getTabIconItem() {
        return new ItemStack(Blocks.command_block).getItem();
    }

    /**
     * Add all items to tab
     *
     * @param itemList list of tab items
     */
    @Override
    public void displayAllReleventItems(List<ItemStack> itemList) {
        itemList.add(new ItemStack(Blocks.command_block));
        itemList.add(new ItemStack(Items.command_block_minecart));
        itemList.add(new ItemStack(Blocks.barrier));
        itemList.add(new ItemStack(Blocks.dragon_egg));
        itemList.add(new ItemStack(Blocks.brown_mushroom_block));
        itemList.add(new ItemStack(Blocks.red_mushroom_block));
        itemList.add(new ItemStack(Blocks.farmland));
        itemList.add(new ItemStack(Blocks.mob_spawner));
        itemList.add(new ItemStack(Blocks.lit_furnace));
    }
}
