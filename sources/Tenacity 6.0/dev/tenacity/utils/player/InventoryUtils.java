// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.player;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import dev.tenacity.utils.Utils;

public class InventoryUtils implements Utils
{
    public static int getItemSlot(final Item item) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack is = InventoryUtils.mc.thePlayer.inventory.mainInventory[i];
            if (is != null && is.getItem() == item) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getBlockSlot(final Block block) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack is = InventoryUtils.mc.thePlayer.inventory.mainInventory[i];
            if (is != null && is.getItem() instanceof ItemBlock && ((ItemBlock)is.getItem()).getBlock() == block) {
                return i;
            }
        }
        return -1;
    }
    
    public static Item getHeldItem() {
        if (InventoryUtils.mc.thePlayer == null || InventoryUtils.mc.thePlayer.getCurrentEquippedItem() == null) {
            return null;
        }
        return InventoryUtils.mc.thePlayer.getCurrentEquippedItem().getItem();
    }
    
    public static boolean isHoldingSword() {
        return getHeldItem() instanceof ItemSword;
    }
    
    public static void click(final int slot, final int mouseButton, final boolean shiftClick) {
        InventoryUtils.mc.playerController.windowClick(InventoryUtils.mc.thePlayer.inventoryContainer.windowId, slot, mouseButton, shiftClick ? 1 : 0, InventoryUtils.mc.thePlayer);
    }
    
    public static void drop(final int slot) {
        InventoryUtils.mc.playerController.windowClick(0, slot, 1, 4, InventoryUtils.mc.thePlayer);
    }
    
    public static void swap(final int slot, final int hSlot) {
        InventoryUtils.mc.playerController.windowClick(InventoryUtils.mc.thePlayer.inventoryContainer.windowId, slot, hSlot, 2, InventoryUtils.mc.thePlayer);
    }
    
    public static float getSwordStrength(final ItemStack stack) {
        if (stack.getItem() instanceof ItemSword) {
            final ItemSword sword = (ItemSword)stack.getItem();
            final float sharpness = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f;
            final float fireAspect = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 1.5f;
            return sword.getDamageVsEntity() + sharpness + fireAspect;
        }
        return 0.0f;
    }
    
    public static boolean isItemEmpty(final Item item) {
        return item == null || Item.getIdFromItem(item) == 0;
    }
}
