package com.initial.utils.player;

import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.potion.*;
import java.util.*;

public final class InventoryUtils
{
    private static final Minecraft mc;
    
    public static void swap(final int slot, final int hotBarSlot) {
        InventoryUtils.mc.playerController.windowClick(InventoryUtils.mc.thePlayer.inventoryContainer.windowId, slot, hotBarSlot, 2, InventoryUtils.mc.thePlayer);
    }
    
    public static boolean isValidItem(final ItemStack itemStack) {
        return itemStack.getDisplayName().startsWith("\u00c2§a") || itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemFood || (itemStack.getItem() instanceof ItemPotion && !isBadPotion(itemStack)) || itemStack.getItem() instanceof ItemBlock || itemStack.getDisplayName().contains("Play") || itemStack.getDisplayName().contains("Game") || itemStack.getDisplayName().contains("Right Click");
    }
    
    public static float getDamageLevel(final ItemStack stack) {
        if (stack.getItem() instanceof ItemSword) {
            final ItemSword sword = (ItemSword)stack.getItem();
            final float sharpness = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f;
            final float fireAspect = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 1.5f;
            return sword.getDamageVsEntity() + sharpness + fireAspect;
        }
        return 0.0f;
    }
    
    public static boolean isBadPotion(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect)o;
                    if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
