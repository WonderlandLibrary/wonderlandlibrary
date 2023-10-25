/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class InventoryUtils {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static boolean busy;

    public static ItemStack compareProtection(ItemStack itemStack, ItemStack itemStack2) {
        if (!(itemStack.getItem() instanceof ItemArmor) && !(itemStack2.getItem() instanceof ItemArmor)) {
            return null;
        }
        if (!(itemStack.getItem() instanceof ItemArmor)) {
            return itemStack2;
        }
        if (!(itemStack2.getItem() instanceof ItemArmor)) {
            return itemStack;
        }
        if (InventoryUtils.getArmorProtection(itemStack) > InventoryUtils.getArmorProtection(itemStack2)) {
            return itemStack;
        }
        if (InventoryUtils.getArmorProtection(itemStack2) > InventoryUtils.getArmorProtection(itemStack)) {
            return itemStack2;
        }
        return null;
    }

    public static boolean isContainerEmpty(Container container) {
        boolean bl = true;
        for (Slot slot : container.inventorySlots) {
            if (!slot.getHasStack()) continue;
            bl = false;
            break;
        }
        return bl;
    }

    public static boolean isContainerFull(Container container) {
        boolean bl = true;
        for (Slot slot : container.inventorySlots) {
            if (slot.getHasStack()) continue;
            bl = false;
            break;
        }
        return bl;
    }

    public static int getArmorItemsEquipSlot(ItemStack itemStack, boolean bl) {
        if (itemStack.getUnlocalizedName().contains("helmet")) {
            return bl ? 4 : 5;
        }
        if (itemStack.getUnlocalizedName().contains("chestplate")) {
            return bl ? 3 : 6;
        }
        if (itemStack.getUnlocalizedName().contains("leggings")) {
            return bl ? 2 : 7;
        }
        if (itemStack.getUnlocalizedName().contains("boots")) {
            return bl ? 1 : 8;
        }
        return -1;
    }

    public static float getItemDamage(ItemStack itemStack) {
        float f = ((ItemSword)itemStack.getItem()).getDamageVsEntity();
        f += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
        return f += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.01f;
    }

    public static boolean isHoldingSword() {
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer != null) {
            Minecraft.getMinecraft();
            if (Minecraft.theWorld != null) {
                Minecraft.getMinecraft();
                if (Minecraft.thePlayer.getCurrentEquippedItem() != null) {
                    Minecraft.getMinecraft();
                    if (Minecraft.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static double getArmorProtection(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemArmor)) {
            return 0.0;
        }
        ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
        double d = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack);
        return (double)itemArmor.damageReduceAmount + (6.0 + d * d) * 0.75 / 3.0;
    }

    public static float getSharpnessLevel(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemSword)) {
            return 0.0f;
        }
        return (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
    }
}

