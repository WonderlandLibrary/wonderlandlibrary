package koks.api.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author kroko
 * @created on 17.11.2020 : 15:33
 */
public class InventoryUtil {

    private Minecraft mc = Minecraft.getMinecraft();

    public boolean hasAir() {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null) {
                return true;
            }
        }
        return false;
    }

    public int getItemSize(Item item, IInventory inventory) {
        int size = 0;
        for(int i = 0; i < inventory.getSizeInventory(); i++) {
            if(inventory.getStackInSlot(i) != null) {
                ItemStack stack = inventory.getStackInSlot(i);
                if(stack.getItem().equals(item) && stack.stackSize != stack.getMaxStackSize())
                    size++;
            }
        }
        return size;
    }

    public int getAir() {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null) {
                return i;
            }
        }
        return -1;
    }

    public ItemStack getItem(int slot) {
        return mc.thePlayer.inventory.getStackInSlot(slot);
    }

    public ItemStack searchInHotbar(Item itm) {
        return searchItem(itm, 0, 8);
    }

    public int searchSlotInHotbar(Item itm) {
        return searchItemSlot(itm, 0, 8);
    }

    public int searchItemSlot(Item itm, int begin, int end) {
        for (int i = begin; i < end; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack != null) {
                if (itemStack.getItem().equals(itm)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public ItemStack searchItem(Item itm, int begin, int end) {
        for (int i = begin; i < end; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack != null) {
                if (itemStack.getItem().equals(itm)) {
                    return itemStack;
                }
            }
        }
        return null;
    }
}
