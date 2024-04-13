package net.smoothboot.client.util;


import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.util.function.Predicate;

public enum InventoryUtil
{
    ;
    private static MinecraftClient mc = MinecraftClient.getInstance();

    public static boolean selectItemFromHotbar(Predicate<Item> item)
    {
        PlayerInventory inv = mc.player.getInventory();

        for (int i = 0; i < 9; i++)
        {
            ItemStack itemStack = inv.getStack(i);
            if (!item.test(itemStack.getItem()))
                continue;
            inv.selectedSlot = i;
            return true;
        }

        return false;
    }

    public static boolean search(Item item) {
        final PlayerInventory inv = mc.player.getInventory();
        for (int i = 0; i <= 8; i ++) {
            if (inv.getStack(i).isOf(item)) {
                inv.selectedSlot = i;
                return true;
            }
        }
        return false;
    }

    public static boolean nameContains(String contains) {
        return nameContains(contains, Hand.MAIN_HAND);
    }

    public static boolean nameContains(String contains, Hand hand) {
        ItemStack item = mc.player.getStackInHand(hand);
        return item != null && item.getTranslationKey().toLowerCase().contains(contains.toLowerCase());
    }


    public static boolean selectItemFromHotbar(Item item)
    {
        return selectItemFromHotbar(i -> i == item);
    }

    public static boolean hasItemInHotbar(Predicate<Item> item)
    {
        PlayerInventory inv = mc.player.getInventory();

        for (int i = 0; i < 9; i++)
        {
            ItemStack itemStack = inv.getStack(i);
            if (item.test(itemStack.getItem()))
                return true;
        }
        return false;
    }
}
