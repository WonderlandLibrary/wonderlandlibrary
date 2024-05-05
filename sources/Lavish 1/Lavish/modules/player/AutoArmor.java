// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.player;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import Lavish.utils.misc.NetUtil;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import Lavish.utils.misc.InventoryUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import Lavish.event.events.EventUpdate;
import Lavish.event.Event;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import Lavish.modules.Category;
import Lavish.utils.misc.Timer;
import Lavish.modules.Module;

public class AutoArmor extends Module
{
    public static boolean isSorting;
    Timer timer;
    
    static {
        AutoArmor.isSorting = false;
    }
    
    public AutoArmor() {
        super("AutoArmor", 0, true, Category.Player, "Puts armor on for you");
        this.timer = new Timer();
        Client.instance.setmgr.rSetting(new Setting("AutoArmor Speed", this, 20.0, 1.0, 500.0, true));
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventUpdate && e.isPre() && (AutoArmor.mc.currentScreen == null || AutoArmor.mc.currentScreen instanceof GuiInventory || (AutoArmor.mc.currentScreen instanceof GuiChat && !InvManager.isSorting))) {
            this.getBestArmor();
        }
    }
    
    public void getBestArmor() {
        for (int type = 1; type < 5; ++type) {
            if (AutoArmor.mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
                final ItemStack item = AutoArmor.mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                if (isBestArmor(item, type)) {
                    continue;
                }
                if (!(AutoArmor.mc.currentScreen instanceof GuiInventory)) {
                    final C16PacketClientStatus p = new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT);
                    AutoArmor.mc.thePlayer.sendQueue.addToSendQueue(p);
                }
                if (this.timer.hasTimeElapsed(Client.instance.setmgr.getSettingByName("AutoArmor Speed").getValDouble(), true)) {
                    InventoryUtils.drop(4 + type);
                    NetUtil.sendPacketNoEvents(new C0DPacketCloseWindow(0));
                }
            }
            for (int i = 9; i < 45; ++i) {
                if (AutoArmor.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    final ItemStack is = AutoArmor.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (isBestArmor(is, type) && getProtection(is) > 0.0f && this.timer.hasTimeElapsed(Client.instance.setmgr.getSettingByName("AutoArmor Speed").getValDouble(), true)) {
                        InventoryUtils.shiftClick(i);
                        NetUtil.sendPacketNoEvents(new C0DPacketCloseWindow(0));
                    }
                }
            }
        }
    }
    
    public static boolean isBestArmor(final ItemStack stack, final int type) {
        final float prot = getProtection(stack);
        String strType = "";
        if (type == 1) {
            strType = "helmet";
        }
        else if (type == 2) {
            strType = "chestplate";
        }
        else if (type == 3) {
            strType = "leggings";
        }
        else if (type == 4) {
            strType = "boots";
        }
        if (!stack.getUnlocalizedName().contains(strType)) {
            return false;
        }
        for (int i = 5; i < 45; ++i) {
            if (AutoArmor.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = AutoArmor.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getProtection(is) > prot && is.getUnlocalizedName().contains(strType)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static float getProtection(final ItemStack stack) {
        float prot = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            final ItemArmor armor = (ItemArmor)stack.getItem();
            prot += (float)(armor.damageReduceAmount + (100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) / 100.0);
        }
        return prot;
    }
}
