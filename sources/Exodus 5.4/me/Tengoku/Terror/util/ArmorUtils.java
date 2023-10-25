/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ArmorUtils {
    public static int getItem(int n) {
        int n2 = 9;
        while (n2 < 45) {
            Minecraft.getMinecraft();
            ItemStack itemStack = Minecraft.thePlayer.inventoryContainer.getSlot(n2).getStack();
            if (itemStack != null && Item.getIdFromItem(itemStack.getItem()) == n) {
                return n2;
            }
            ++n2;
        }
        return -1;
    }

    public static boolean isBetterArmor(int n, int[] nArray) {
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.inventory.armorInventory[n] != null) {
            int n2;
            int n3 = 0;
            int n4 = 0;
            int n5 = -1;
            int n6 = -1;
            int[] nArray2 = nArray;
            int n7 = nArray.length;
            int n8 = 0;
            while (n8 < n7) {
                n2 = nArray2[n8];
                Minecraft.getMinecraft();
                if (Item.getIdFromItem(Minecraft.thePlayer.inventory.armorInventory[n].getItem()) == n2) {
                    n5 = n3;
                    break;
                }
                ++n3;
                ++n8;
            }
            nArray2 = nArray;
            n7 = nArray.length;
            n8 = 0;
            while (n8 < n7) {
                n2 = nArray2[n8];
                if (ArmorUtils.getItem(n2) != -1) {
                    n6 = n4;
                    break;
                }
                ++n4;
                ++n8;
            }
            if (n6 > -1) {
                return n6 < n5;
            }
        }
        return false;
    }
}

