// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.COMBAT;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.DamageSource;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import ru.tuskevich.event.EventTarget;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.item.ItemArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.util.math.TimerUtility;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "AutoArmor", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.COMBAT)
public class AutoArmor extends Module
{
    public BooleanSetting openInventory;
    public SliderSetting delay;
    private final TimerUtility timerUtils;
    
    public AutoArmor() {
        this.openInventory = new BooleanSetting("openInventory", true);
        this.delay = new SliderSetting("Delay", 1.0f, 0.1f, 10.0f, 0.1f);
        this.timerUtils = new TimerUtility();
        this.add(this.openInventory, this.delay);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        if (!(AutoArmor.mc.currentScreen instanceof GuiInventory) && this.openInventory.get()) {
            return;
        }
        if (AutoArmor.mc.currentScreen instanceof GuiContainer && !(AutoArmor.mc.currentScreen instanceof InventoryEffectRenderer)) {
            return;
        }
        final Minecraft mc = AutoArmor.mc;
        final InventoryPlayer inventory = Minecraft.player.inventory;
        final int[] ArmorSlots = new int[4];
        final int[] ArmorValues = new int[4];
        for (int type = 0; type < 4; ++type) {
            ArmorSlots[type] = -1;
            final ItemStack stack = inventory.armorItemInSlot(type);
            if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {
                final ItemArmor item = (ItemArmor)stack.getItem();
                ArmorValues[type] = this.getArmorValue(item, stack);
            }
        }
        for (int slot = 0; slot < 36; ++slot) {
            final ItemStack stack = inventory.getStackInSlot(slot);
            if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {
                final ItemArmor item = (ItemArmor)stack.getItem();
                final int armorType = item.armorType.getIndex();
                final int armorValue = this.getArmorValue(item, stack);
                if (armorValue > ArmorValues[armorType]) {
                    ArmorSlots[armorType] = slot;
                    ArmorValues[armorType] = armorValue;
                }
            }
        }
        final ArrayList<Integer> types = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(types);
        for (final int i : types) {
            int j = ArmorSlots[i];
            if (j == -1) {
                continue;
            }
            final ItemStack oldArmor = inventory.armorItemInSlot(i);
            if (!isNullOrEmpty(oldArmor) && inventory.getFirstEmptyStack() == -1) {
                continue;
            }
            if (j < 9) {
                j += 36;
            }
            if (this.timerUtils.hasTimeElapsed((long)(this.delay.getFloatValue() * 100.0f))) {
                if (!isNullOrEmpty(oldArmor)) {
                    final PlayerControllerMP playerController = AutoArmor.mc.playerController;
                    final int windowId = 0;
                    final int slotId = 8 - i;
                    final int mouseButton = 0;
                    final ClickType quick_MOVE = ClickType.QUICK_MOVE;
                    final Minecraft mc2 = AutoArmor.mc;
                    playerController.windowClick(windowId, slotId, mouseButton, quick_MOVE, Minecraft.player);
                }
                final PlayerControllerMP playerController2 = AutoArmor.mc.playerController;
                final int windowId2 = 0;
                final int slotId2 = j;
                final int mouseButton2 = 0;
                final ClickType quick_MOVE2 = ClickType.QUICK_MOVE;
                final Minecraft mc3 = AutoArmor.mc;
                playerController2.windowClick(windowId2, slotId2, mouseButton2, quick_MOVE2, Minecraft.player);
                this.timerUtils.reset();
                break;
            }
            break;
        }
    }
    
    private int getArmorValue(final ItemArmor item, final ItemStack stack) {
        final int armorPoints = item.damageReduceAmount;
        int prtPoints = 0;
        final int armorToughness = (int)item.toughness;
        final int armorType = item.getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.LEGS);
        final Enchantment protection = Enchantments.PROTECTION;
        final int prtLvl = EnchantmentHelper.getEnchantmentLevel(protection, stack);
        final Minecraft mc = AutoArmor.mc;
        final DamageSource dmgSource = DamageSource.causePlayerDamage(Minecraft.player);
        prtPoints = protection.calcModifierDamage(prtLvl, dmgSource);
        return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
    }
    
    public static boolean isNullOrEmpty(final ItemStack stack) {
        return stack == null || stack.isEmpty();
    }
}
