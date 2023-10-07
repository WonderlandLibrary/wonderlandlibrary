/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.combat;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class AutoSword
extends Module {
    public AutoSword(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventMotion.class})
    public void onEvent(Event event) {
        EventMotion em = (EventMotion)event;
        if (em.isPre() && AutoSword.mc.thePlayer.getCurrentEquippedItem() != null && AutoSword.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
            ItemSword currentSword = (ItemSword)AutoSword.mc.thePlayer.getCurrentEquippedItem().getItem();
            for (int i = 0; i < 45; ++i) {
                float currentDamage;
                float itemDamage;
                Item item;
                if (!AutoSword.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = AutoSword.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()) instanceof ItemSword) || (itemDamage = this.getSharpnessLevel(AutoSword.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) + ((ItemSword)item).getDamageGiven()) <= (currentDamage = this.getSharpnessLevel(AutoSword.mc.thePlayer.getCurrentEquippedItem()) + currentSword.getDamageGiven())) continue;
                this.swap(i, AutoSword.mc.thePlayer.inventory.currentItem);
                break;
            }
        }
    }

    protected void swap(int slot, int hotbarNum) {
        AutoSword.mc.playerController.windowClick(AutoSword.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, AutoSword.mc.thePlayer);
    }

    private float getSharpnessLevel(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSword)) {
            return 0.0f;
        }
        return (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f;
    }
}

