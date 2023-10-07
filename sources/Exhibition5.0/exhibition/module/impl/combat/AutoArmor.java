// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.combat;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemArmor;
import exhibition.event.impl.EventTick;
import exhibition.event.RegisterEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.inventory.GuiInventory;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.util.Timer;
import exhibition.module.Module;

public class AutoArmor extends Module
{
    private Timer timer;
    
    public AutoArmor(final ModuleData data) {
        super(data);
        this.timer = new Timer();
    }
    
    @RegisterEvent(events = { EventTick.class })
    @Override
    public void onEvent(final Event event) {
        if (AutoArmor.mc.thePlayer != null && (AutoArmor.mc.currentScreen == null || AutoArmor.mc.currentScreen instanceof GuiInventory)) {
            int slotID = -1;
            double maxProt = -1.0;
            int switchArmor = -1;
            for (int i = 9; i < 45; ++i) {
                final ItemStack stack = AutoArmor.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack != null && (this.canEquip(stack) || (this.betterCheck(stack) && !this.canEquip(stack)))) {
                    if (this.betterCheck(stack) && switchArmor == -1) {
                        switchArmor = this.betterSwap(stack);
                    }
                    final double protValue = this.getProtectionValue(stack);
                    if (protValue >= maxProt) {
                        slotID = i;
                        maxProt = protValue;
                    }
                }
            }
            if (slotID != -1 && this.timer.delay(200.0f)) {
                if (switchArmor != -1) {
                    AutoArmor.mc.playerController.windowClick(AutoArmor.mc.thePlayer.inventoryContainer.windowId, 4 + switchArmor, 0, 0, AutoArmor.mc.thePlayer);
                    AutoArmor.mc.playerController.windowClick(AutoArmor.mc.thePlayer.inventoryContainer.windowId, -999, 0, 0, AutoArmor.mc.thePlayer);
                    this.timer.reset();
                }
                else {
                    AutoArmor.mc.playerController.windowClick(AutoArmor.mc.thePlayer.inventoryContainer.windowId, slotID, 0, 1, AutoArmor.mc.thePlayer);
                    this.timer.reset();
                }
            }
        }
    }
    
    private boolean betterCheck(final ItemStack stack) {
        try {
            if (stack.getItem() instanceof ItemArmor) {
                if (AutoArmor.mc.thePlayer.getEquipmentInSlot(1) != null && stack.getUnlocalizedName().contains("boots")) {
                    assert AutoArmor.mc.thePlayer.getEquipmentInSlot(1).getItem() instanceof ItemArmor;
                    if (this.getProtectionValue(stack) + ((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(AutoArmor.mc.thePlayer.getEquipmentInSlot(1)) + ((ItemArmor)AutoArmor.mc.thePlayer.getEquipmentInSlot(1).getItem()).damageReduceAmount) {
                        return true;
                    }
                }
                if (AutoArmor.mc.thePlayer.getEquipmentInSlot(2) != null && stack.getUnlocalizedName().contains("leggings")) {
                    assert AutoArmor.mc.thePlayer.getEquipmentInSlot(2).getItem() instanceof ItemArmor;
                    if (this.getProtectionValue(stack) + ((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(AutoArmor.mc.thePlayer.getEquipmentInSlot(2)) + ((ItemArmor)AutoArmor.mc.thePlayer.getEquipmentInSlot(2).getItem()).damageReduceAmount) {
                        return true;
                    }
                }
                if (AutoArmor.mc.thePlayer.getEquipmentInSlot(3) != null && stack.getUnlocalizedName().contains("chestplate")) {
                    assert AutoArmor.mc.thePlayer.getEquipmentInSlot(3).getItem() instanceof ItemArmor;
                    if (this.getProtectionValue(stack) + ((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(AutoArmor.mc.thePlayer.getEquipmentInSlot(3)) + ((ItemArmor)AutoArmor.mc.thePlayer.getEquipmentInSlot(3).getItem()).damageReduceAmount) {
                        return true;
                    }
                }
                if (AutoArmor.mc.thePlayer.getEquipmentInSlot(4) != null && stack.getUnlocalizedName().contains("helmet")) {
                    assert AutoArmor.mc.thePlayer.getEquipmentInSlot(4).getItem() instanceof ItemArmor;
                    if (this.getProtectionValue(stack) + ((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(AutoArmor.mc.thePlayer.getEquipmentInSlot(4)) + ((ItemArmor)AutoArmor.mc.thePlayer.getEquipmentInSlot(4).getItem()).damageReduceAmount) {
                        return true;
                    }
                }
            }
        }
        catch (Exception e) {
            return false;
        }
        return false;
    }
    
    private int betterSwap(final ItemStack stack) {
        if (stack.getItem() instanceof ItemArmor) {
            if (AutoArmor.mc.thePlayer.getEquipmentInSlot(1) != null && stack.getUnlocalizedName().contains("boots")) {
                assert AutoArmor.mc.thePlayer.getEquipmentInSlot(1).getItem() instanceof ItemArmor;
                if (this.getProtectionValue(stack) + ((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(AutoArmor.mc.thePlayer.getEquipmentInSlot(1)) + ((ItemArmor)AutoArmor.mc.thePlayer.getEquipmentInSlot(1).getItem()).damageReduceAmount) {
                    return 4;
                }
            }
            if (AutoArmor.mc.thePlayer.getEquipmentInSlot(2) != null && stack.getUnlocalizedName().contains("leggings")) {
                assert AutoArmor.mc.thePlayer.getEquipmentInSlot(2).getItem() instanceof ItemArmor;
                if (this.getProtectionValue(stack) + ((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(AutoArmor.mc.thePlayer.getEquipmentInSlot(2)) + ((ItemArmor)AutoArmor.mc.thePlayer.getEquipmentInSlot(2).getItem()).damageReduceAmount) {
                    return 3;
                }
            }
            if (AutoArmor.mc.thePlayer.getEquipmentInSlot(3) != null && stack.getUnlocalizedName().contains("chestplate")) {
                assert AutoArmor.mc.thePlayer.getEquipmentInSlot(1).getItem() instanceof ItemArmor;
                if (this.getProtectionValue(stack) + ((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(AutoArmor.mc.thePlayer.getEquipmentInSlot(3)) + ((ItemArmor)AutoArmor.mc.thePlayer.getEquipmentInSlot(3).getItem()).damageReduceAmount) {
                    return 2;
                }
            }
            if (AutoArmor.mc.thePlayer.getEquipmentInSlot(4) != null && stack.getUnlocalizedName().contains("helmet")) {
                assert AutoArmor.mc.thePlayer.getEquipmentInSlot(1).getItem() instanceof ItemArmor;
                if (this.getProtectionValue(stack) + ((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(AutoArmor.mc.thePlayer.getEquipmentInSlot(4)) + ((ItemArmor)AutoArmor.mc.thePlayer.getEquipmentInSlot(4).getItem()).damageReduceAmount) {
                    return 1;
                }
            }
        }
        return -1;
    }
    
    private boolean canEquip(final ItemStack stack) {
        assert stack.getItem() instanceof ItemArmor;
        return (AutoArmor.mc.thePlayer.getEquipmentInSlot(1) == null && stack.getUnlocalizedName().contains("boots")) || (AutoArmor.mc.thePlayer.getEquipmentInSlot(2) == null && stack.getUnlocalizedName().contains("leggings")) || (AutoArmor.mc.thePlayer.getEquipmentInSlot(3) == null && stack.getUnlocalizedName().contains("chestplate")) || (AutoArmor.mc.thePlayer.getEquipmentInSlot(4) == null && stack.getUnlocalizedName().contains("helmet"));
    }
    
    private double getProtectionValue(final ItemStack stack) {
        if (stack.getItem() instanceof ItemArmor) {
            return ((ItemArmor)stack.getItem()).damageReduceAmount + (100 - ((ItemArmor)stack.getItem()).damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075;
        }
        return 0.0;
    }
}
