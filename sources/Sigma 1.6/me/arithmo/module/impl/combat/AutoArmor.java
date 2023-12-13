/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.combat;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventTick;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor
extends Module {
    public static Timer timer = new Timer();

    public AutoArmor(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventTick.class})
    public void onEvent(Event event) {
        if (AutoArmor.mc.thePlayer != null && (AutoArmor.mc.currentScreen == null || AutoArmor.mc.currentScreen instanceof GuiInventory)) {
            int slotID = -1;
            double maxProt = -1.0;
            int switchArmor = -1;
            for (int i = 9; i < 45; ++i) {
                double protValue;
                ItemStack stack = AutoArmor.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack == null || !this.canEquip(stack) && (!this.betterCheck(stack) || this.canEquip(stack))) continue;
                if (this.betterCheck(stack) && switchArmor == -1) {
                    switchArmor = this.betterSwap(stack);
                }
                if ((protValue = this.getProtectionValue(stack)) < maxProt) continue;
                slotID = i;
                maxProt = protValue;
            }
            if (slotID != -1 && this.timer.delay(100.0f)) {
                if (switchArmor != -1) {
                    AutoArmor.mc.playerController.windowClick(AutoArmor.mc.thePlayer.inventoryContainer.windowId, 4 + switchArmor, 0, 0, AutoArmor.mc.thePlayer);
                    AutoArmor.mc.playerController.windowClick(AutoArmor.mc.thePlayer.inventoryContainer.windowId, -999, 0, 0, AutoArmor.mc.thePlayer);
                }
                AutoArmor.mc.playerController.windowClick(AutoArmor.mc.thePlayer.inventoryContainer.windowId, slotID, 0, 1, AutoArmor.mc.thePlayer);
                this.timer.reset();
            }
        }
    }

    private boolean betterCheck(ItemStack stack) {
        if (stack.getItem() instanceof ItemArmor) {
            if (AutoArmor.mc.thePlayer.getEquipmentInSlot(1) != null && stack.getUnlocalizedName().contains("boots") && this.getProtectionValue(stack) + (double)((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(AutoArmor.mc.thePlayer.getEquipmentInSlot(1)) + (double)((ItemArmor)AutoArmor.mc.thePlayer.getEquipmentInSlot((int)1).getItem()).damageReduceAmount) {
                return true;
            }
            if (AutoArmor.mc.thePlayer.getEquipmentInSlot(2) != null && stack.getUnlocalizedName().contains("leggings") && this.getProtectionValue(stack) + (double)((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(AutoArmor.mc.thePlayer.getEquipmentInSlot(2)) + (double)((ItemArmor)AutoArmor.mc.thePlayer.getEquipmentInSlot((int)2).getItem()).damageReduceAmount) {
                return true;
            }
            if (AutoArmor.mc.thePlayer.getEquipmentInSlot(3) != null && stack.getUnlocalizedName().contains("chestplate") && this.getProtectionValue(stack) + (double)((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(AutoArmor.mc.thePlayer.getEquipmentInSlot(3)) + (double)((ItemArmor)AutoArmor.mc.thePlayer.getEquipmentInSlot((int)3).getItem()).damageReduceAmount) {
                return true;
            }
            if (AutoArmor.mc.thePlayer.getEquipmentInSlot(4) != null && stack.getUnlocalizedName().contains("helmet") && this.getProtectionValue(stack) + (double)((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(AutoArmor.mc.thePlayer.getEquipmentInSlot(4)) + (double)((ItemArmor)AutoArmor.mc.thePlayer.getEquipmentInSlot((int)4).getItem()).damageReduceAmount) {
                return true;
            }
        }
        return false;
    }

    private int betterSwap(ItemStack stack) {
        if (stack.getItem() instanceof ItemArmor) {
            if (AutoArmor.mc.thePlayer.getEquipmentInSlot(1) != null && stack.getUnlocalizedName().contains("boots") && this.getProtectionValue(stack) + (double)((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(AutoArmor.mc.thePlayer.getEquipmentInSlot(1)) + (double)((ItemArmor)AutoArmor.mc.thePlayer.getEquipmentInSlot((int)1).getItem()).damageReduceAmount) {
                return 4;
            }
            if (AutoArmor.mc.thePlayer.getEquipmentInSlot(2) != null && stack.getUnlocalizedName().contains("leggings") && this.getProtectionValue(stack) + (double)((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(AutoArmor.mc.thePlayer.getEquipmentInSlot(2)) + (double)((ItemArmor)AutoArmor.mc.thePlayer.getEquipmentInSlot((int)2).getItem()).damageReduceAmount) {
                return 3;
            }
            if (AutoArmor.mc.thePlayer.getEquipmentInSlot(3) != null && stack.getUnlocalizedName().contains("chestplate") && this.getProtectionValue(stack) + (double)((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(AutoArmor.mc.thePlayer.getEquipmentInSlot(3)) + (double)((ItemArmor)AutoArmor.mc.thePlayer.getEquipmentInSlot((int)3).getItem()).damageReduceAmount) {
                return 2;
            }
            if (AutoArmor.mc.thePlayer.getEquipmentInSlot(4) != null && stack.getUnlocalizedName().contains("helmet") && this.getProtectionValue(stack) + (double)((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(AutoArmor.mc.thePlayer.getEquipmentInSlot(4)) + (double)((ItemArmor)AutoArmor.mc.thePlayer.getEquipmentInSlot((int)4).getItem()).damageReduceAmount) {
                return 1;
            }
        }
        return -1;
    }

    private boolean canEquip(ItemStack stack) {
        return AutoArmor.mc.thePlayer.getEquipmentInSlot(1) == null && stack.getUnlocalizedName().contains("boots") || AutoArmor.mc.thePlayer.getEquipmentInSlot(2) == null && stack.getUnlocalizedName().contains("leggings") || AutoArmor.mc.thePlayer.getEquipmentInSlot(3) == null && stack.getUnlocalizedName().contains("chestplate") || AutoArmor.mc.thePlayer.getEquipmentInSlot(4) == null && stack.getUnlocalizedName().contains("helmet");
    }

    private double getProtectionValue(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemArmor)) {
            return 0.0;
        }
        return (double)((ItemArmor)stack.getItem()).damageReduceAmount + (double)((100 - ((ItemArmor)stack.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 4) * 0.0075;
    }
}

