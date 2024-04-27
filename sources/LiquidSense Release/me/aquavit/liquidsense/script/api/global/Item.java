package me.aquavit.liquidsense.script.api.global;

import me.aquavit.liquidsense.utils.item.ItemUtils;
import net.minecraft.item.ItemStack;

public class Item {
    public static Item item = new Item();

    public static ItemStack create(String itemArguments) {
        return ItemUtils.createItem(itemArguments);
    }
}
