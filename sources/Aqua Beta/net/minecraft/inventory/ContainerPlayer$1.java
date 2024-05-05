package net.minecraft.inventory;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

class ContainerPlayer.1
extends Slot {
    final /* synthetic */ int val$k_f;

    ContainerPlayer.1(IInventory inventoryIn, int index, int xPosition, int yPosition, int n) {
        this.val$k_f = n;
        super(inventoryIn, index, xPosition, yPosition);
    }

    public int getSlotStackLimit() {
        return 1;
    }

    public boolean isItemValid(ItemStack stack) {
        return stack == null ? false : (stack.getItem() instanceof ItemArmor ? ((ItemArmor)stack.getItem()).armorType == this.val$k_f : (stack.getItem() != Item.getItemFromBlock((Block)Blocks.pumpkin) && stack.getItem() != Items.skull ? false : this.val$k_f == 0));
    }

    public String getSlotTexture() {
        return ItemArmor.EMPTY_SLOT_NAMES[this.val$k_f];
    }
}
