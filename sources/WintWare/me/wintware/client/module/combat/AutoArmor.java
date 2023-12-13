/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovementInput;

public class AutoArmor
extends Module {
    public AutoArmor() {
        super("AutoArmor", Category.Combat);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        ItemArmor item;
        ItemStack stack;
        if (AutoArmor.mc.currentScreen instanceof GuiContainer) {
            if (!(AutoArmor.mc.currentScreen instanceof InventoryEffectRenderer)) {
                return;
            }
        }
        InventoryPlayer inventory = Minecraft.player.inventory;
        if (MovementInput.moveStrafe == 0.0f) {
            if (MovementInput.moveStrafe != 0.0f) {
                return;
            }
        } else {
            return;
        }
        int[] bestArmorSlots = new int[4];
        int[] bestArmorValues = new int[4];
        for (int type = 0; type < 4; ++type) {
            bestArmorSlots[type] = -1;
            stack = inventory.armorItemInSlot(type);
            if (AutoArmor.isNullOrEmpty(stack) || !(stack.getItem() instanceof ItemArmor)) continue;
            item = (ItemArmor)stack.getItem();
            bestArmorValues[type] = this.getArmorValue(item, stack);
        }
        for (int slot = 0; slot < 36; ++slot) {
            stack = inventory.getStackInSlot(slot);
            if (AutoArmor.isNullOrEmpty(stack) || !(stack.getItem() instanceof ItemArmor)) continue;
            item = (ItemArmor)stack.getItem();
            int armorType = item.armorType.getIndex();
            int armorValue = this.getArmorValue(item, stack);
            if (armorValue <= bestArmorValues[armorType]) continue;
            bestArmorSlots[armorType] = slot;
            bestArmorValues[armorType] = armorValue;
        }
        ArrayList<Integer> types = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(types);
        for (int i : types) {
            ItemStack oldArmor;
            int j = bestArmorSlots[i];
            if (j == -1 || !AutoArmor.isNullOrEmpty(oldArmor = inventory.armorItemInSlot(i)) && inventory.getFirstEmptyStack() == -1) continue;
            if (j < 9) {
                j += 36;
            }
            if (!AutoArmor.isNullOrEmpty(oldArmor)) {
                AutoArmor.mc.playerController.windowClick(0, 8 - i, 0, ClickType.QUICK_MOVE, Minecraft.player);
            }
            AutoArmor.mc.playerController.windowClick(0, j, 0, ClickType.QUICK_MOVE, Minecraft.player);
            break;
        }
    }

    int getArmorValue(ItemArmor item, ItemStack stack) {
        int armorPoints = item.damageReduceAmount;
        int prtPoints = 0;
        int armorToughness = (int)item.toughness;
        int armorType = item.getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.LEGS);
        Enchantment protection = Enchantments.PROTECTION;
        int prtLvl = EnchantmentHelper.getEnchantmentLevel(protection, stack);
        EntityPlayerSP player = Minecraft.player;
        DamageSource dmgSource = DamageSource.causePlayerDamage(player);
        prtPoints = protection.calcModifierDamage(prtLvl, dmgSource);
        return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
    }

    public static boolean isNullOrEmpty(ItemStack stack) {
        return stack == null || stack.func_190926_b();
    }
}

