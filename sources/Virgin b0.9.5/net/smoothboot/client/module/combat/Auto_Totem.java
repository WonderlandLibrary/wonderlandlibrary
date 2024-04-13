package net.smoothboot.client.module.combat;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.settings.NumberSetting;

public class Auto_Totem extends Mod {

    public NumberSetting autoTotemSlot = new NumberSetting("Slot", 1, 9, 9, 1);
    public Auto_Totem() {
        super("Auto Totem", "", Category.Combat);
        addsettings(autoTotemSlot);
    }

    private boolean isTotem(ItemStack stack) {
        return stack.getItem() == Items.TOTEM_OF_UNDYING;
    }

    int i = mc.player.getInventory().indexOf(new ItemStack(Items.TOTEM_OF_UNDYING));

    @Override
    public void onTick() {
        if (!isTotem(mc.player.getOffHandStack()) && mc.currentScreen instanceof InventoryScreen) {
            if (i > 8 && i < 36 && isTotem(mc.player.getInventory().getStack(i))) {
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, i, SlotActionType.SWAP, mc.player);
                i++;
                return;
            }
            else if (i >= 36) {
                i = 9;
                return;
            }
            else {
                i++;
                return;
            }
        }
        if (!isTotem(mc.player.getMainHandStack()) && mc.currentScreen instanceof InventoryScreen) {
            if (i > 8 && i < 36 && isTotem(mc.player.getInventory().getStack(i))) {
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, autoTotemSlot.getValueInt() - 1 + 36, i, SlotActionType.SWAP, mc.player);
                i++;
            }
            else if (i >= 36) {
                i = 9;
            }
            else {
                i++;
            }
            super.onTick();
        }
    }


}