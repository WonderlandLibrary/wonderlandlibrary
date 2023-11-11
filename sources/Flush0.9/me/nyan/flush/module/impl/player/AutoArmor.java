package me.nyan.flush.module.impl.player;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.other.MathUtils;
import me.nyan.flush.utils.other.Timer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;

public class AutoArmor extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "SpoofInv", "OpenInv");
    private final NumberSetting delay = new NumberSetting("Delay", this, 80, 0, 300);

    private boolean inventoryOpen;
    private final Timer timer = new Timer();

    public AutoArmor() {
        super("AutoArmor", Category.PLAYER);
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if (e.isOutgoing()) {
            if (e.getPacket() instanceof C16PacketClientStatus) {
                C16PacketClientStatus status = e.getPacket();

                if (status.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT)
                    inventoryOpen = true;
            }

            if (e.getPacket() instanceof C0DPacketCloseWindow)
                inventoryOpen = false;
        }
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if (e.isPost() || mc.currentScreen instanceof GuiChest || (!(mc.currentScreen instanceof GuiInventory) && mode.is("openinv"))) {
            return;
        }

        int slot = -1;
        int badSlot = -1;
        double maxProtection = -1;

        for (int i = 9; i < 45; ++i) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (stack != null && (canEquip(stack) || isBetter(stack) && !canEquip(stack))) {
                if (isBetter(stack) && badSlot == -1)
                    badSlot = getArmorToDrop(stack);

                double protectionValue = getProtectionValue(stack);
                if (protectionValue >= maxProtection) {
                    slot = i;
                    maxProtection = protectionValue;
                }
            }
        }

        if (slot != -1) {
            openInvPacket();
            if (timer.hasTimeElapsed(delay.getValueInt() + MathUtils.getRandomNumber(0, 10), true)) {
                if (badSlot != -1) {
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 4 + badSlot, 1, 4, mc.thePlayer);
                }

                mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);
            }
            closeInvPacket();
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }

    public boolean isBetter(ItemStack stack) {
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) stack.getItem();
            return (mc.thePlayer.getEquipmentInSlot(1) != null && armor.armorType == 3 && getProtectionValue(stack) +
                    (double) armor.damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(1)) +
                    (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(1).getItem()).damageReduceAmount) ||
                    (mc.thePlayer.getEquipmentInSlot(2) != null && armor.armorType == 2 && getProtectionValue(stack) +
                            (double) armor.damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(2)) +
                            (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(2).getItem()).damageReduceAmount) ||
                    (mc.thePlayer.getEquipmentInSlot(3) != null && armor.armorType == 1 && getProtectionValue(stack) +
                            (double) armor.damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(3)) +
                            (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(3).getItem()).damageReduceAmount) ||
                    (mc.thePlayer.getEquipmentInSlot(4) != null && armor.armorType == 0 && getProtectionValue(stack) +
                            (double) armor.damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(4)) +
                            (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(4).getItem()).damageReduceAmount);
        }
        return false;
    }

    private int getArmorToDrop(ItemStack stack) {
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) stack.getItem();
            if (mc.thePlayer.getEquipmentInSlot(4) != null && armor.armorType == 0 && getProtectionValue(stack) +
                    (double) ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(4)) +
                    (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(4).getItem()).damageReduceAmount)
                return 1;

            if (mc.thePlayer.getEquipmentInSlot(3) != null && armor.armorType == 1 && getProtectionValue(stack) +
                    (double) ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(3)) + (double)
                    ((ItemArmor) mc.thePlayer.getEquipmentInSlot(3).getItem()).damageReduceAmount)
                return 2;

            if (mc.thePlayer.getEquipmentInSlot(2) != null && armor.armorType == 2 && getProtectionValue(stack) +
                    (double) ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(2)) +
                    (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(2).getItem()).damageReduceAmount)
                return 3;

            if (mc.thePlayer.getEquipmentInSlot(1) != null && armor.armorType == 3 && getProtectionValue(stack) +
                    (double) ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(1)) +
                    (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(1).getItem()).damageReduceAmount)
                return 4;
        }
        return -1;
    }

    private boolean canEquip(ItemStack stack) {
        return mc.thePlayer.getEquipmentInSlot(1) == null && stack.getUnlocalizedName().contains("boots") ||
                mc.thePlayer.getEquipmentInSlot(2) == null && stack.getUnlocalizedName().contains("leggings") ||
                mc.thePlayer.getEquipmentInSlot(3) == null && stack.getUnlocalizedName().contains("chestplate") ||
                mc.thePlayer.getEquipmentInSlot(4) == null && stack.getUnlocalizedName().contains("helmet");
    }

    private double getProtectionValue(ItemStack stack) {
        return !(stack.getItem() instanceof ItemArmor) ? 0.0D : (double) ((ItemArmor) stack.getItem()).damageReduceAmount + (double) ((100 - ((ItemArmor)
                stack.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 4) * 0.0075;
    }

    private void openInvPacket() {
        if (mode.is("spoofinv")) {
            if (!inventoryOpen)
                mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            inventoryOpen = true;
        }
    }

    private void closeInvPacket() {
        if (mode.is("spoofinv")) {
            if (inventoryOpen)
                mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(mc.thePlayer.inventoryContainer.windowId));
            inventoryOpen = false;
        }
    }
}
