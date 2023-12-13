// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.util.Timer;
import com.elementars.eclient.module.Module;

public class AutoArmor extends Module
{
    private Timer timer;
    private int[] bestArmorDamage;
    private int[] bestArmorSlots;
    private Value<Boolean> pif;
    private Value<Boolean> replace;
    private Value<Boolean> preserve;
    private Value<Integer> preserveDMG;
    private Value<Integer> ms;
    
    public AutoArmor() {
        super("AutoArmor", "Automatically refills armor", 0, Category.COMBAT, true);
        this.timer = new Timer();
        this.pif = this.register(new Value<Boolean>("Pickup If Full", this, true));
        this.replace = this.register(new Value<Boolean>("Replace Empty", this, true));
        this.preserve = this.register(new Value<Boolean>("Preserve Damaged", this, false));
        this.preserveDMG = this.register(new Value<Integer>("Damage %", this, 5, 0, 100));
        this.ms = this.register(new Value<Integer>("MS delay", this, 500, 0, 1000));
    }
    
    @Override
    public void onUpdate() {
        if (AutoArmor.mc.currentScreen instanceof GuiContainer && !(AutoArmor.mc.currentScreen instanceof InventoryEffectRenderer)) {
            return;
        }
        this.searchSlots();
        for (int i = 0; i < 4; ++i) {
            if (this.bestArmorSlots[i] != -1) {
                int bestSlot = this.bestArmorSlots[i];
                if (bestSlot < 9) {
                    bestSlot += 36;
                }
                if (!AutoArmor.mc.player.inventory.armorItemInSlot(i).isEmpty()) {
                    if (AutoArmor.mc.player.inventory.getFirstEmptyStack() == -1 && !AutoTotem.switchInProgress && !Offhand.switchInProgress && !MiddleClickPearl.switchInProgress && this.pif.getValue()) {
                        AutoArmor.mc.playerController.windowClick(AutoArmor.mc.player.inventoryContainer.windowId, 8 - i, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.player);
                        AutoArmor.mc.playerController.windowClick(AutoArmor.mc.player.inventoryContainer.windowId, bestSlot, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.player);
                        AutoArmor.mc.playerController.windowClick(AutoArmor.mc.player.inventoryContainer.windowId, 8 - i, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.player);
                        continue;
                    }
                    AutoArmor.mc.playerController.windowClick(AutoArmor.mc.player.inventoryContainer.windowId, 8 - i, 0, ClickType.QUICK_MOVE, (EntityPlayer)AutoArmor.mc.player);
                    if (!this.timer.hasReached(this.ms.getValue())) {
                        return;
                    }
                }
                AutoArmor.mc.playerController.windowClick(AutoArmor.mc.player.inventoryContainer.windowId, bestSlot, 0, ClickType.QUICK_MOVE, (EntityPlayer)AutoArmor.mc.player);
                this.timer.reset();
            }
        }
    }
    
    private void searchSlots() {
        this.bestArmorDamage = new int[4];
        this.bestArmorSlots = new int[4];
        Arrays.fill(this.bestArmorDamage, -1);
        Arrays.fill(this.bestArmorSlots, -1);
        for (int i = 0; i < this.bestArmorSlots.length; ++i) {
            final ItemStack itemStack = AutoArmor.mc.player.inventory.armorItemInSlot(i);
            if (itemStack.getItem() instanceof ItemArmor) {
                final ItemArmor armor = (ItemArmor)itemStack.getItem();
                if (this.preserve.getValue()) {
                    final float dmg = (itemStack.getMaxDamage() - (float)itemStack.getItemDamage()) / itemStack.getMaxDamage();
                    final int percent = (int)(dmg * 100.0f);
                    if (percent > this.preserveDMG.getValue()) {
                        this.bestArmorDamage[i] = armor.damageReduceAmount;
                    }
                }
                else {
                    this.bestArmorDamage[i] = armor.damageReduceAmount;
                }
            }
            else if (itemStack.isEmpty() && !this.replace.getValue()) {
                this.bestArmorDamage[i] = Integer.MAX_VALUE;
            }
        }
        for (int i = 0; i < 36; ++i) {
            final ItemStack itemStack = AutoArmor.mc.player.inventory.getStackInSlot(i);
            if (itemStack.getCount() <= 1) {
                if (itemStack.getItem() instanceof ItemArmor) {
                    final ItemArmor armor = (ItemArmor)itemStack.getItem();
                    final int armorType = armor.armorType.ordinal() - 2;
                    if (this.bestArmorDamage[armorType] < armor.damageReduceAmount) {
                        this.bestArmorDamage[armorType] = armor.damageReduceAmount;
                        this.bestArmorSlots[armorType] = i;
                    }
                }
            }
        }
    }
}
