package io.github.raze.utilities.collection.world;

import io.github.raze.utilities.system.Methods;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class PlayerUtil implements Methods {

    public static int getIndexOfItem() {
        final InventoryPlayer inventoryPlayer = mc.thePlayer.inventory;
        return inventoryPlayer.currentItem;
    }

    public static ItemStack getItemStack() {
        return (mc.thePlayer == null || mc.thePlayer.inventoryContainer == null ? null : mc.thePlayer.inventoryContainer.getSlot(getIndexOfItem() + 36).getStack());
    }

}
