package info.sigmaclient.module.impl.combat;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.impl.player.InventoryCleaner;
import info.sigmaclient.util.Timer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module {

    private Timer timer = new Timer();


    public AutoArmor(ModuleData data) {
        super(data);
    }

    @Override
    @RegisterEvent(events = {EventTick.class})
    public void onEvent(Event event) {
        if(Client.getModuleManager().get(InventoryCleaner.class).isEnabled() && InventoryCleaner.isCleaning())
            return;
        if (mc.thePlayer != null && (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory)) {
            int slotID = -1;
            double maxProt = -1.0D;
            int switchArmor = -1;
            for (int i = 9; i < 45; i++) {
                ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack != null && (canEquip(stack) || (betterCheck(stack) && !canEquip(stack)))) {
                    if (betterCheck(stack) && switchArmor == -1) {
                        switchArmor = betterSwap(stack);
                    }
                    double protValue = getProtectionValue(stack);
                    if (protValue >= maxProt) {
                        slotID = i;
                        maxProt = protValue;
                    }
                }
            }

            if (slotID != -1 && timer.delay(200)) {
                if (switchArmor != -1) {
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 4 + switchArmor, 0, 0, mc.thePlayer);
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, -999, 0, 0, mc.thePlayer);
                    timer.reset();
                } else {
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slotID, 0, 1, mc.thePlayer);
                    timer.reset();
                }
            }
        }
    }


    private boolean betterCheck(ItemStack stack) {
        try {
            if (stack.getItem() instanceof ItemArmor) {
                if (mc.thePlayer.getEquipmentInSlot(1) != null && stack.getUnlocalizedName().contains("boots")) {
                    assert mc.thePlayer.getEquipmentInSlot(1).getItem() instanceof ItemArmor;
                    if (getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(1)) + ((ItemArmor) mc.thePlayer.getEquipmentInSlot(1).getItem()).damageReduceAmount) {
                        return true;
                    }
                }
                if (mc.thePlayer.getEquipmentInSlot(2) != null && stack.getUnlocalizedName().contains("leggings")) {
                    assert mc.thePlayer.getEquipmentInSlot(2).getItem() instanceof ItemArmor;
                    if (getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(2)) + ((ItemArmor) mc.thePlayer.getEquipmentInSlot(2).getItem()).damageReduceAmount) {
                        return true;
                    }
                }
                if (mc.thePlayer.getEquipmentInSlot(3) != null && stack.getUnlocalizedName().contains("chestplate")) {
                    assert mc.thePlayer.getEquipmentInSlot(3).getItem() instanceof ItemArmor;
                    if (getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(3)) + ((ItemArmor) mc.thePlayer.getEquipmentInSlot(3).getItem()).damageReduceAmount) {
                        return true;
                    }
                }
                if (mc.thePlayer.getEquipmentInSlot(4) != null && stack.getUnlocalizedName().contains("helmet")) {
                    assert mc.thePlayer.getEquipmentInSlot(4).getItem() instanceof ItemArmor;
                    if (getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(4)) + ((ItemArmor) mc.thePlayer.getEquipmentInSlot(4).getItem()).damageReduceAmount) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private int betterSwap(ItemStack stack) {
        if (stack.getItem() instanceof ItemArmor) {
            if (mc.thePlayer.getEquipmentInSlot(1) != null && stack.getUnlocalizedName().contains("boots")) {
                assert mc.thePlayer.getEquipmentInSlot(1).getItem() instanceof ItemArmor;
                if (getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(1)) + ((ItemArmor) mc.thePlayer.getEquipmentInSlot(1).getItem()).damageReduceAmount) {
                    return 4;
                }
            }
            if (mc.thePlayer.getEquipmentInSlot(2) != null && stack.getUnlocalizedName().contains("leggings")) {
                assert mc.thePlayer.getEquipmentInSlot(2).getItem() instanceof ItemArmor;
                if (getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(2)) + ((ItemArmor) mc.thePlayer.getEquipmentInSlot(2).getItem()).damageReduceAmount) {
                    return 3;
                }
            }
            if (mc.thePlayer.getEquipmentInSlot(3) != null && stack.getUnlocalizedName().contains("chestplate")) {
                assert mc.thePlayer.getEquipmentInSlot(1).getItem() instanceof ItemArmor;
                if (getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(3)) + ((ItemArmor) mc.thePlayer.getEquipmentInSlot(3).getItem()).damageReduceAmount) {
                    return 2;
                }
            }
            if (mc.thePlayer.getEquipmentInSlot(4) != null && stack.getUnlocalizedName().contains("helmet")) {
                assert mc.thePlayer.getEquipmentInSlot(1).getItem() instanceof ItemArmor;
                if (getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(4)) + ((ItemArmor) mc.thePlayer.getEquipmentInSlot(4).getItem()).damageReduceAmount) {
                    return 1;
                }
            }
        }
        return -1;
    }

    private boolean canEquip(ItemStack stack) {
        assert stack.getItem() instanceof ItemArmor;
        return ((mc.thePlayer.getEquipmentInSlot(1) == null) && (stack.getUnlocalizedName().contains("boots")))
                || ((mc.thePlayer.getEquipmentInSlot(2) == null) && (stack.getUnlocalizedName().contains("leggings")))
                || ((mc.thePlayer.getEquipmentInSlot(3) == null) && (stack.getUnlocalizedName().contains("chestplate")))
                || ((mc.thePlayer.getEquipmentInSlot(4) == null) && (stack.getUnlocalizedName().contains("helmet")));
    }

    private double getProtectionValue(ItemStack stack) {
        if ((stack.getItem() instanceof ItemArmor)) {//floor ( [ 6 + level ^ 2 ] * .75 / 3 )​
            return ((ItemArmor) stack.getItem()).damageReduceAmount + (100 - ((ItemArmor) stack.getItem()).damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075D;
        }
        return 0;
    }
}
