/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
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
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class AutoArmor
extends Feature {
    public static BooleanSetting openInventory;
    private final NumberSetting delay;
    public TimerHelper timerUtils = new TimerHelper();

    public AutoArmor() {
        super("AutoArmor", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043e\u0434\u0435\u0432\u0430\u0435\u0442 \u043b\u0443\u0447\u0448\u0443\u044e \u0431\u0440\u043e\u043d\u044e \u043d\u0430\u0445\u043e\u0434\u044f\u0449\u0438\u0435\u0441\u044f \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u0435", Type.Combat);
        this.delay = new NumberSetting("Equip Delay", 1.0f, 0.0f, 10.0f, 0.5f, () -> true);
        openInventory = new BooleanSetting("Open Inventory", true, () -> true);
        this.addSettings(this.delay, openInventory);
    }

    public static boolean isNullOrEmpty(ItemStack stack) {
        return stack == null || stack.isEmpty();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        ItemArmor item;
        ItemStack stack;
        this.setSuffix("" + this.delay.getCurrentValue());
        if (!(AutoArmor.mc.currentScreen instanceof GuiInventory) && openInventory.getCurrentValue()) {
            return;
        }
        if (AutoArmor.mc.currentScreen instanceof GuiContainer && !(AutoArmor.mc.currentScreen instanceof InventoryEffectRenderer)) {
            return;
        }
        InventoryPlayer inventory = AutoArmor.mc.player.inventory;
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
            if (!this.timerUtils.hasReached(this.delay.getCurrentValue() * 100.0f)) break;
            if (!AutoArmor.isNullOrEmpty(oldArmor)) {
                AutoArmor.mc.playerController.windowClick(0, 8 - i, 0, ClickType.QUICK_MOVE, AutoArmor.mc.player);
            }
            AutoArmor.mc.playerController.windowClick(0, j, 0, ClickType.QUICK_MOVE, AutoArmor.mc.player);
            this.timerUtils.reset();
            break;
        }
    }

    private int getArmorValue(ItemArmor item, ItemStack stack) {
        int armorPoints = item.damageReduceAmount;
        int prtPoints = 0;
        int armorToughness = (int)item.toughness;
        int armorType = item.getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.LEGS);
        Enchantment protection = Enchantments.PROTECTION;
        int prtLvl = EnchantmentHelper.getEnchantmentLevel(protection, stack);
        DamageSource dmgSource = DamageSource.causePlayerDamage(AutoArmor.mc.player);
        prtPoints = protection.calcModifierDamage(prtLvl, dmgSource);
        return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
    }
}

