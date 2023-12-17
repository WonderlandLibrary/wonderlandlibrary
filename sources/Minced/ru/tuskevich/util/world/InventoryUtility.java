// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.world;

import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemFirework;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.client.Minecraft;
import ru.tuskevich.util.Utility;

public class InventoryUtility implements Utility
{
    public static boolean doesHotbarHaveAxe() {
        for (int i = 0; i < 9; ++i) {
            final Minecraft mc = InventoryUtility.mc;
            Minecraft.player.inventory.getStackInSlot(i);
            final Minecraft mc2 = InventoryUtility.mc;
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() instanceof ItemAxe) {
                return true;
            }
        }
        return false;
    }
    
    public static int getInventoryItemSlot(final Item item, final boolean hotbar) {
        int n;
        for (int i = n = (hotbar ? 0 : 9); i < 39; ++i) {
            final Minecraft mc = InventoryUtility.mc;
            final ItemStack stack = Minecraft.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                return (i < 9) ? (i + 36) : i;
            }
        }
        return -1;
    }
    
    public static int getSlotWithArmor() {
        for (int i = 0; i < 45; ++i) {
            final Minecraft mc = InventoryUtility.mc;
            final ItemStack itemStack = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.DIAMOND_CHESTPLATE || itemStack.getItem() == Items.GOLDEN_CHESTPLATE || itemStack.getItem() == Items.LEATHER_CHESTPLATE || itemStack.getItem() == Items.CHAINMAIL_CHESTPLATE || itemStack.getItem() == Items.IRON_CHESTPLATE) {
                return (i < 9) ? (i + 36) : i;
            }
        }
        return -1;
    }
    
    public static int getPearls() {
        for (int i = 0; i < 9; ++i) {
            final Minecraft mc = InventoryUtility.mc;
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() instanceof ItemEnderPearl) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getFireworks() {
        for (int i = 0; i < 9; ++i) {
            final Minecraft mc = InventoryUtility.mc;
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() instanceof ItemFirework) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getFireworksAtInventory() {
        for (int i = 0; i < 9; ++i) {
            final Minecraft mc = InventoryUtility.mc;
            if (Minecraft.player.inventory.getItemStack().getItem() instanceof ItemFirework) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getSwordAtHotbar() {
        for (int i = 0; i < 9; ++i) {
            final Minecraft mc = InventoryUtility.mc;
            final ItemStack itemStack = Minecraft.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemSword) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getAxe() {
        for (int i = 0; i < 9; ++i) {
            final Minecraft mc = InventoryUtility.mc;
            final ItemStack itemStack = Minecraft.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemAxe) {
                return i;
            }
        }
        return 1;
    }
    
    public static int getItemIndex(final Item item) {
        for (int i = 0; i < 45; ++i) {
            final Minecraft mc = InventoryUtility.mc;
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }
}
