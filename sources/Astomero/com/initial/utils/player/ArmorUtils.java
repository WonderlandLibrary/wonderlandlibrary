package com.initial.utils.player;

import net.minecraft.client.*;
import net.minecraft.item.*;

public class ArmorUtils
{
    public static boolean isBetterArmor(final int slot, final int[] armorType) {
        if (Minecraft.getMinecraft().thePlayer.inventory.armorInventory[slot] != null) {
            int currentIndex = 0;
            int invIndex = 0;
            int finalCurrentIndex = -1;
            int finalInvIndex = -1;
            int[] array = armorType;
            for (int j = armorType.length, i = 0; i < j; ++i) {
                final int armor = array[i];
                if (Item.getIdFromItem(Minecraft.getMinecraft().thePlayer.inventory.armorInventory[slot].getItem()) == armor) {
                    finalCurrentIndex = currentIndex;
                    break;
                }
                ++currentIndex;
            }
            array = armorType;
            for (int j = armorType.length, i = 0; i < j; ++i) {
                final int armor = array[i];
                if (getItem(armor) != -1) {
                    finalInvIndex = invIndex;
                    break;
                }
                ++invIndex;
            }
            if (finalInvIndex > -1) {
                return finalInvIndex < finalCurrentIndex;
            }
        }
        return false;
    }
    
    public static int getItem(final int id) {
        for (int i = 9; i < 45; ++i) {
            final ItemStack item = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
            if (item != null && Item.getIdFromItem(item.getItem()) == id) {
                return i;
            }
        }
        return -1;
    }
}
